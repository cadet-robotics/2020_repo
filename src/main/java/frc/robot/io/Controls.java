package frc.robot.io;

import edu.wpi.first.wpilibj.Joystick;
import frc6868.config.api.Config;

/**
 * Contains the controls objects and methods to get them
 * <p> Items will static so we don't need to pass around an instance
 * 
 * @author Alex Pickering
 */
public class Controls {
    
    private static Joystick controller;
    
    // Example variable
    private static int xAxis;
    
    // Example getter
    public static double getXAxis() {
        return controller.getRawAxis(xAxis);
    }
    
    /**
     * Loads the configuration, initializing all controls
     * 
     * @param controlsConfig A Config instance holding only the "controls" category
     */
    public static void loadConfiguration(Config mainConfig) {
        Config controls = mainConfig.separateCategory("controls");
        
        controller = new Joystick(controls.getIntValue("controller port"));
        
        // Example initialization
        xAxis = controls.getIntValue("x axis");
    }
}
