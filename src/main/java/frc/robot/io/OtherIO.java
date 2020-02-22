package frc.robot.io;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc6868.config.api.Config;

/**
 * Other IO things, created for pneumatics
 * 
 * @author Alex Pickering
 */
public class OtherIO {
    
    public static DoubleSolenoid lockingPiston;
    
    public static void loadConfig(Config c) {
        Config other = c.separateCategory("other");
        
        // Double solenoid
        // TODO: WIRES
        lockingPiston = new DoubleSolenoid(other.getIntValue("piston solenoid"), other.getIntValue("unused solenoid"));
    }
}
