/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.google.gson.JsonObject;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.GetBallCommand;
import frc.robot.config.ConfigLoader;
import frc.robot.io.*;
import frc.robot.sensors.Sensors;
import frc.robot.sensors.SightData;

import java.io.IOException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot implements Nexus {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private final SendableChooser<String> m_chooser = new SendableChooser<>();
	
	public static final double DRIVE_MODIFIER = 0.85,					//Multiplier for teleop drive motors
							   DRIVE_THRESHOLD = 0.1,					//Threshold for the teleop controls
							   ELEVATOR_MANUAL_SPEED = 0.5,				//Manual control speed for the elevator
							   ELEVATOR_MAINTENANCE_SPEED = 0.25,		//Speed to keep the elevator in place
							   CLAW_WHEEL_SPEED = 0.7,					//Speed of the claw's wheels
							   BALL_DISTANCE = 5;						//Maximum distance to say there's a ball
	
	public static final int BALL_EJECT_TIME = 10;	//Number of ticks to eject balls for
	
	public static final Value CLAW_OPEN = Value.kForward,
							  CLAW_CLOSED = Value.kReverse;

	public static final boolean GET_BALL_DIRECTION = false,
								debug = true,
								useCamera = true;

	public JsonObject configJSON;
	
	public UsbCamera driverCamera;

	public Controls controls;

	public Motors motors;

	public Drive drive;

	public Sensors sensors;
	
	public Pneumatics pneumatics;
	
	public SightData sightData;

	public Elevator elevator;
	
	GetBallCommand getBallCommand;
	
	boolean throttle = false,
			elevatorThrottle = false,
			clawOpen = true,
			elevatorRunning = false,
			clawRunning = false,
			ejectingBall = false;
	
	//New Press booleans
	boolean newElevatorPress = true,
			newClawTogglePress = true,
			newGetBallPress = true,
			newEjectBallPress = true;
	
	int ballEjectTimer = BALL_EJECT_TIME;

	//public UpdateLineManager lineManager = null;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
		m_chooser.addOption("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		sightData = new SightData(NetworkTableInstance.getDefault());
		
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
		
		drive = new Drive(this);
		elevator = new Elevator(this);
		
		if(useCamera) {
			driverCamera = CameraServer.getInstance().startAutomaticCapture(0);
			driverCamera.setBrightness(35);
		}
		/*
		drive =
		UpdateLineManager m = new UpdateLineManager(NetworkTableInstance.getDefault(), seeInstance);
		*/
		
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

	private AutoLock autoCommand;
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		if (autoCommand != null) autoCommand.cancel();
		(autoCommand = new AutoLock(this)).start();
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
		runElevator();
		runClaw();
	}
	
	/**
	 * Outputs debug
	 */
	public void runDebug() {
		/*String s = "Proximity ";
		
		for(int i = 0; i < sensors.elevatorSensors.length; i++) {
			s += i + ": " + sensors.elevatorSensors[i].detected() + " ";
		}
		
		System.out.println(s);*/
		//System.out.println("X: " + controls.getXAxis() + " Y: " + controls.getYAxis() + " Z: " + controls.getZAxis());
		//System.out.println(clawOpen);
		//System.out.println(controls.getThrottleAxis());
		//System.out.println(motors.leftElevator.get() + " " + motors.rightElevator.get());
		
		/*String flv = Double.toString(motors.frontLeftDrive.get()).substring(0, 3),
			   frv = Double.toString(motors.frontRightDrive.get()).substring(0, 3),
			   blv = Double.toString(motors.backLeftDrive.get()).substring(0, 3),
			   brv = Double.toString(motors.backRightDrive.get()).substring(0, 3);
		System.out.println(flv + "\t" + frv + "\n" + blv + "\t" + brv + "\n");*/
		System.out.println("BALL DISTANCE: " + sensors.ballDistance.getValue());
		//System.out.println(motors.leftClaw.get() + " " + motors.rightClaw.get());
		
		SmartDashboard.putBoolean("Bottom Limit", sensors.bottomLimitSwitch.get());
	}
	
	/**
	 * Runs the claw (opening, closing, wheels)
	 */
	public void runClaw() {	
		//Get the ball
		if(controls.getGetBall()) {
			if(newGetBallPress) {
				System.out.println("STARTING NEW GET BALL COMMAND");
				newGetBallPress = false;
				
				if(getBallCommand != null)
					getBallCommand.cancel();
				
				//New edition of the command
				getBallCommand = new GetBallCommand(this);
				Scheduler.getInstance().add(getBallCommand);
			}
		} else if(getBallCommand != null) {
			System.out.println("STOPPING GET BALL COMMAND");
			getBallCommand.cancel();
			getBallCommand = null;
		}
		
		//Allow another press
		if(!controls.getGetBall() && !newGetBallPress) {
			newGetBallPress= true;
		}
		
		//Eject the ball, get the hatch panel
		if(controls.getEjectBall()) {
			//If it has a ball, eject it
			if(sensors.ballDistance.getAverageValue() < BALL_DISTANCE) {
				if(newEjectBallPress) {
					ejectingBall = true;
					ballEjectTimer = BALL_EJECT_TIME;
					newEjectBallPress = false;
				}
			} else { //Doesn't have a ball, close claw for panels
				pneumatics.clawSolenoid.set(CLAW_CLOSED);
			}
		} else {
			pneumatics.clawSolenoid.set(CLAW_OPEN);
		}
		
		//Allow multiple ball ejects
		if(!controls.getEjectBall() && !newEjectBallPress) {
			newEjectBallPress = true;
		}
		
		//Eject balls
		if(ejectingBall) {
			if(ballEjectTimer-- <= 0) ejectingBall = false;
			
			motors.leftClaw.set(GET_BALL_DIRECTION ? CLAW_WHEEL_SPEED : -CLAW_WHEEL_SPEED);
			motors.rightClaw.set(GET_BALL_DIRECTION ? -CLAW_WHEEL_SPEED : CLAW_WHEEL_SPEED);
		}
		
		
		if(controls.getPOV() == 0) {
			motors.leftClaw.set(CLAW_WHEEL_SPEED);
			motors.rightClaw.set(-CLAW_WHEEL_SPEED);
		} else if(controls.getPOV() == 180) {
			motors.leftClaw.set(-CLAW_WHEEL_SPEED);
			motors.rightClaw.set(CLAW_WHEEL_SPEED);
		}
	}
	
	/**
	 * Runs the elevator
	 */
	public void runElevator() {
		//This section runs the elevator to specific positions
		boolean elevatorButtonsPressed = false;
		int elevatorPressIndex = -1;
		
		for(int i = 0 ; i < 6; i++) {
			if(controls.getElevatorButton(i)) {
				elevatorButtonsPressed = true;
				elevatorPressIndex = i;
				break;
			}
		}
		
		//New button presses (don't spam every tick)
		if(elevatorButtonsPressed && newElevatorPress) {
			newElevatorPress = false;
			
			elevator.moveTo(elevatorPressIndex);
		} else if(!elevatorButtonsPressed && !newElevatorPress) {
			newElevatorPress = true;
		}
		
		//This section runs the elevator manually
		double elevatorSpeed = 0;
		if(controls.getElevatorUp()) elevatorSpeed += ELEVATOR_MANUAL_SPEED;
		if(controls.getElevatorDown()) elevatorSpeed -= ELEVATOR_MANUAL_SPEED;
		
		//double t = mapDouble(controls.getThrottleAxis(), 1, -1, 0, 1);
		//System.out.println(t);
		
		elevatorSpeed += ELEVATOR_MAINTENANCE_SPEED;
		
		if(!elevatorRunning) {
			//Only move up when at bottom; only move down or maintain when at the top
			if((sensors.bottomLimitSwitch.get() && elevatorSpeed < ELEVATOR_MANUAL_SPEED + ELEVATOR_MAINTENANCE_SPEED) ||
				(sensors.elevatorSensors[5].get() && elevatorSpeed > ELEVATOR_MAINTENANCE_SPEED)) {
				motors.leftElevator.set(0);
				motors.rightElevator.set(0);
			} else {
				motors.leftElevator.set(-elevatorSpeed);
				motors.rightElevator.set(elevatorSpeed);
			}
		}
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
	
	/**
	 * Sets whether or not the elevator is in use
	 * 
	 * @param run The new value
	 */
	public void setElevatorRunning(boolean run) {
		elevatorRunning = run;
	}
	
	/**
	 * Sets whether or note the claw is in use
	 * 
	 * @param run The new value
	 */
	public void setClawRunning(boolean run) {
		clawRunning = run;
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	@Override
	public void disabledInit() {
		if (autoCommand != null) {
			autoCommand.cancel();
			autoCommand = null;
		}
	}

	@Override
	public Controls getControls() {
		return controls;
	}

	@Override
	public Drive getDriveSystem() {
		return drive;
	}

	@Override
	public SightData getSightData() {
		return sightData;
	}

	@Override
	public Motors getMotors() {
		return motors;
	}

	@Override
	public Sensors getSensors() {
		return sensors;
	}
	
	@Override
	public Pneumatics getPneumatics() {
		return pneumatics;
	}

	@Override
	public Elevator getElevator() {
		return elevator;
	}

	@Override
	public Robot getRobot() {
		return this;
	}
}