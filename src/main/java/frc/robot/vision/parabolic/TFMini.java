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
    
    private static final byte HEADER = 0x59;
    
    // Interface to UART through the NavX
    private SerialPort uartPort;
    
    // Values
    private int distance,
                strength;
    
    /**
     * Initializes the TFMini
     */
    public TFMini() {
        // Initialize the uart according to datasheet
        uartPort = new SerialPort(115200, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
        
        // Run our management stuff
        new Thread(this).start();
    }
    
    /**
     * Gets distance in meters
     * @return
     */
    public int getDistance() { return distance; }
    
    /**
     * Gets signal strength in
     * @return
     */
    public int getStrength() { return strength; }

    @Override
    public void run() {
        // Flush serial port buffers
        uartPort.reset();
        
        byte[] frame = new byte[9];
        
        mainLoop:
        while(true) {
            // Grab a byte
            waitForByte();
            
            byte b = uartPort.read(1)[0];               //0xFF casts to int without sign-extension
            System.out.println("TFMini: Got byte: 0x" + Integer.toHexString(b & 0xFF).toUpperCase());
            
            // Header start found
            if(b == HEADER) {
                System.out.println("TFMini: Started frame");
                
                frame = new byte[9];
                frame[0] = HEADER;
                
                // Get next bytes
                for(int i = 1; i < 9; i++) {
                    waitForByte();
                    b = uartPort.read(1)[0];
                    
                    // Make sure the header is proper
                    if(i == 1 && b != HEADER) {
                        System.out.println("TFMini: Header failed");
                        continue mainLoop;
                    }
                    
                    frame[i] = b;
                }
                
                // Checksum
                int sum = 0;
                for(int i = 0; i < 8; i++) sum += frame[i];
                
                if(sum != frame[8]) {
                    System.out.println("TFMini: Checksum failed");
                    continue mainLoop;
                }
                
                System.out.println("TFMini: Updating values");
                
                // Update values
                distance = ((frame[3] << 8) | frame[2]) / 100;
                strength = (frame[5] << 8) | frame[4];
            }
        }
    }
    
    /**
     * Waits until a byte is available
     */
    private void waitForByte() {
        int msgTimer = 0;
        
        while(uartPort.getBytesReceived() == 0) {
            if(msgTimer++ % 1_000 == 0) {
                if(msgTimer == 100_000) {
                    System.out.println("TFMini: Wating. (100k)");
                    msgTimer = 1;
                } else {
                    System.out.println("TFMini: Waiting.");
                }
            }
            
            try {
                // nice us
                Thread.sleep(0, 690_000);
            } catch(InterruptedException e) {}
        }
    }
}






