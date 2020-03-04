package frc.robot.vision.parabolic;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * Manages the TFMini time-of-flight infrared distance sensor
 * Inner workings adapted from https://github.com/jkoehring/SensorTests2018/blob/master/SensorTests2018/src/org/usfirst/frc/team6352/robot/subsystems/TFMini.java
 * 
 *  Info
 * We get data in centimeters! Convenient!
 * 16 bits of uwu
 * 
 * Data Frame Format:
 *  byte 0: 0x59 header
 *  byte 1: 0x59 header
 *  byte 2: Distance low byte
 *  byte 3: Distance high byte
 *  byte 4: Strength low byte
 *  byte 5: Strength high byte
 *  byte 6: Integration time
 *  byte 7: reserved
 *  byte 8: Checksum (low byte of sum)
 * 
 * @author Alex Pickering
 */
public class TFMini implements Runnable {
    
    // Interface to UART through the NavX
    private SerialPort uartPort;
    
    // Values
    private int distance,
                strength,
                time;
    
    /**
     * Initializes the TFMini
     */
    public TFMini() {
        // Initialize the uart according to datasheet
        uartPort = new SerialPort(115200, SerialPort.Port.kMXP, 8, SerialPort.Parity.kEven, SerialPort.StopBits.kOne);
        
        // Run our management stuff
        new Thread(this).start();
    }
    
    public int getDistance() { return distance; }
    public int getStrength() { return strength; }
    public int getIntegrationTime() { return time; }

    @Override
    public void run() {
        // Flush serial port buffers
        uartPort.reset();
        
        byte[] frameData = new byte[9], // 9 bytes per frame
               dataBuffer = new byte[0];// Initialized later
        
        int bytesReceivedSinceHeader = 0;
        
        boolean hasHeader = false; // If we have a header and are looking to complete the data
        
        // Forever
        outerLoop:
        while(true) {
            // Wait until we have enough bytes to fill a frame
            if(uartPort.getBytesReceived() + bytesReceivedSinceHeader < 9) {
                System.out.println("TFMINI: Waiting for more bytes");
                continue;
            }
            
            // Get new bytes
            // Merge old and new data
            System.out.println("TFMINI: Getting new data");
            byte[] uartBuffer = uartPort.read(uartPort.getBytesReceived()),
                   mergeBuffer = new byte[dataBuffer.length + uartBuffer.length];
            
            System.arraycopy(dataBuffer, 0, mergeBuffer, 0, dataBuffer.length);                 // Put the old data in
            System.arraycopy(uartBuffer, 0, mergeBuffer, dataBuffer.length, uartBuffer.length); // Append the new data
            
            
            // Scan for a header if we don't have one
            if(!hasHeader) {
                // Loop over combined data
                for(int i = 0; i < mergeBuffer.length; i++) {
                    if(mergeBuffer[i] == 0x59) { // We found a header
                        System.out.println("TFMINI: Found header");
                        hasHeader = true;
                        bytesReceivedSinceHeader = mergeBuffer.length - i;
                        
                        // Get new data buffer with data since header
                        dataBuffer = new byte[bytesReceivedSinceHeader];
                        System.arraycopy(mergeBuffer, i, dataBuffer, 0, dataBuffer.length);
                        break;
                    }
                }
                
                // Didn't find a header
                if(!hasHeader) {
                    System.out.println("TFMINI: No header");
                    continue;
                }
            }
            
            // Get data until we have a full frame
            for(int i = 0; i < 9; i++) {
                // Continue the outer loop if we run out of data
                if(i >= dataBuffer.length) {
                    System.out.println("TFMINI: Too little data found");
                    continue outerLoop;
                }
                
                frameData[i] = dataBuffer[i];
            }
            
            // If we're here, frameData is full with a buffer
            // Check that we have a correct buffer
            if(frameData[1] != 0x59) {
                if(dataBuffer.length < 10) {
                    // If we don't have enough data for a full frame with the off-by-one, abandon it
                    System.out.println("TFMINI: ERROR TOO LITTLE DATA");
                    
                    hasHeader = false;
                    makeNewDataBuffer(dataBuffer);
                    continue;
                }
                
                // Off-by-1 error, we need to shift right
                frameData = shiftArray(frameData);
            }
            
            // Validate the frame, if we're here we have one
            int sum = 0;
            for(int i = 0; i < 8; i++) sum++;
            
            if(frameData[8] != (sum & 0xFF)) {
                System.out.println("TFMINI: ERROR CHECKSUM WRONG");
                
                // Checksum doesn't match, get new frame
                makeNewDataBuffer(dataBuffer);
                hasHeader = false;
                continue;
            }
            
            // Valid, update values
            distance = (frameData[2] << 8) | frameData[3];
            strength = (frameData[4] << 8) | frameData[5];
            time = frameData[6];
            
            // Tell it to wait for new data
            makeNewDataBuffer(dataBuffer);
            hasHeader = false;
            continue;
        }
    }
    
    /**
     * Eliminates a frame's worth of bytes from the data buffer
     * 
     * @param dataBuffer
     * @return
     */
    private byte[] makeNewDataBuffer(byte[] dataBuffer) {
        int len = (dataBuffer.length - 9 < 0) ? 0 : (dataBuffer.length - 9);
        byte[] newData = new byte[len];
        
        System.arraycopy(dataBuffer, dataBuffer.length - len, newData, 0, len);
        
        return newData;
    }
    
    /**
     * Returns a new array of arr shifted right by 1
     * 
     * @param arr
     * @return
     */
    private byte[] shiftArray(byte[] arr) {
        byte[] newArr = new byte[arr.length + 1];
        
        System.arraycopy(arr, 0, newArr, 1, arr.length);
        newArr[0] = arr[0];
        
        return newArr;
    }
}






