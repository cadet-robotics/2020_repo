package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc6868.config.api.Config;

/**
 * Contains the motor objects
 * 
 * @author Alex Pickering
 */
public class Motors {
    
    // Spark MAXes
    public static CANSparkMax wheelSpinner,
                              leftDrive,
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
        
        // init SparkMAX
        wheelSpinner = new CANSparkMax(canMotors.getIntValue("wheel spinner"), CANSparkMaxLowLevel.MotorType.kBrushed);
        leftDrive = new CANSparkMax(canMotors.getIntValue("left drive"), CANSparkMaxLowLevel.MotorType.kBrushed);
        rightDrive = new CANSparkMax(canMotors.getIntValue("right drive"), CANSparkMaxLowLevel.MotorType.kBrushed);
    }
}
