package frc.robot.io;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.VictorSP;
import frc.robot.config.ConfigUtil;

/**
 * The legibility-orient rewrite of the motors class
 *
 * <p>Later modified to use ConfigUtil
 *
 * @author Alex Pickering, Owen Avery
 */
public class Motors {
	//Config object
	JsonObject configJSON;
	
	//Configured motors record
	ArrayList<String> configuredMotors;
	
	//Motor Objects
	public CANSparkMax frontLeftDrive,
					   frontRightDrive,
					   backLeftDrive,
					   backRightDrive;
	
	public Talon leftElevator,
				 rightElevator;
	
	public VictorSP leftClaw,
					rightClaw;
			 
	
	boolean debug = true;
	
	/**
	 * Creates the motors object
	 * 
	 * @param conf The config to parse
	 */
	public Motors(JsonObject conf) {
		configuredMotors = new ArrayList<>();
		
		init(conf);
	}
	
	/**
	 * Gets the list of configured motors
	 * 
	 * @return A list of configured motors
	 */
	public ArrayList<String> getConfiguredMotors() {
		return configuredMotors;
	}
	
	/**
	 * Initalizes the motors
	 * 
	 * @param configJSON The isntance of the configuration file
	 */
	public void init(JsonObject configJSON) {
		this.configJSON = configJSON;
		
		loadMotors();
	}
	
	/**
	 * Loads the motors from the config
	 */
	public void loadMotors() {
		JsonObject pwmJSON = configJSON.getAsJsonObject("pwm");
		
		configuredMotors = new ArrayList<>();
		
		for(String k : pwmJSON.keySet()) {
			if(ConfigUtil.isFiltered(k)) continue;
			
			JsonObject item = pwmJSON.getAsJsonObject(k);
			int itemInt = ConfigUtil.getAsInt(item.get("id"), "PWM", k);
			configuredMotors.add(k);
			
			switch(k) {
				case "front left":
					frontLeftDrive = new CANSparkMax(itemInt, MotorType.kBrushed);
					break;

				case "front right":
					frontRightDrive = new CANSparkMax(itemInt, MotorType.kBrushed);
					break;

				case "rear left":
					backLeftDrive = new CANSparkMax(itemInt, MotorType.kBrushed);
					break;

				case "rear right":
					backRightDrive = new CANSparkMax(itemInt, MotorType.kBrushed);
					break;

				case "left elevator":
					leftElevator = new Talon(itemInt);
					break;

				case "right elevator":
					rightElevator = new Talon(itemInt);
					break;

				case "left claw wheel":
					leftClaw = new VictorSP(itemInt);
					break;

				case "right claw wheel":
					rightClaw = new VictorSP(itemInt);
					break;

				default:
					System.err.println("Unrecognized motor: " + k);
					return;
			}
			configuredMotors.add(k);
		}
    }

	/**
	 * Sets all motors to 0 for safety
	 */
	public void resetAll() {
		frontLeftDrive.set(0);
		frontRightDrive.set(0);
		backLeftDrive.set(0);
		backRightDrive.set(0);

		leftElevator.set(0);
		rightElevator.set(0);

		leftClaw.set(0);
		rightClaw.set(0);
	}
}