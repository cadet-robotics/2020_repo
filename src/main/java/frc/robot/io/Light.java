package frc.robot.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PWM;

/**
 * Handles the camera light on the front of the robot
 */
public class Light {
    public DigitalOutput light = null;

    /**
     * Default constructor
     * Doesn't extend ConfigHandlerInt because we're looking for a single config item
     * @param o
     */
    public Light(JsonObject o) {
        JsonObject dio = null;
        JsonElement dioE;
        if (o.has("dio") && (dioE = o.get("dio")).isJsonObject()) {
            dio = dioE.getAsJsonObject();
            JsonElement lightE;
            if (dio.has("light") && (lightE = dio.get("light")).isJsonPrimitive() && ((JsonPrimitive) lightE).isNumber()) {
                light = new DigitalOutput(lightE.getAsInt());
            }
        }
        if (light == null) light = new DigitalOutput(2);
        light.enablePWM(0);
        light.setPWMRate(2000);
    }

    /**
     * Sets the light's intensity
     *
     * @param d the light's intensity, 0-1 inclusive
     */
    public void setIntensity(double d) {
        if (d < 0) d = 0;
        if (d > 1) d = 1;
        light.updateDutyCycle(d);
    }
}