package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Robot;
import frc.robot.commands.RotateWheelCountChangesCommand;
import frc.robot.commands.RotateWheelToColorCommand;
import frc.robot.commands.SetShooterSpeedCommand;
import frc.robot.io.Motors;
import frc.robot.io.OtherIO;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.wheel.ColorEnum;
import frc6868.config.api.Config;

/**
 * Contains the controls objects and methods to get them
 *
 * @author Alex Pickering
 * @author Matt Robinson
 */
public class ControlSubsystem extends SubsystemBase {

    //Controls
    private Joystick controller;
    private JoystickButton spinButton;
    private JoystickButton shootButton;
    
    // Axes
    private int xAxis,
                yAxis,
                zAxis;
    

    //Private Subsystem instances
    private DriveSubsystem driveSubsystem;
    private ArmSubsystem armSubsystem;
    private ShooterSubsystem shooterSubsystem;

    // Axis getters
    public double getXAxis() { return controller.getRawAxis(xAxis); }
    public double getYAxis() { return controller.getRawAxis(yAxis); }
    public double getZAxis() { return controller.getRawAxis(zAxis); }
    
    // Raw getters
    public Joystick getController() { return controller; }
    
    private double rpm = 6000 / 2;
    
    /**
     * Loads the configuration, initializing all controls
     * 
     * @param mainConfig The config instance
     * @param driveSubsystemIn The drive subsystem instance
     * @param armSubsystemIn The arm subsystem instance
     * @param shooterSubsystemIn The shooter subsystem instance
     */
    public ControlSubsystem(Config mainConfig, DriveSubsystem driveSubsystemIn, ArmSubsystem armSubsystemIn, ShooterSubsystem shooterSubsystemIn) {
        super();
        Config controls = mainConfig.separateCategory("controls");
        driveSubsystem = driveSubsystemIn;
        armSubsystem = armSubsystemIn;
        shooterSubsystem = shooterSubsystemIn;
        
        controller = new Joystick(controls.getIntValue("controller port"));
        
        // Axes
        xAxis = controls.getIntValue("x axis");
        yAxis = controls.getIntValue("y axis");
        zAxis = controls.getIntValue("z axis");
        
        // Buttons
        spinButton = new JoystickButton(controller, controls.getIntValue("spin button"));
        shootButton = new JoystickButton(controller, controls.getIntValue("shoot button"));

        //Spin wheel
        spinButton.whenPressed(() -> {
            //new RotateWheelToColorCommand(arm, ColorEnum.BLUE).schedule(false);
        });

        //Shoot
        shootButton.whenPressed(() -> {
            System.out.println("WORKING");
            new SetShooterSpeedCommand(shooterSubsystem, 2500, 2).schedule();
        });
    }
    
    /**
     * Run by teleopPeriodic, so that these stay consolidated but this is always during teleop 
     */
    public void periodicTeleop() {
        //Drive Movement
        driveSubsystem.getDriveBase().arcadeDrive(-getZAxis(), getYAxis(), true);
        
        double r = rpm * (-controller.getRawAxis(3) + 1);
        System.out.println(r);
        new SetShooterSpeedCommand(shooterSubsystem, r).schedule();
        Robot.crosshairs.setVelocityRPM(r);
        
        /*
         * TEMPORARY MANUAL CONTROLS
         */
        Motors.intake.set(0);
        Motors.magazine.set(0);
        
        if(controller.getRawButton(3)) {
            Motors.intake.set(0.75);
        } else if(controller.getRawButton(5)) {
            Motors.intake.set(-0.75);
        }
        
        if(controller.getRawButton(4)) {
            Motors.magazine.set(0.75);
        } else if(controller.getRawButton(6)) {
            Motors.magazine.set(-0.75);
        }
        
        // run winch uwu
        Motors.winch.set(0);
        if(controller.getRawButton(11)) {
            Motors.winch.set(0.75);
        } else if(controller.getRawButton(12)) {
            Motors.winch.set(-0.75);
        }
        
        if(controller.getRawButton(10)) {
            OtherIO.lockingPiston.set(DoubleSolenoid.Value.kForward);
        } else if(controller.getRawButton(9)) {
            OtherIO.lockingPiston.set(DoubleSolenoid.Value.kOff);
        } else if(controller.getRawButton(8)) {
            OtherIO.lockingPiston.set(DoubleSolenoid.Value.kReverse);
        }
    }
}  


