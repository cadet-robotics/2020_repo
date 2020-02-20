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

        // Encoders
        topFlyEncoder = new Encoder(dioSensors.getIntValue("top shooter encoder 1"), dioSensors.getIntValue("top shooter encoder 2"));
        bottomFlyEncoder = new Encoder(dioSensors.getIntValue("bottom shooter encoder 1"), dioSensors.getIntValue("bottom shooter encoder 2"));
        driveEncoderLeft = new Encoder(dioSensors.getIntValue("left drive encoder 1"), dioSensors.getIntValue("left drive encoder 2"));
        driveEncoderRight = new Encoder(dioSensors.getIntValue("right drive encoder 1"), dioSensors.getIntValue("right drive encoder 2"));
    }
}