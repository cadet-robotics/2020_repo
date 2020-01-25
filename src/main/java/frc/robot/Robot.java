/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorSensorV3;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Filesystem;
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

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command m_autonomousCommand;
    
    private RobotContainer m_robotContainer;

    public static CvSource cvSource;

    public static VideoCamera cam;
    
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
        try {
            BufferedReader r = new BufferedReader(new FileReader(mainConfig.getFileLocation()));
            while (true) {
                String s = r.readLine();
                if (s == null) {
                    break;
                }
                System.out.println("#>>> " + s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Controls.loadConfiguration(mainConfig);
        Motors.loadConfiguration(mainConfig);
        
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();

        arm = new ArmSubsystem();

        //Controls.setupCommands(arm);

        cam = CameraServer.getInstance().startAutomaticCapture();

        cvSource = CameraServer.getInstance().putVideo("cv_debug", 320, 240);
        //cvSource.setPixelFormat(VideoMode.PixelFormat.kBGR);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    private ArmSubsystem arm;
    private int cnt = 0;

    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
        if (cnt >= 0) {
            cnt = 0;
        } else {
            cnt += 1;
        }
        //System.out.println("CNT: " + cnt);
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
    }
    
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
