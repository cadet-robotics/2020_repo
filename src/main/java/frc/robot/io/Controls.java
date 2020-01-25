package frc.robot.io;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.RotateWheelCountChanges;
import frc.robot.subsystems.ArmSubsystem;
import frc6868.config.api.Config;

/**
 * Contains the controls objects and methods to get them
 * <p> Items will static so we don't need to pass around an instance
 * 
 * @author Alex Pickering
 */
public class Controls {
    
    private static Joystick controller;
    private static JoystickButton spinButton;
    
    // Example variable
    private static int xAxis;
    
    // Example getter
    public static double getXAxis() {
        return controller.getRawAxis(xAxis);
    }
    
    /**
     * Loads the configuration, initializing all controls
     * 
     * @param mainConfig The config instance
     */
    public static void loadConfiguration(Config mainConfig) {
        Config controls = mainConfig.separateCategory("controls");
        
        controller = new Joystick(controls.getIntValue("controller port"));
        
        // Example initialization
        xAxis = controls.getIntValue("x axis");

        spinButton = new JoystickButton(controller, 1);
    }

    public static void setupCommands(ArmSubsystem arm) {
        spinButton.whenPressed(() -> {
            System.out.println("RUNNING IN THE ||{}");
            new RotateWheelCountChanges(arm).schedule(false);
        });
    }
}
