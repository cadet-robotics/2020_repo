package frc.robot.wheel;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;

import static frc.robot.wheel.ColorEnum.*;

class ColorWheelUtil {
    private ColorWheelUtil() {
    }

    /**
     * Retrieves the color we should set the wheel to
     *
     * Does not account for the difference in position
     * of our sensor and the field sensor
     *
     * @return The wheel color
     */
    private static ColorEnum getGoalRaw() {
        String s = DriverStation
                .getInstance()
                .getGameSpecificMessage();
        if (s.length() == 0) {
            return null;
        } else {
            switch (s.charAt(0)) {
                case 'R': return RED;
                case 'G': return GREEN;
                case 'B': return BLUE;
                case 'Y': return YELLOW;
                default: return null;
            }
        }
    }

    /**
     * Retrieves the color we should set the wheel to
     *
     * Accounts for the difference in position
     * of our sensor and the field sensor
     *
     * @return The wheel color
     */
    static ColorEnum getGoal() {
        // (x + 2) % 4 == x ^ 2
        switch (getGoalRaw()) {
            case RED: return BLUE;
            case GREEN: return YELLOW;
            case BLUE: return RED;
            case YELLOW: return GREEN;
        }
        return null;
    }

    //private static final double MIN_VAL = 0.4;

    /**
     * Converts an RGB color to HSV
     *
     * Algorithm derived from https://cs.rit.edu/~ncs/color/t_convert.html
     *
     * @param c The RGB color
     * @return The Hue value, or NaN if the hue is too uncertain
     */
    static double toHue(Color c) {
        // We don't want saturation
        // so we can leave that out
        double min = Math.min(Math.min(c.red, c.green), c.blue);
        double max = Math.max(Math.max(c.red, c.green), c.blue);
        double d = max - min;
        /*
        if (max < MIN_VAL) {
            System.out.println("BELOW MIN VAL: " + max);
            return Double.NaN;
        }
        */
        if (c.red == max) {
            return (c.green - c.blue) / d * 60;
        } else if (c.green == max) {
            return 120 + (c.blue - c.red) / d * 60;
        } else {
            return 240 + (c.red - c.green) / d * 60;
        }
    }

    /**
     * Finds the difference between two spots on a wheel
     *
     * Takes into account the circular nature of the wheel
     *
     * @param a The first position in degrees
     * @param b The second position in degrees
     * @return The difference in degrees
     */
    static double getWheelDiff(double a, double b) {
        double diff = Math.abs(a - b);
        if (diff > 180) {
            diff = 360 - diff;
        }
        return diff;
    }
}
