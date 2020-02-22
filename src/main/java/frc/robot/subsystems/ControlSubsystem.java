package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.RotateWheelCountChangesCommand;
import frc.robot.commands.RotateWheelToColorCommand;
import frc.robot.commands.SetShooterSpeedCommand;
import frc.robot.io.Motors;
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
                zAxis,
                intakeButton,
                magButton;

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
        
        intakeButton = controls.getIntValue("intake");
        magButton = controls.getIntValue("magazine");

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
    
    /*
     * RUN PERIODIC CONTROLS HERE
     */
    @Override
    public void periodic() {
        //Drive Movement
        if (DriverStation.getInstance().isOperatorControl()) {
            //System.out.println("running drive");
            driveSubsystem.getDriveBase().arcadeDrive(getZAxis(), getYAxis(), true);
        }
        
        // TEMPORARY MAG CONTROLS
        Motors.intake.set(0);
        Motors.magazine.set(0);
        
        if(controller.getRawButton(intakeButton)) {
            Motors.intake.set(0.5);
        }
        
        if(controller.getRawButton(magButton)) {
            Motors.magazine.set(0.5);
        }
    }
}
