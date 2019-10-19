/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.config.ConfigLoader;
import frc.robot.io.Controls;
import frc.robot.io.Drive;
import frc.robot.io.Motors;
import frc.robot.io.Pneumatics;
import com.google.gson.JsonObject;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.Sensors;

import java.io.IOException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private final SendableChooser<String> m_chooser = new SendableChooser<>();
	
	public static final double DRIVE_MODIFIER = 0.85,					//Multiplier for teleop drive motors
							   DRIVE_THRESHOLD = 0.1;					//Threshold for the teleop controls

	public static final boolean debug = true,
								useCamera = true;

	private JsonObject configJSON;

	private Controls controls;

	private Motors motors;

	private Drive drive;

	private Sensors sensors;
	
	private Pneumatics pneumatics;
	
	boolean throttle = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
		m_chooser.addOption("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		try{
			configJSON = ConfigLoader.loadConfigFile();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		//Initialize configured classes
		controls = new Controls(configJSON);
		motors = new Motors(configJSON);
		sensors = new Sensors(configJSON);
		pneumatics = new Pneumatics(configJSON);
		
		drive = new Drive(motors);
		
		if(useCamera) {
			UsbCamera driverCamera = CameraServer.getInstance().startAutomaticCapture(0);
			driverCamera.setBrightness(35);
		}
		
		IHaveNothingToDo.yeet();
	}

	/**
	 * This function is called every robot packet, no matter the mode. Use
	 * this for items like diagnostics that you want ran during disabled,
	 * autonomous, teleoperated and test.
	 *
	 * <p>This runs after the mode specific periodic functions, but before
	 * LiveWindow and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {
		if(debug) runDebug();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */

	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto)
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		/*
		switch (m_autoSelected) {
		case kCustomAuto:
			// Put custom auto code here
			break;
		case kDefaultAuto:
		default:
			// Put default auto code here
			break;
		}
		*/
		Scheduler.getInstance().run();
		drivePeriodic();
	}

	@Override
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		motors.resetAll();
		Scheduler.getInstance().run();
		drivePeriodic();
	}
	
	/**
	 * Outputs debug
	 */
	public void runDebug() {
	}
	
	/**
	 * Runs the mecanum drive
	 */
	public void drivePeriodic() {
		double xAxis = -controls.getXAxis(),
			   yAxis = controls.getYAxis(),
			   zAxis = -controls.getZAxis();
		
		if(Math.abs(xAxis) < DRIVE_THRESHOLD) xAxis = 0;
		if(Math.abs(yAxis) < DRIVE_THRESHOLD) yAxis = 0;
		if(Math.abs(zAxis) < DRIVE_THRESHOLD) zAxis = 0;
		
		xAxis *= DRIVE_MODIFIER;
		yAxis *= DRIVE_MODIFIER;
		zAxis *= DRIVE_MODIFIER;
		
		if(throttle) {
			double t = mapDouble(controls.getThrottleAxis(), 1, -1, 0, 1);
			xAxis *= t;
			yAxis *= t;
			zAxis *= t;
		}
		
		drive.driveCartesian(xAxis, yAxis, zAxis);
	}
	
	/**
	 * Maps a value from range to range
	 * 
	 * @param val The value to map
	 * @param oldMin The old range's minimum
	 * @param oldMax The old range's maximum
	 * @param newMin The new range's minimum
	 * @param newMax The new range's maximum
	 * @return The new mapped value
	 */
	public double mapDouble(double val, double oldMin, double oldMax, double newMin, double newMax){
	  	return (((val - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
  	}

	public Controls getControls() {
		return controls;
	}

	public Drive getDrive() {
		return drive;
	}

	public Motors getMotors() {
		return motors;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public Pneumatics getPneumatics() {
		return pneumatics;
	}
}