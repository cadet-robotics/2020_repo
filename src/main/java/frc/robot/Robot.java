/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutoCommand;
import frc.robot.commands.ManualShooterSpeedCommand;
import frc.robot.commands.RotateToLightCommand;
import frc.robot.commands.TeleDriveCommand;
import frc.robot.greeneva.Limelight;
import frc.robot.io.Motors;
import frc.robot.io.OtherIO;
import frc.robot.io.Sensors;
import frc.robot.subsystems.*;
import frc.robot.vision.LineOverlay;
import frc.robot.vision.ParabolaOverlay;
import frc.robot.vision.TextOverlay;
import frc.robot.vision.VisionThread;
import frc.robot.vision.parabolic.CrosshairsOverlay;
import frc6868.config.api.Config;
import org.opencv.core.Scalar;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

   // public static CvSource cvSource;

    public static VideoCamera cam;

   // public static NetworkTableEntry cnt;
    
    // Crosshairs debug
    ParabolaOverlay parOverlay;
    TextOverlay textOverlay;
    double camHeight,       // These two are used elsewhere
           shooterHeight;
    
    // time for c r o s s f i t
    public static CrosshairsOverlay crosshairs;
    LineOverlay intersectLineA, // These two are debug
                intersectLineB;

    // Subsystems
    public ControlSubsystem controlSubsystem;
    public DriveSubsystem driveSubsystem;
    public ShooterSubsystem shooterSubsystem;
    public ArmSubsystem armSubsystem;
    public WinchSubsystem winchSubsystem;
    public Limelight limelight;
    public PickupSubsystem pickupSubsystem;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Initialize configured things
        Config mainConfig = new Config(Filesystem.getDeployDirectory().getAbsolutePath() + "/config.json");

        try {
            mainConfig.readFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        // Initialize the configurations
        Motors.loadConfiguration(mainConfig);
        Sensors.loadConfig(mainConfig);
        Sensors.driveEncoderLeft.setPosition(0);
        Sensors.driveEncoderRight.setPosition(0);
        OtherIO.loadConfig(mainConfig);
        
        // Initialize subsystems
        armSubsystem = new ArmSubsystem();
        driveSubsystem = new DriveSubsystem(new Pose2d(new Translation2d(), new Rotation2d()));
        pickupSubsystem = new PickupSubsystem();
        shooterSubsystem = new ShooterSubsystem(pickupSubsystem);
        winchSubsystem = new WinchSubsystem();

        limelight = new Limelight(NetworkTableInstance.getDefault());
        limelight.setCamMode(Limelight.CamMode.Vision);

        controlSubsystem = new ControlSubsystem(mainConfig, driveSubsystem, armSubsystem, shooterSubsystem, pickupSubsystem, winchSubsystem, limelight);

        driveSubsystem.setDefaultCommand(new TeleDriveCommand(driveSubsystem, controlSubsystem));
        shooterSubsystem.setDefaultCommand(new ManualShooterSpeedCommand(shooterSubsystem, controlSubsystem));

        // Crosshairs vision setup
        cam = limelight.getLimelightCam();
        CameraServer.getInstance().addCamera(cam);
        setupCrosshairsVision(mainConfig);

        // Initialize the climb camera
        cam = CameraServer.getInstance().startAutomaticCapture();
        cam.setResolution(320, 240);
        cam.setFPS(15);
    }
    
    /**
     * Sets up the stuff for the crosshairs vision system
     * 
     * @param mainConfig
     */
    private void setupCrosshairsVision(Config mainConfig) {
        Config cross = mainConfig.separateCategory("crosshairs");
        textOverlay = new TextOverlay("parabola data", 5, Constants.IMAGE_HEIGHT - 10, new Scalar(0, 255, 0));
        camHeight = Constants.CAMERA_Y;
        shooterHeight = Constants.SHOOTER_Y;
        
        // Create the crosshairs object
        crosshairs = new CrosshairsOverlay(Constants.CAMERA_X,
                                           camHeight,
                                           Constants.CAMERA_ANGLE, 
                                           Constants.CAMERA_FOV,
                                           Constants.IMAGE_HEIGHT,
                                           shooterHeight,
                                           Constants.SHOOTER_WHEELS_DIAMETER,
                                           Constants.TARGET_HEIGHT,
                                           Constants.GRAVITY_ACCEL,
                                           0,
                                           Constants.SHOOTER_ANGLE,
                                           Util.csvToScalar(cross.getValue("color a")), //Constants.CROSSHAIR_A_COLOR,
                                           Util.csvToScalar(cross.getValue("color b")), //Constants.CROSSHAIR_B_COLOR,
                                           Util.csvToScalar(cross.getValue("center color"))); //Constants.CROSSHAIR_CENTER_COLOR);
        
        // Create the thread to process stuff
        VisionThread vt = new VisionThread("uwu feed", 320, 240);
        vt.addProcessor(crosshairs);
        vt.addProcessor(textOverlay);
        
        // Debug stuff if we use it
        if(Constants.CROSSHAIRS_DEBUG) {
            setupDebugVision(vt);
        }
        
        new Thread(vt).start();

    }
    
    /**
     * Sets up vision overlays for crosshair debug
     * 
     * @param vt
     */
    private void setupDebugVision(VisionThread vt) {
        // Draw the parabola
        parOverlay = new ParabolaOverlay(-1, 10, -1, 10);
        
        // Draw intersecting lines
        intersectLineA = new LineOverlay(-1, 10, -1, 10, Constants.CAMERA_X, camHeight, 0, crosshairs.getColorA());
        intersectLineB = new LineOverlay(-1, 10, -1, 10, Constants.CAMERA_X, camHeight, 0, crosshairs.getColorB());
        
        // Add processors
        vt.addProcessor(parOverlay);                                                                                                                                            // Parabola
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, 0, Math.toRadians(90)));                                                                                             // y axis
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, 0, 0));                                                                                                              // x axis
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, Constants.TARGET_HEIGHT, 0));                                                                                        // target height
        vt.addProcessor(intersectLineA);                                                                                                                                        // Intersecting lines
        vt.addProcessor(intersectLineB);
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, camHeight, Constants.CAMERA_ANGLE + (Constants.LIFECAM_3000_VERTICAL_FOV / 2), new Scalar(0, 100, 0)));    // Camera FOV indicators
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, camHeight, Constants.CAMERA_ANGLE - (Constants.LIFECAM_3000_VERTICAL_FOV / 2), new Scalar(0, 100, 0)));
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */

    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
        //System.out.println("SPEED TOP: " + Sensors.bottomFlyEncoder.getVelocity());
        //System.out.println("SPEED BOTTOM: " + Sensors.bottomFlyEncoder.getVelocity());
        /*
        if (((System.currentTimeMillis() / 1000) % 2) == 0) {
            limelight.setCamMode(Limelight.CamMode.Driver);
        } else {
            limelight.setCamMode(Limelight.CamMode.Vision);
        }
        */

        //Debug and Smartdashboard
        SmartDashboard.putBoolean("Auto Ball Intake", pickupSubsystem.getAutoIntakeEnabled());

        SmartDashboard.putNumber("Gyro", Sensors.getGyro());
        SmartDashboard.putNumber("Enc Left P", Sensors.driveEncoderLeft.getPosition());
        SmartDashboard.putNumber("Enc Right P", Sensors.driveEncoderRight.getPosition());
        SmartDashboard.putNumber("Enc Left V", Sensors.driveEncoderLeft.getVelocity());
        SmartDashboard.putNumber("Enc Right V", Sensors.driveEncoderRight.getVelocity());
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }
    
    /*
     * Autonomous Group Methods
     */

    /**
     * This autonomous runs the autonomous command
     */
    @Override
    public void autonomousInit() {
        //m_autonomousCommand = new SkitterCommand(driveSubsystem);
        m_autonomousCommand = new AutoCommand(limelight, driveSubsystem, shooterSubsystem, pickupSubsystem);
        m_autonomousCommand.schedule();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }
    
    /*
     * Teleop Group Methods
     */

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
        Sensors.driveEncoderLeft.setPosition(0);
        Sensors.driveEncoderRight.setPosition(0);
    }
    
    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        
        // Drive them wheels
    	//runManualDrive();
    	
    	// Update the values of the crosshairs system
    	runCrosshairs();
        
        // Run controls subsystem periodic
        controlSubsystem.periodicTeleop();
        //driveSubsystem.getDriveBase().tankDrive(0.3, 0.3, false);
    }
    
    double debugVelocity = 0,
           debugAngle = 0;
    
    private void runCrosshairs() {
        // Get known information
        // Apply the velocity to the crosshairs
        //crosshairs.setAngle(Constants.SHOOTER_ANGLE);
        //crosshairs.setVelocity(debugVelocity);
        crosshairs.calculateLinePositions();
        
        // We don't have anything giving us data so this is it actually
        // Once we have information from Owen's system there will be proper stuff here            
        if(Constants.CROSSHAIRS_DEBUG) {
            // Apply the velocity to the crosshairs
            
            // Show the projected parabola
            parOverlay.setParabola(crosshairs.getQuadraticA(), crosshairs.getQuadraticB(), shooterHeight);
            
            // Show the lines its using
            intersectLineA.setAngle(crosshairs.getAngleA());
            intersectLineB.setAngle(crosshairs.getAngleB());
            
        }
        
        // Update info
        DecimalFormat df = new DecimalFormat("##.##");
        textOverlay.setText(String.format("a=%-4s b=%-4s v=%-4s t=%-4s", df.format(crosshairs.getQuadraticA()), df.format(crosshairs.getQuadraticB()), df.format(debugVelocity), df.format(debugAngle)));
    }
    
    /**
     * Runs the drive train manually
     */
    private void runManualDrive() {
    	// Start with no movement
    	double left = 0, right = 0;
    	
    	// Add forwards/backwards movement based on the y-axis
    	left += controlSubsystem.getYAxis() * Constants.DRIVE_Y_AXIS_MODIFIER;
    	right += controlSubsystem.getYAxis() * Constants.DRIVE_Y_AXIS_MODIFIER;
    	
    	// Add left/right turning based on the x-axis
    	left += controlSubsystem.getXAxis() * Constants.DRIVE_X_AXIS_MODIFIER;
    	right += controlSubsystem.getXAxis() * Constants.DRIVE_X_AXIS_MODIFIER;
    	
    	// Apply total speed modifier
    	left *= Constants.DRIVE_SPEED_MODIFIER;
    	right *= Constants.DRIVE_SPEED_MODIFIER;
    	
    	// Set motor values
    	driveSubsystem.getDriveBase().tankDrive(left, right, false);
    }
    
    
    /*
     * Test Group Methods
     */
    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
