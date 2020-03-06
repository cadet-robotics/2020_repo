package frc.robot.io;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Uses the MB1013 ultrasonic distance sensor
 * 
 * @author Alex Pickering
 */
public class MB1013 extends AnalogInput {
    
    /**
     * Standard AnalogInput constructor
     * @param channel
     */
    public MB1013(int channel) {
        super(channel);
    }
    
    /**
     * Returns the measured distance in meters
     * 
     * @return
     */
    public double getDistance() {
        // 1,024 mm per volt
        return (getAverageVoltage() * 1024) / 1000;
    }
}
