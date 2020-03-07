package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.RotateWheelCountChangesCommand;
import frc.robot.commands.RotateWheelToColorCommand;
import frc.robot.commands.SetShooterSpeedCommand;
import frc.robot.greeneva.Limelight;
import frc.robot.io.Motors;
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
                           shootButton,
                           intakeButton,
                           toggleLimeButton,
                           winchUnlockButton,
                           winchLockButton;

    // Axes
    private int xAxis,
                yAxis,
                zAxis,
                sliderAxis;
    
    // Winch stuff
    private int winchUpAngle,
                winchDownAngle;
    
    // Misc buttons
    private int manualIntakeIn,
                manualIntakeOut,
                manualMagazineUp,
                manualMagazineDown;
    

    //Private Subsystem instances
    private DriveSubsystem driveSubsystem;
    private ArmSubsystem armSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private WinchSubsystem winchSubsystem;
    private PickupSubsystem pickupSubsystem;

    // Axis getters
    public double getXAxis() { return driverController.getRawAxis(xAxis); }
    public double getYAxis() { return driverController.getRawAxis(yAxis); }
    public double getZAxis() { return driverController.getRawAxis(zAxis); }
    public double getSliderAxis() { return driverController.getRawAxis(sliderAxis); }
    
    /**
     * Loads the configuration, initializing all controls
     * 
     * @param mainConfig The config instance
     * @param driveSubsystemIn The drive subsystem instance
     * @param armSubsystemIn The arm subsystem instance
     * @param shooterSubsystemIn The shooter subsystem instance
     */
    public ControlSubsystem(Config mainConfig, DriveSubsystem driveSubsystemIn, ArmSubsystem armSubsystemIn, ShooterSubsystem shooterSubsystemIn, PickupSubsystem pickupSubsystemIn, WinchSubsystem winchSubsystemIn, Limelight lime) {
        super();
        Config driverControls = mainConfig.separateCategory("driver controls"),
               codriverControls = mainConfig.separateCategory("codriver controls");
        driveSubsystem = driveSubsystemIn;
        armSubsystem = armSubsystemIn;
        shooterSubsystem = shooterSubsystemIn;
        winchSubsystem = winchSubsystemIn;
        pickupSubsystem = pickupSubsystemIn;
        
        driverController = new Joystick(driverControls.getIntValue("controller port"));
        codriverController = new Joystick(codriverControls.getIntValue("controller port"));
        
        // Axes
        xAxis = driverControls.getIntValue("x axis");
        yAxis = driverControls.getIntValue("y axis");
        zAxis = driverControls.getIntValue("z axis");
        sliderAxis = driverControls.getIntValue("slider axis");
        
        // Buttons
        spinButton = new JoystickButton(driverController, driverControls.getIntValue("spin button"));
        intakeButton = new JoystickButton(driverController, driverControls.getIntValue("intake button"));
        shootButton = new JoystickButton(codriverController, codriverControls.getIntValue("shoot button"));
        toggleLimeButton = new JoystickButton(driverController, driverControls.getIntValue("toggle lime"));
        winchUnlockButton = new JoystickButton(codriverController, codriverControls.getIntValue("winch unlock button"));
        winchLockButton = new JoystickButton(codriverController, codriverControls.getIntValue("winch lock button"));
        
        // Winch
        winchUpAngle = codriverControls.getIntValue("winch up");
        winchDownAngle = codriverControls.getIntValue("winch down");
        
        // Other
        manualMagazineUp = driverControls.getIntValue("manual mag up");
        manualMagazineDown = driverControls.getIntValue("manual mag down");
        manualIntakeIn = driverControls.getIntValue("manual intake in");
        manualIntakeOut = driverControls.getIntValue("manual intake out");

        //Spin wheel
        spinButton.whenPressed(() -> {
            Command c = armSubsystem.getCurrentCommand();
            if (c == null) {
                new RotateWheelCountChangesCommand(armSubsystem)
                        .andThen(new WaitCommand(5))
                        .andThen(new RotateWheelToColorCommand(armSubsystem, ColorEnum.YELLOW))
                        .schedule();
            } else {
                c.cancel();
            }
        });

        //Shoot
        shootButton.whenPressed(() -> {
            //System.out.println("WORKING");
            shooterSubsystem.triggerAutoShooter(false);
        });
        
        // Toggle intake
        intakeButton.whenPressed(() -> {
            pickupSubsystem.toggleAutoIntake();
        });

        toggleLimeButton.whenPressed(lime::toggleCamMode);

        winchUnlockButton.whenPressed(() -> winchSubsystem.setLockedState(false));
        winchLockButton.whenPressed(() -> winchSubsystem.setLockedState(true));
    }
    
    /**
     * Run by teleopPeriodic, so that these stay consolidated but this is always during teleop 
     */
    public void periodicTeleop() {
        SmartDashboard.putNumber("LEFT TELE", Motors.leftDrive.get());
        SmartDashboard.putNumber("RIGHT TELE", Motors.rightDrive.get());

        // Run winch
        int pov = codriverController.getPOV();
        
        if(pov == winchUpAngle) {
            winchSubsystem.runUp();
        } else if(pov == winchDownAngle) {
            winchSubsystem.runDown();
        }
        
        // Run intake manually
        if(!pickupSubsystem.getAutoIntakeEnabled()) {
            {
                Command c = pickupSubsystem.getCurrentCommand();
                if (c != null) c.cancel();
            }
            Motors.intake.set(0);
            Motors.magazine.set(0);
            
            if(driverController.getRawButton(manualIntakeIn)) {
                System.out.println("INTAKE IN");
                Motors.intake.set(Constants.INTAKE_SPEED);
            } else if(driverController.getRawButton(manualIntakeOut)) {
                Motors.intake.set(-Constants.INTAKE_SPEED);
            }
            
            if(driverController.getRawButton(manualMagazineUp)) {
                System.out.println("MAGAZINE UP");
                Motors.magazine.set(Constants.MAGAZINE_SPEED);
            } else if(driverController.getRawButton(manualMagazineDown)) {
                Motors.magazine.set(-Constants.MAGAZINE_SPEED);
            }
        }
    }
}  


