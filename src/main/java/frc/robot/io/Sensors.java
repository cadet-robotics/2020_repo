package frc.robot.io;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.EncoderType;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc6868.config.api.Config;

public class Sensors {
    public static CANEncoder topFlyEncoder,
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
        topFlyEncoder = new CANEncoder(Motors.topFly, EncoderType.kQuadrature, 1024);
        bottomFlyEncoder = new CANEncoder(Motors.bottomFly, EncoderType.kHallSensor, 0);
        driveEncoderLeft = new CANEncoder(Motors.leftDriveA, EncoderType.kQuadrature, 1024);
        driveEncoderRight = new CANEncoder(Motors.rightDriveB, EncoderType.kQuadrature, 1024);

        /*
        // Sets up drive encoders to report rate as m/s for each side of the robot
        // TODO: Check gear ratio between encoder and wheels
        final double DIST_DRIVE = 6.0 * .0254 * Math.PI / 1024.0;
        driveEncoderLeft.setDistancePerPulse(DIST_DRIVE);
        driveEncoderRight.setDistancePerPulse(DIST_DRIVE);
         */

        gyro = new AHRS(SerialPort.Port.kMXP);
    }
}