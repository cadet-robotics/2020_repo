package frc.robot.wheel;

import edu.wpi.first.wpilibj.util.Color;
import static frc.robot.wheel.ColorWheelUtil.getWheelDiff;
import static frc.robot.wheel.ColorWheelUtil.toHue;

public enum ColorEnum {
    RED(10),
    GREEN(165),
    BLUE(215),
    YELLOW(70);

    // Measured experimentally
    // Used to match a hue to a wheel color
    private final double matchingHue;

    ColorEnum(double matchingHueIn) {
        matchingHue = matchingHueIn;
    }

    /**
     * Converts an RGB color to a wheel color
     *
     * @param c The RGB color
     * @return The wheel color, or null if the hue doesn't match sufficiently
     */
    public static ColorEnum from(Color c) {
        return from(toHue(c));
    }

    /**
     * Converts a hue value to a wheel color
     *
     * @param hue The hue, can be NaN
     * @return The wheel color, or null if the hue doesn't match sufficiently
     */
    public static ColorEnum from(double hue) {
        //System.out.println("HUE: " + hue);
        if (Double.isNaN(hue)) {
            return null;
        }
        // Finds the best match
        // Initial match is null, creating a matching threshold
        ColorEnum current = null;
        double min = 20;
        for (ColorEnum cmp : ColorEnum.values()) {
            double v = getWheelDiff(hue, cmp.matchingHue);
            if (v < min) {
                min = v;
                current = cmp;
            }
        }
        //System.out.println((current != null) ? current.name() : "NONE");
        return current;
    }
}