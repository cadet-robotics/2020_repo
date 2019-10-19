package frc.robot.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.config.ConfigUtil;

/**
 * Contains pneumatics objects and handles loading their config
 *
 * @author Alex Pickering
 */
public class Pneumatics {
	JsonObject configJSON;
	
	boolean debug = false;
	
	/**
	 * Creates the pneumatics object
	 * 
	 * @param conf The config to parse
	 */
	public Pneumatics(JsonObject conf) {
		init(conf);
	}
	
	/**
	 * Initialize objects, load config
	 * 
	 * @param configJSON The instance of the config file
	 */
	public void init(JsonObject configJSON) {
		if(debug) System.out.println("INIT PNEUMATICS");
		
		this.configJSON = configJSON;
		
		loadPneumatics();
	}
	
	/**
	 * Loads the pneumatics from the config
	 */
	public void loadPneumatics() {
		JsonObject pcmJSON = configJSON.getAsJsonObject("pcm");
		
		for(String k : pcmJSON.keySet()) {
			if(ConfigUtil.isFiltered(k)) continue;
			
			JsonElement item = pcmJSON.get(k);
			int itemInt = ConfigUtil.getAsInt(item, "PCM", k);
			
			switch(k) {
				default:
					System.err.println("Unrecognised Pneumatics Object: " + k);
			}
		}
	}
}