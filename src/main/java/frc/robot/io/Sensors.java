package frc.robot.io;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.EncoderType;
import edu.wpi.first.wpilibj.AnalogInput;
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
    public static AnalogInput intakeSensor; //Ball at intake entry sensor. TODO: Sensor on Analog Pin 6
    public static DigitalInput magSensor; //Ball at mag entry sensor

    public static void loadConfig(Config c) {
        // TODO: Change config to match with names of sensors
        // *jojo's noises*
        Config sensors = c.separateCategory("sensors");
        
        // Magazine prox sensors
        magSensor = new DigitalInput(sensors.getIntValue("magazine sensor"));
        intakeSensor = new AnalogInput(sensors.getIntValue("intake sensor"));

        // Encoders
        topFlyEncoder = new CANEncoder(Motors.topFly, EncoderType.kHallSensor, 0);
        topFlyEncoder.setPositionConversionFactor(1 / 3.0);
        topFlyEncoder.setVelocityConversionFactor(1 / 3.0);

        bottomFlyEncoder = new CANEncoder(Motors.bottomFly, EncoderType.kHallSensor, 0);
        bottomFlyEncoder.setPositionConversionFactor(1 / 3.0);
        bottomFlyEncoder.setVelocityConversionFactor(1 / 3.0);

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