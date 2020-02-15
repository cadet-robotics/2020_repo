/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.ControlSubsystem;
import frc.robot.io.Motors;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.vision.LineOverlay;
import frc.robot.vision.ParabolaOverlay;
import frc.robot.vision.TextOverlay;
import frc.robot.vision.VisionThread;
import frc.robot.vision.parabolic.CrosshairsOverlay;
import frc6868.config.api.Config;

import java.io.IOException;
import java.text.DecimalFormat;

import org.opencv.core.Scalar;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command m_autonomousCommand;
    
    private RobotContainer m_robotContainer;

   // public static CvSource cvSource;

    public static VideoCamera cam;

   // public static NetworkTableEntry cnt;
    
    // Crosshairs debug
    ParabolaOverlay parOverlay;
    TextOverlay textOverlay;
    
    // time for c r o s s f i t
    CrosshairsOverlay crosshairs;
    LineOverlay intersectLineA, // These two are debug
                intersectLineB;

    public ControlSubsystem controlSubsystem;
    public DriveSubsystem driveSubsystem;
    public ShooterSubsystem shooterSubsystem;

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
        
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();
        
        // Initialize subsystems
        armSubsystem = new ArmSubsystem();
        driveSubsystem = new DriveSubsystem(new Pose2d(new Translation2d(), new Rotation2d()));
        shooterSubsystem = new ShooterSubsystem();

        controlSubsystem = new ControlSubsystem(mainConfig, driveSubsystem, armSubsystem, shooterSubsystem);
        
        // Initialize the camera itself
        cam = CameraServer.getInstance().startAutomaticCapture();
        cam.setResolution(320, 240);
        cam.setFPS(15);
        
        // Crosshairs vision setup
        textOverlay = new TextOverlay("parabola data", 5, Constants.IMAGE_HEIGHT - 10, new Scalar(0, 255, 0));
        
        // Create the crosshairs object
        crosshairs = new CrosshairsOverlay(Constants.CAMERA_HEIGHT,
                                           Math.toRadians(20),
                                           Constants.LIFECAM_3000_VERTICAL_FOV,
                                           Constants.IMAGE_HEIGHT,
                                           Constants.SHOOTER_HEIGHT,
                                           Constants.TARGET_HEIGHT,
                                           Constants.GRAVITY_ACCEL,
                                           Constants.CROSSHAIR_A_COLOR,
                                           Constants.CROSSHAIR_B_COLOR,
                                           Constants.CROSSHAIR_CENTER_COLOR);
        
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
     */
    private void setupDebugVision(VisionThread vt) {
        // Draw the parabola
        parOverlay = new ParabolaOverlay(-1, 10, -1, 10);
        
        // Draw intersecting lines
        intersectLineA = new LineOverlay(-1, 10, -1, 10, 0, Constants.CAMERA_HEIGHT, 0, Constants.CROSSHAIR_A_COLOR);
        intersectLineB = new LineOverlay(-1, 10, -1, 10, 0, Constants.CAMERA_HEIGHT, 0, Constants.CROSSHAIR_B_COLOR);
        
        // Add processors
        vt.addProcessor(parOverlay);                                                                                                                                            // Parabola
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, 0, Math.toRadians(90)));                                                                                             // y axis
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, 0, 0));                                                                                                              // x axis
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, Constants.TARGET_HEIGHT, 0));                                                                                        // target height
        vt.addProcessor(intersectLineA);                                                                                                                                        // Intersecting lines
        vt.addProcessor(intersectLineB);
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, Constants.CAMERA_HEIGHT, Math.toRadians(20) + (Constants.LIFECAM_3000_VERTICAL_FOV / 2), new Scalar(0, 100, 0)));    // Camera FOV indicators
        vt.addProcessor(new LineOverlay(-1, 10, -1, 10, 0, Constants.CAMERA_HEIGHT, Math.toRadians(20) - (Constants.LIFECAM_3000_VERTICAL_FOV / 2), new Scalar(0, 100, 0)));
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    private ArmSubsystem armSubsystem;

    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
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
     * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
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
    }
    
    boolean leftXUsed = false,
            leftYUsed = false,
            rightXUsed = false,
            rightYUsed = false;
    
    double velocity = 0, angle = Math.toRadians(45);
    
    final double GRAVITY_CONSTANT = -9.81, // m/s^2
                 ANGLE_CONSTANT = Math.toRadians(45);
    
    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        
        // Drive them wheels
    	runManualDrive();
    	
    	// Update the values of the crosshairs system
    	runCrosshairs();
    }
    
    private void runCrosshairs() {
        // We don't have anything giving us data so this is it actually
        if(Constants.CROSSHAIRS_DEBUG) {
            // Run the crosshairs manually
            Joystick js = controlSubsystem.getController();
            double leftJoystickY = -js.getRawAxis(1),
                   rightJoystickY = -js.getRawAxis(5);
            
            if(Math.abs(leftJoystickY) > 0.1) {
                velocity += leftJoystickY / 20;
            }
            
            if(Math.abs(rightJoystickY) > 0.1) {
                angle += rightJoystickY / 100;
            }
            
            // Apply the velocity to the crosshairs
            crosshairs.setAngle(angle);
            crosshairs.setVelocity(velocity);
            crosshairs.calculateLinePositions();
            
            // Show the projected parabola
            parOverlay.setParabola(crosshairs.getQuadraticA(), crosshairs.getQuadraticB(), Constants.SHOOTER_HEIGHT);
            
            // Show the lines its using
            intersectLineA.setAngle(crosshairs.getAngleA());
            intersectLineB.setAngle(crosshairs.getAngleB());
            
        }
        
        // Update info
        DecimalFormat df = new DecimalFormat("##.##");
        textOverlay.setText(String.format("a=%-4s b=%-4s v=%-4s t=%-4s", df.format(crosshairs.getQuadraticA()), df.format(crosshairs.getQuadraticB()), df.format(velocity), df.format(ANGLE_CONSTANT)));
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
    	
    	// Add left/right turning based on the x-axis, opposite per side
    	left -= controlSubsystem.getXAxis() * Constants.DRIVE_X_AXIS_MODIFIER;
    	right += controlSubsystem.getXAxis() * Constants.DRIVE_X_AXIS_MODIFIER;
    	
    	// Apply total speed modifier
    	left *= Constants.DRIVE_SPEED_MODIFIER;
    	right *= Constants.DRIVE_SPEED_MODIFIER;
    	
    	// Set motor values
    	Motors.leftDrive.set(left);
    	Motors.rightDrive.set(right);
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
