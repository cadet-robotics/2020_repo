package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.FDriverFactory;
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
                           winchLockButton,
                           cycleBallCountButton,
                           magicShootButton;

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
        intakeButton = new JoystickButton(codriverController, codriverControls.getIntValue("intake button"));
        shootButton = new JoystickButton(codriverController, codriverControls.getIntValue("shoot button"));
        toggleLimeButton = new JoystickButton(driverController, driverControls.getIntValue("toggle lime"));
        winchUnlockButton = new JoystickButton(codriverController, codriverControls.getIntValue("winch unlock button"));
        winchLockButton = new JoystickButton(codriverController, codriverControls.getIntValue("winch lock button"));
        cycleBallCountButton = new JoystickButton(codriverController, codriverControls.getIntValue("cycle ball count"));

        magicShootButton = new JoystickButton(driverController, driverControls.getIntValue("magic shoot"));
        
        // Winch
        winchUpAngle = codriverControls.getIntValue("winch up");
        winchDownAngle = codriverControls.getIntValue("winch down");

        // Other
        manualMagazineUp = codriverControls.getIntValue("manual mag up");
        manualMagazineDown = codriverControls.getIntValue("manual mag down");
        manualIntakeIn = codriverControls.getIntValue("manual intake in");
        manualIntakeOut = codriverControls.getIntValue("manual intake out");

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
            System.out.println("SETTING SPEED");
            manualRPM = false;
            //Robot.crosshairs.setRPMFromTable(lime.getDistance());
            //shooterSubsystem.triggerAutoShooter(false);
            Robot.crosshairs.setVelocityDistance(lime.getDistance());
            new SetShooterSpeedCommand(shooterSubsystem, Robot.crosshairs.getRPM()).schedule();
        });
        
        new JoystickButton(driverController, 10).whenPressed(() -> {
            manualRPM = true;
        });
        
        // Toggle intake
        intakeButton.whenPressed(() -> {
            pickupSubsystem.toggleAutoIntake();
        });

        cycleBallCountButton.whenPressed(() -> {
           pickupSubsystem.cycleBallCount();
        });

        toggleLimeButton.whenPressed(lime::toggleCamMode);

        winchUnlockButton.whenPressed(() -> winchSubsystem.setLockedState(false));
        winchLockButton.whenPressed(() -> winchSubsystem.setLockedState(true));

        magicShootButton.whenPressed(() -> {
            FDriverFactory.produce(shooterSubsystem, pickupSubsystem, lime::getDistance, true).schedule();
        });
    }
    
    private boolean manualRPM = true;
    int maxRPM = 3000;
    
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
        
        // Sets the RPM manually. This is final, and will not be made a command due to pain, suffering, and hours wasted.
        if(manualRPM) {
            double rpm = (-driverController.getRawAxis(sliderAxis) + 1) * (maxRPM / 2);
            new SetShooterSpeedCommand(shooterSubsystem, rpm).schedule();
            Robot.crosshairs.setVelocityRPM(rpm);
        }
        
        // Run intake manually
        if(!pickupSubsystem.getAutoIntakeEnabled()) {
            {
                Command c = pickupSubsystem.getCurrentCommand();
                if (c != null) c.cancel();
            }
            Motors.intake.set(0);
            Motors.magazine.set(0);
            
            if(codriverController.getRawButton(manualIntakeIn)) {
                System.out.println("INTAKE IN");
                Motors.intake.set(Constants.INTAKE_SPEED);
            } else if(codriverController.getRawButton(manualIntakeOut)) {
                Motors.intake.set(-Constants.INTAKE_SPEED);
            }
            
            if(codriverController.getRawButton(manualMagazineUp)) {
                System.out.println("MAGAZINE UP");
                Motors.magazine.set(Constants.MAGAZINE_SPEED);
            } else if(codriverController.getRawButton(manualMagazineDown)) {
                Motors.magazine.set(-Constants.MAGAZINE_SPEED);
            }
        }
    }
}  


