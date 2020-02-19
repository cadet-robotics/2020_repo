package frc.robot.io;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc6868.config.api.Config;

public class Sensors {
    public static Encoder topFlyEncoder,
                          bottomFlyEncoder,
                          driveEncoderLeft,
                          driveEncoderRight;
    public static Gyro gyro;
    
    // Proximity sensors for the magazine
    public static DigitalInput topBallSensor,
                               middleBallSensor,
                               bottomBallSensor;

    public static void loadConfig(Config c) {
        // *jojo's noises*
        Config dioSensors = c.separateCategory("sensors");
        
        // Magazine prox sensors
        topBallSensor = new DigitalInput(dioSensors.getIntValue("top ball sensor"));
        middleBallSensor = new DigitalInput(dioSensors.getIntValue("middle ball sensor"));
        bottomBallSensor = new DigitalInput(dioSensors.getIntValue("bottom ball sensor"));
    }
}