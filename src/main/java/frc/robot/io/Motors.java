package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import frc.robot.io.motortype.MotorPair;
import frc6868.config.api.Config;

/**
 * Contains the motor objects
 * 
 * @author Alex Pickering
 */
public class Motors {
    // TODO: Configure encoders, setDistancePerPulse
    
    // Spark MAXes
    public static CANSparkMax wheelSpinner;
    
    public static MotorPair<CANSparkMax> leftDrive,
                                         rightDrive;
    
    /**
     * Loads the configuration, initializing motor objects
     * 
     * @param mainConfig The config instance
     */
    public static void loadConfiguration(Config mainConfig) {
        // We'll need these once we start properly programming
        Config pwmMotors = mainConfig.separateCategory("pwm motors"),
               canMotors = mainConfig.separateCategory("can motors");

        // System.out.println(mainConfig);
        
        // init SparkMAXes
        wheelSpinner = new CANSparkMax(canMotors.getIntValue("wheel spinner"), CANSparkMaxLowLevel.MotorType.kBrushed);
        
        // init motor pairs
        leftDrive = new MotorPair<>(new CANSparkMax(canMotors.getIntValue("left drive a"), CANSparkMaxLowLevel.MotorType.kBrushed), new CANSparkMax(canMotors.getIntValue("left drive b"), CANSparkMaxLowLevel.MotorType.kBrushed));
        rightDrive = new MotorPair<>(new CANSparkMax(canMotors.getIntValue("right drive a"), CANSparkMaxLowLevel.MotorType.kBrushed), new CANSparkMax(canMotors.getIntValue("right drive b"), CANSparkMaxLowLevel.MotorType.kBrushed));
        
        leftDrive.setInverted(false);
        rightDrive.setInverted(true);
    }
}
