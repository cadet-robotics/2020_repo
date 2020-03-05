package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.RotateWheelCountChangesCommand;
import frc.robot.commands.RotateWheelToColorCommand;
import frc.robot.commands.SetShooterSpeedCommand;
import frc.robot.commands.magazine.IntakeNewBallCommand;
import frc.robot.greeneva.Limelight;
import frc.robot.io.Motors;
import frc.robot.io.OtherIO;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.vision.parabolic.CrosshairsOverlay;
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
                           winchLockButton;

    // Axes
    private int xAxis,
                yAxis,
                zAxis;
    
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
    
    /**
     * Loads the configuration, initializing all controls
     * 
     * @param mainConfig The config instance
     * @param driveSubsystemIn The drive subsystem instance
     * @param armSubsystemIn The arm subsystem instance
     * @param shooterSubsystemIn The shooter subsystem instance
     */
    public ControlSubsystem(Config mainConfig, DriveSubsystem driveSubsystemIn, ArmSubsystem armSubsystemIn, ShooterSubsystem shooterSubsystemIn, PickupSubsystem pickupSubsystemIn, WinchSubsystem winchSubsystemIn, CrosshairsOverlay crosshairsIn, Limelight limelightIn) {
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
        
        // Buttons
        spinButton = new JoystickButton(driverController, driverControls.getIntValue("spin button"));
        //intakeButton = new JoystickButton(driverController, driverControls.getIntValue("intake button"));
        intakeButton = new JoystickButton(codriverController, 8);
        shootButton = new JoystickButton(codriverController, codriverControls.getIntValue("shoot button"));
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
            //new RotateWheelToColorCommand(arm, ColorEnum.BLUE).schedule(false);
        });

        //Shoot
        shootButton.whenPressed(() -> {
            //System.out.println("WORKING");
            new SetShooterSpeedCommand(shooterSubsystem, 2500, 2).schedule();
        });
        
        // Toggle intake
        intakeButton.whenPressed(() -> {
            pickupSubsystem.toggleAutoIntake();
        });
        
        // Toggle winch lock
        winchLockButton.whenPressed(() -> {
            winchSubsystem.toggleLockedState();
        });
        
        
        // MANUAL TO BE FORMALIZED LATER
        // Test vision thing
        new JoystickButton(codriverController, 4).whenPressed(() -> {
            //crosshairsIn.setVelocityDistance(limelightIn.getDistance());
            limelightIn.setCamMode(Limelight.CamMode.Vision);
            manualRPM = false;
            
            //crosshairsIn.runAutoVelocityLimelight(limelightIn, shooterSubsystemIn);
            
        });
        
        new JoystickButton(driverController, 9).whenPressed(() -> {
            System.out.println("Setting to DRIVER");
            limelightIn.setCamMode(Limelight.CamMode.Driver);
        });
        
        new JoystickButton(driverController, 10).whenPressed(() -> {
            System.out.println("Setting to VISION");
            limelightIn.setCamMode(Limelight.CamMode.Vision);
        });
        
        new JoystickButton(driverController, 12).whenPressed(() -> {
            manualRPM = !manualRPM;
            System.out.println(manualRPM);
        });
    }
    
    private double rpm = 2500 / 2;
    boolean manualRPM = false;
    
    /**
     * Run by teleopPeriodic, so that these stay consolidated but this is always during teleop 
     */
    public void periodicTeleop() {
        //Drive Movement
        driveSubsystem.getDriveBase().arcadeDrive(-getYAxis(), getZAxis(), true);

        SmartDashboard.putNumber("LEFT TELE", Motors.leftDrive.get());
        SmartDashboard.putNumber("RIGHT TELE", Motors.rightDrive.get());

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
        if(manualRPM) {
            double r = rpm * (-driverController.getRawAxis(3) + 1);
            //System.out.println(r);
            new SetShooterSpeedCommand(shooterSubsystem, r).schedule();
            Robot.crosshairs.setVelocityRPM(r);
        }
        
        // Run intake manually
        if(!pickupSubsystem.getAutoIntakeEnabled()) {
            //pickupSubsystem.getCurrentCommand().cancel();
            Motors.intake.set(0);
            Motors.magazine.set(0);
            
            if(codriverController.getRawAxis(3) > 0.9) {  //driverController.getRawButton(manualIntakeIn)) {
                System.out.println("INTAKE IN");
                Motors.intake.set(Constants.INTAKE_SPEED);
            } else if(codriverController.getRawAxis(2) > 0.9) {  //driverController.getRawButton(manualIntakeOut)) {
                Motors.intake.set(-Constants.INTAKE_SPEED);
            }
            
            if(codriverController.getRawButton(6)) {  //driverController.getRawButton(manualMagazineUp)) {
                System.out.println("MAGAZINE UP");
                Motors.magazine.set(Constants.MAGAZINE_SPEED);
            } else if(codriverController.getRawButton(5)) {  //driverController.getRawButton(manualMagazineDown)) {
                Motors.magazine.set(-Constants.MAGAZINE_SPEED);
            }
        }
    }
}  


