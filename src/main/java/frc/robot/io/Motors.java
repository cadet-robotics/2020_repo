package frc.robot.io;

import frc6868.config.api.Config;

/**
 * Contains the motor objects
 * 
 * @author Alex Pickering
 */
public class Motors {
    
    /**
     * Loads the configuration, initializing motor objects
     * 
     * @param motorsConfig A Config instance containing only the motors category
     */
    public static void loadConfiguration(Config mainConfig) {
        // We'll need these once we start properly programming
        Config pwmMotors = mainConfig.separateCategory("pwm motors"),
               canMotors = mainConfig.separateCategory("can motors");
    }
}
