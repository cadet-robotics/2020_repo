package frc.robot.sensors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.config.ConfigUtil;

/**
 * Contains the sensors
 * <p>Javadoc comment lovingly provided by Alex Pickering
 *
 * Later modified to use ConfigUtil
 *
 * @author Owen Avery, Alex Pickering
 */
public class Sensors {
	//null until we have a gyro on the robot
    public Gyro gyro = null; //new ADXRS450_Gyro();
    
    public DigitalInput[] elevatorSensors;
    
    public DigitalInput topLimitSwitch,
    					bottomLimitSwitch;
    
    public AnalogInput ballDistance;
    
    JsonObject configJSON;
    
    /**
     * Creates the sensors object
     * 
     * @param conf The config to parse
     */
    public Sensors(JsonObject conf) {
    	elevatorSensors = new DigitalInput[6];
    	
    	init(conf);
    }
    
    /**
     * Initializes any sensors that need to use the config
     * 
     * @param config The robot config file
     * @author Alex Pickering
     */
    public void init(JsonObject config) {
    	this.configJSON = config;
    	
    	loadDIOSensors();
    }
    
    public void loadDIOSensors() {
    	JsonObject dioJSON = configJSON.getAsJsonObject("dio"),
    			   ainJSON = configJSON.getAsJsonObject("analog in");
    	
    	//Load DIO sensors
    	for(String k : dioJSON.keySet()) {
    		if(ConfigUtil.isFiltered(k)) continue;
    		
    		JsonElement item = dioJSON.get(k);
    		int itemInt = ConfigUtil.getAsInt(item, "DIO", k);
    		
    		switch(k) {
    			case "proximity sensor 1":
    				elevatorSensors[0] = new DigitalInput(itemInt);
    				break;
    			
    			case "proximity sensor 2":
    				elevatorSensors[1] = new DigitalInput(itemInt);
    				break;
    			
    			case "proximity sensor 3":
    				elevatorSensors[2] = new DigitalInput(itemInt);
    				break;
    			
    			case "proximity sensor 4":
    				elevatorSensors[3] = new DigitalInput(itemInt);
    				break;
    			
    			case "proximity sensor 5":
    				elevatorSensors[4] = new DigitalInput(itemInt);
    				break;
    			
    			case "proximity sensor 6":
    				elevatorSensors[5] = new DigitalInput(itemInt);
    				break;
    			
    			case "bottom limit switch":
    				bottomLimitSwitch = new DigitalInput(itemInt);
    				break;
    			
    			case "top limit switch":
    				topLimitSwitch = new DigitalInput(itemInt);
    				break;
    			
    			default:
    				System.err.println("Unrecognized DIO Sensor: " + k);
    		}
    	}
    	
    	//Load AIn sensors
    	for(String k : ainJSON.keySet()) {
    		if(ConfigUtil.isFiltered(k)) continue;
    		
    		JsonElement item = ainJSON.get(k);
    		int itemInt = ConfigUtil.getAsInt(item, "AIN", k);
    		
    		switch(k) {
    			case "ball distance sensor":
    				ballDistance = new AnalogInput(itemInt);
    				break;
    			
    			default:
    				System.err.println("Unregocnized AIn Sensor: " + k);
    		}
    	}
    }
}