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
    public static CANSparkMax wheelSpinner;
    
    public static SpeedControllerGroup leftDrive,
                                       rightDrive,
                                       winch;

    public static CANSparkMax topFly,
                              bottomFly;
    
    public static VictorSP intake,
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
        wheelSpinner = new CANSparkMax(canMotors.getIntValue("wheel spinner"), CANSparkMaxLowLevel.MotorType.kBrushed);
        
        // init motor pairs
        leftDrive = new SpeedControllerGroup(new CANSparkMax(canMotors.getIntValue("left drive a"), CANSparkMaxLowLevel.MotorType.kBrushed), new CANSparkMax(canMotors.getIntValue("left drive b"), CANSparkMaxLowLevel.MotorType.kBrushed));
        rightDrive = new SpeedControllerGroup(new CANSparkMax(canMotors.getIntValue("right drive a"), CANSparkMaxLowLevel.MotorType.kBrushed), new CANSparkMax(canMotors.getIntValue("right drive b"), CANSparkMaxLowLevel.MotorType.kBrushed));
        winch = new SpeedControllerGroup(new PWMVictorSPX(pwmMotors.getIntValue("winch a")), new PWMVictorSPX(pwmMotors.getIntValue("winch b")));
        
        leftDrive.setInverted(false);
        rightDrive.setInverted(true);

        // init flywheels
        topFly = new CANSparkMax(canMotors.getIntValue("top flywheel motor"), CANSparkMaxLowLevel.MotorType.kBrushed);
        bottomFly = new CANSparkMax(canMotors.getIntValue("bottom flywheel motor"), CANSparkMaxLowLevel.MotorType.kBrushed);
        
        // init others
        intake = new VictorSP(pwmMotors.getIntValue("intake"));
        magazine = new VictorSP(pwmMotors.getIntValue("magazine"));
    }
}
