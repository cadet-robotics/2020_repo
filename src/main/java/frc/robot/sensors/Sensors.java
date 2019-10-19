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
    
    JsonObject configJSON;
    
    /**
     * Creates the sensors object
     * 
     * @param conf The config to parse
     */
    public Sensors(JsonObject conf) {
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
    			default:
    				System.err.println("Unregocnized AIn Sensor: " + k);
    		}
    	}
    }
}