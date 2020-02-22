package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import frc6868.config.api.Config;

/**
 * Contains the motor objects
 * 
 * @author Alex Pickering
 */
public class Motors {
    // TODO: Configure encoders, setDistancePerPulse
    
    // Spark MAXes
    public static CANSparkMax wheelSpinner,
                              leftDriveA,
                              leftDriveB,
                              rightDriveA,
                              rightDriveB;
    
    public static MotorPair leftDrive,
                            rightDrive,
                            winch;

    public static CANSparkMax topFly,
                              bottomFly;
    
    public static PWMVictorSPX intake,
                               magazine;
    
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
        //wheelSpinner = new CANSparkMax(canMotors.getIntValue("wheel spinner"), CANSparkMaxLowLevel.MotorType.kBrushed);
        
        // init motor pairs
        leftDriveA = new CANSparkMax(canMotors.getIntValue("left drive a"), CANSparkMaxLowLevel.MotorType.kBrushed);
        leftDriveB = new CANSparkMax(canMotors.getIntValue("left drive b"), CANSparkMaxLowLevel.MotorType.kBrushed);
        leftDrive = new MotorPair(leftDriveA, leftDriveB);

        rightDriveA = new CANSparkMax(canMotors.getIntValue("right drive a"), CANSparkMaxLowLevel.MotorType.kBrushed);
        rightDriveB = new CANSparkMax(canMotors.getIntValue("right drive b"), CANSparkMaxLowLevel.MotorType.kBrushed);
        rightDrive = new MotorPair(rightDriveA, rightDriveB);

        winch = new MotorPair(new PWMVictorSPX(pwmMotors.getIntValue("winch a")), new PWMVictorSPX(pwmMotors.getIntValue("winch b")));
        
        leftDrive.setInverted(false);
        rightDrive.setInverted(true);

        // init flywheels
        topFly = new CANSparkMax(canMotors.getIntValue("top flywheel motor"), CANSparkMaxLowLevel.MotorType.kBrushless);
        topFly.setInverted(false);
        
        bottomFly = new CANSparkMax(canMotors.getIntValue("bottom flywheel motor"), CANSparkMaxLowLevel.MotorType.kBrushless);
        bottomFly.setInverted(true);
        
        // init others
        intake = new PWMVictorSPX(pwmMotors.getIntValue("intake"));
        magazine = new PWMVictorSPX(pwmMotors.getIntValue("magazine"));
    }
}
