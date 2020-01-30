/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorSensorV3;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.ColorShim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.colordebug.ColorReporter;
import frc.robot.io.Controls;
import frc.robot.io.Motors;
import frc.robot.subsystems.ArmSubsystem;
import frc6868.config.api.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

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
    
    // test points
    public static Point lineAPointA = new Point(0, 50),
                        lineAPointB = new Point(320, 50),
                        lineBPointA = new Point(0, 150),
                        lineBPointB = new Point(320, 150);
    
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

        //System.out.println(mainConfig);
        
        /*
        // Echo config file so in case it's breaking
        // try with resources to autoclose
        try (BufferedReader r = new BufferedReader(new FileReader(mainConfig.getFileLocation()))) {
            r.lines().forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        
        Controls.loadConfiguration(mainConfig);
        Motors.loadConfiguration(mainConfig);
        
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();

        arm = new ArmSubsystem();

        Controls.setupCommands(arm);

        //cam = CameraServer.getInstance().startAutomaticCapture();
        
        //cvSource = CameraServer.getInstance().putVideo("cv_debug", 320, 240);
        //cvSource.setPixelFormat(VideoMode.PixelFormat.kBGR);

        //cnt = NetworkTableInstance.getDefault().getEntry("CNT");
        
        // Let's try some vision
        // Goal: Add a line to the screen, preferably one we control
        new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(320, 240);
            camera.setFPS(15);
            
            // setup streams
            CvSink sink = CameraServer.getInstance().getVideo();
            CvSource output = CameraServer.getInstance().putVideo("uwu", 320, 240);
            
            System.out.println("starting vision thread owo");
            
            Mat sourceMat = new Mat(),
                destMat = new Mat();
            
            // process the things
            while(!Thread.interrupted()) {
                long val = sink.grabFrame(sourceMat);
                if(val == 0) continue;
                
                //Imgproc.cvtColor(sourceMat, destMat, Imgproc.COLOR_BGR2GRAY);
                
                // do the things
                Imgproc.rectangle(sourceMat,                  // frame
                                  new Point(10, 10),    // first corner
                                  new Point(20, 20),    // second corner
                                  new Scalar(0, 0, 0)); // color in BGR?
                Imgproc.line(sourceMat, lineAPointA, lineAPointB, new Scalar(0, 255, 0));
                Imgproc.line(sourceMat, lineBPointA, lineBPointB, new Scalar(255, 0, 0));
                
                // uuuuuuhhhh copy mat
                sourceMat.copyTo(destMat);
                
                output.putFrame(destMat);
            }
        }).start();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    private ArmSubsystem arm;

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

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    	
        // vision line test stuff
        Joystick js = Controls.getController();
        double leftX = js.getRawAxis(0),
               leftY = js.getRawAxis(1),
               rightX = js.getRawAxis(4),
               rightY = js.getRawAxis(5);
        
        double left = js.getRawAxis(1),
               right = js.getRawAxis(5);
        
        lineAPointA.x += leftX * 5;
        lineAPointA.y += leftY * 5;
        
        lineAPointB.x += rightX * 5;
        lineAPointB.y += rightY * 5;
        
    	runManualDrive();
    }
    
    /**
     * Runs the drive train manually
     */
    public void runManualDrive() {
    	// Start with no movement
    	double left = 0, right = 0;
    	
    	// Add forwards/backwards movement based on the y-axis
    	left += Controls.getYAxis() * Constants.DRIVE_Y_AXIS_MODIFIER;
    	right += Controls.getYAxis() * Constants.DRIVE_Y_AXIS_MODIFIER;
    	
    	// Add left/right turning based on the x-axis, opposite per side
    	left -= Controls.getXAxis() * Constants.DRIVE_X_AXIS_MODIFIER;
    	right += Controls.getXAxis() * Constants.DRIVE_X_AXIS_MODIFIER;
    	
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
