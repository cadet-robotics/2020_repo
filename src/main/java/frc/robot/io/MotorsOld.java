package frc.robot.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.wpi.first.wpilibj.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains motor objects and loads their config
 * Javadoc comments lovingly provided by Alex Pickering
 * 
 * @author Owen Avery
 * @deprecated
 */
@Deprecated
public class MotorsOld {
    private HashMap<String, SpeedController> map = new HashMap<>();
    
    private static HashMap<String, String> typeMapDefault = new HashMap<>();
    private static HashMap<String, Integer> portMapDefault = new HashMap<>();
    //private static HashMap<String, String>

    private static void addMotorDefault(String n, String t, int p) {
        portMapDefault.put(n, p);
        typeMapDefault.put(n, t);
    }

    static {
        addMotorDefault("front left","victor",2);
        addMotorDefault("rear left","victor",3);
        addMotorDefault("front right","victor",1);
        addMotorDefault("rear right","victor",0);
        //addMotorDefault("elevator1", "victor", );
    }

    /**
     * Gets a motor, returning a fake one if unsuccessful
     * @param s The motor name
     * @return
     */
    public SpeedController getSpeedControllerOrInert(String s) {
        return getSpeedControllerOrFallback(s, new InertSpeedController());
    }

    /**
     * Gets a motor, returning a fallback one if unsuccessful
     * @param s The motor name
     * @param sf The fallback motor
     * @return
     */
    public SpeedController getSpeedControllerOrFallback(String s, SpeedController sf) {
        if (!map.containsKey(s)) return sf;
        return map.get(s);
    }

    /**
     * Gets a motor, returning null if unsuccessful
     * @param s The motor name
     * @return
     */
    public SpeedController getSpeedController(String s) {
        return map.get(s);
    }

    /**
     * Figures out if we have a motor
     * @param s The motor name
     * @return
     */
    public boolean hasSpeedController(String s) {
        return map.containsKey(s);
    }

    /**
     * Default constructor
     * 
     * @param configIn JSON Config reading
     * @throws IOException
     */
    public MotorsOld(JsonObject configIn) throws IOException {
        //int fl = 2, rl = 3, fr = 1, rr = 0; //Default values
        try {
            JsonObject con = configIn.getAsJsonObject("pwm");
            
            for (String s : con.keySet()) {
                if (s.equals("desc")) continue;
                
                JsonElement e = con.get(s);
                SpeedController speed = getSpeedController(e, s);
                if (speed != null) map.put(s, speed);
            }
            
            SpeedController t;
            
            for (String s : portMapDefault.keySet()) {
                if (!map.containsKey(s)) {
                    t = getMotor(portMapDefault.get(s), typeMapDefault.get(s));
                    if (t != null) map.put(s, t);
                }
            }
        } catch (UnsupportedOperationException e) {
            throw new IOException(e);
        }
    }

    /**
     *
     * @param e The JsonElement
     * @param n The name of the motor/key for the JsonObject
     * @return A SpeedController for a JsonElement motor definition
     */
    public static SpeedController getSpeedController(JsonElement e, String n) {
        // Cross compatible - each motor can be an int
        try {
            int j = e.getAsInt();
            String t = "victor";
            
            if (typeMapDefault.containsKey(n)) {
                t = typeMapDefault.get(n);
            }
            
            return getMotor(j, t);
        } catch (UnsupportedOperationException ex2) {}
        
        JsonObject obj;
        
        try {
            obj = e.getAsJsonObject(); // Is it an object?
        } catch (UnsupportedOperationException ex) {return null;} // Nope
        
        try {
            int port;
            
            try {
                port = obj.get("port").getAsInt(); // Get port value as an int
            } catch (UnsupportedOperationException ex) {
                try {
                    // It's probably a list of other motors, so let's build a group speed controller
                    JsonArray motorList = obj.get("port").getAsJsonArray();
                    ArrayList<SpeedController> s = new ArrayList<>();
                    SpeedController t;
                    
                    for (int i = 0; i < motorList.size(); i++) {
                        if ((t = getSpeedController(motorList.get(i), n + "#" + i)) != null) {
                            // We turned one of the elements into a speed controller
                            s.add(t); // So add it to the list
                        }
                    }
                    
                    if (s.size() == 0) return null; // No speed controllers in list
                    if (s.size() == 1) return s.get(0); // Only one speed controller
                    
                    SpeedController speedFirst = s.remove(0); // Take the first controller
                    SpeedController[] list = s.toArray(new SpeedController[0]); // Turn the rest into an array
                    
                    return new SpeedControllerGroup(speedFirst, list); // And create the group
                } catch (UnsupportedOperationException ex2) {
                    return null; // It's some other weird thing that we can't handle
                }
            }
            
            // Let's get its type (talon/spark/vector/etc)
            String type;
            try {
                type = obj.get("type").getAsString();
            } catch (UnsupportedOperationException ex) {
                type = "victor"; // Victor is default
            }
            
            SpeedController s = getMotor(port, type); // Get the motor
            if (s == null) return null; // ...aaaand it failed
            
            try {
                if (obj.get("reverse").getAsBoolean()) {
                    // It has "reverse": true
                    s.setInverted(true); // So reverse it
                }
            } catch (UnsupportedOperationException ex) {} // Don't reverse it
            return s; // Return it
        } catch (UnsupportedOperationException ex) {
            return null;
        }
    }

    /**
     * Gets a Speed Controller from a port and type
     * @param port The motor port
     * @param type The motor's type
     * @return
     */
    public static SpeedController getMotor(int port, String type) {
        switch (type) {
            case "victor":
                return new PWMVictorSPX(port);
            case "talon":
                return new PWMTalonSRX(port);
            case "spark":
                return new Spark(port);
        }
        return null;
    }
}

/**
 * Does literally nothing but prevent NullPointerExceptions
 * Use carefully
 */
class InertSpeedController implements SpeedController {
    double speed = 0;
    boolean inverted = false;

    @Override
    public void set(double speedIn) {
        speed = speedIn;
    }

    @Override
    public double get() {
        return speed;
    }

    @Override
    public void setInverted(boolean isInverted) {
        inverted = isInverted;
    }

    @Override
    public boolean getInverted() {
        return inverted;
    }

    @Override
    public void disable() {
    }

    @Override
    public void stopMotor() {
    }

    @Override
    public void pidWrite(double output) {
        speed = 0;
    }
}