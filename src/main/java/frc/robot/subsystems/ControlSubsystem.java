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
    private Joystick driverController,
                     codriverController;
    private JoystickButton spinButton,
                           shootButton;
    
    // Axes
    private int xAxis,
                yAxis,
                zAxis;
    
    // Winch stuff
    private int winchUpAngle,
                winchDownAngle;
    

    //Private Subsystem instances
    private DriveSubsystem driveSubsystem;
    private ArmSubsystem armSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private WinchSubsystem winchSubsystem;

    // Axis getters
    public double getXAxis() { return driverController.getRawAxis(xAxis); }
    public double getYAxis() { return driverController.getRawAxis(yAxis); }
    public double getZAxis() { return driverController.getRawAxis(zAxis); }
    
    
    private double rpm = 6000 / 2;
    
    /**
     * Loads the configuration, initializing all controls
     * 
     * @param mainConfig The config instance
     * @param driveSubsystemIn The drive subsystem instance
     * @param armSubsystemIn The arm subsystem instance
     * @param shooterSubsystemIn The shooter subsystem instance
     */
    public ControlSubsystem(Config mainConfig, DriveSubsystem driveSubsystemIn, ArmSubsystem armSubsystemIn, ShooterSubsystem shooterSubsystemIn, WinchSubsystem winchSubsystemIn) {
        super();
        Config driverControls = mainConfig.separateCategory("driver controls"),
               codriverControls = mainConfig.separateCategory("codriver controls");
        driveSubsystem = driveSubsystemIn;
        armSubsystem = armSubsystemIn;
        shooterSubsystem = shooterSubsystemIn;
        winchSubsystem = winchSubsystemIn;
        
        driverController = new Joystick(driverControls.getIntValue("controller port"));
        codriverController = new Joystick(codriverControls.getIntValue("controller port"));
        
        // Axes
        xAxis = driverControls.getIntValue("x axis");
        yAxis = driverControls.getIntValue("y axis");
        zAxis = driverControls.getIntValue("z axis");
        
        // Buttons
        spinButton = new JoystickButton(driverController, driverControls.getIntValue("spin button"));
        shootButton = new JoystickButton(codriverController, codriverControls.getIntValue("shoot button"));
        
        // Winch
        winchUpAngle = codriverControls.getIntValue("winch up");
        winchDownAngle = codriverControls.getIntValue("winch down");

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
        
        // Run winch
        int pov = codriverController.getPOV();
        if(pov == winchUpAngle) {
            winchSubsystem.runUp();
        } else if(pov == winchDownAngle) {
            winchSubsystem.runDown();
        }
        
        /*
         * TEMPORARY MANUAL CONTROLS
         */
        double r = rpm * (-driverController.getRawAxis(3) + 1);
        System.out.println(r);
        new SetShooterSpeedCommand(shooterSubsystem, r).schedule();
        Robot.crosshairs.setVelocityRPM(r);
        
        Motors.intake.set(0);
        Motors.magazine.set(0);
        
        if(driverController.getRawButton(3)) {
            Motors.intake.set(0.75);
        } else if(driverController.getRawButton(5)) {
            Motors.intake.set(-0.75);
        }
        
        if(driverController.getRawButton(4)) {
            Motors.magazine.set(0.75);
        } else if(driverController.getRawButton(6)) {
            Motors.magazine.set(-0.75);
        }
    }
}  


