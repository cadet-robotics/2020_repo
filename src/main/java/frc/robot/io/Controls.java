package frc.robot.io;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.wpi.first.wpilibj.Joystick;

import frc.robot.config.ConfigLoader;
import frc.robot.config.ConfigUtil;

/**
 * Contains controls objects and handles loading them from config
 *
 * @author Alex Pickering,
 */
public class Controls {
    //Config'd controls record
    ArrayList<String> configuredControls;
    
    //Controls objects
    Joystick mainJoystick;

    //Configured control ports and such
    //Axes
    int mainJoystickPort = 0,
            xAxis = 0,
            yAxis = 1,
            zAxis = 2,
            throttleAxis = 3;

    //Buttons
    int autoLockButtonPort = 5,
            elevatorUp = 5,
            elevatorDown = 3,
            getBall = 1,
            ejectBall = 2,
            toggleClaw = 4,
            toggleType = 9; //TODO: TEMPORARY
    int[] elevatorPos = new int[6];

    boolean debug = false;
    
    JsonObject configJSON;
  
    /**
     * Creates the controls object
     * 
     * @param conf The config to parse
     */
    public Controls(JsonObject conf) {
    	 configuredControls = new ArrayList<>();
    	 init(conf);
    }
  
    //Getters

    /**
     * Gets the X Axis control
     *
     * @return The X Axis
     */
    public double getXAxis() {
        return mainJoystick.getRawAxis(xAxis);
    }

    /**
     * Gets the Y Axis control
     *
     * @return The Y Axis
     */
    public double getYAxis() {
        return mainJoystick.getRawAxis(yAxis);
    }

    /**
     * Gets the Z Axis control
     *
     * @return The Z Axis
     */
    public double getZAxis() {
        return mainJoystick.getRawAxis(zAxis);
    }

    /**
     * Gets the throttle axis control
     *
     * @return The throttle axis
     */
    public double getThrottleAxis() {
        return mainJoystick.getRawAxis(throttleAxis);
    }

    /**
     * Gets the type toggle button
     * TODO: TEMPORARY
     *
     * @return Toggle Type button state
     */
    public boolean getToggleType() {
        return mainJoystick.getRawButton(toggleType);
    }
    
    //TODO: TEMPORARY
    public int getPOV() {
    	return mainJoystick.getPOV(); 
    }

    /**
     * Gets the Auto Lock Button state
     *
     * @return
     */
    public boolean isAutoLock() {
        return mainJoystick.getRawButton(autoLockButtonPort);
    }
    
    public ArrayList<String> getConfiguredControls(){
        return configuredControls;
    }
    
    /**
     * Initializes the controls
     * <p>Loads from config and creates the objects
     */
    public void init(JsonObject configJSON){
        this.configJSON = configJSON;
        
        try{
            loadControls();
        } catch(IOException e){
            System.err.println("Failed to load controls");
            e.printStackTrace();
        }
        
        mainJoystick = new Joystick(mainJoystickPort);
    }
    
    /**
     * Loads the controls from config
     * 
     * @throws IOException
     */
    public void loadControls() throws IOException {
        JsonObject configJSON = ConfigLoader.loadConfigFile();
        JsonObject controlsJSON = configJSON.getAsJsonObject("controls");
        
        //Debug - outputs all the json
        if(debug){
            for(String s : configJSON.keySet()){
                System.out.println(s + ": " + configJSON.get(s));
            }
        }
        
        configuredControls = new ArrayList<>();
        
        for(String k : controlsJSON.keySet()){
        	if(ConfigUtil.isFiltered(k)) continue;
        	
            JsonElement item = controlsJSON.get(k);
            int itemInt = ConfigUtil.getAsInt(item, "Controls", k);
            configuredControls.add(k);
            
            switch(k){
                case "main joystick":
                    mainJoystickPort = itemInt;
                    break;
                
                case "main joystick x-axis":
                    xAxis = itemInt;
                    break;
                
                case "main joystick y-axis":
                    yAxis = itemInt;
                    break;
                
                case "main joystick z-axis":
                    zAxis = itemInt;
                    break;
                
                case "main joystick throttle axis":
                	throttleAxis = itemInt;
                	break;

                case "main joystick auto-lock":
                    autoLockButtonPort = itemInt;
                    break;
                
                case "elevator hatch 1":
                	elevatorPos[0] = itemInt;
                	break;
                
                case "elevator ball 1":
                	elevatorPos[1] = itemInt;
                	break;
                
                case "elevator hatch 2":
                	elevatorPos[2] = itemInt;
                	break;
                
                case "elevator ball 2":
                	elevatorPos[3] = itemInt;
                	break;
                
                case "elevator hatch 3":
                	elevatorPos[4] = itemInt;
                	break;
                
                case "elevator ball 3":
                	elevatorPos[5] = itemInt;
                	break;
                
                case "elevator up":
                	elevatorUp = itemInt;
                	break;
                
                case "elevator down":
                	elevatorDown = itemInt;
                	break;
                
                case "toggle claw":
                	toggleClaw = itemInt;
                	break;
                
                case "get ball":
                	getBall = itemInt;
                	break;
                
                case "eject ball, get hatch":
                	ejectBall = itemInt;
                	break;
                
                case "toggle type": //TODO: TEMPORARY
                	toggleType = itemInt;
                	break;
                
                default:
                  System.out.println("Unrecognized Control: " + k);
            }
        }
    }
}