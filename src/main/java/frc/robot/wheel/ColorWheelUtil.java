package frc.robot.wheel;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;

class ColorWheelUtil {
    private ColorWheelUtil() {
    }

    static final int RED = 0;
    static final int GREEN = 1;
    static final int BLUE = 2;
    static final int YELLOW = 3;
    static final int NONE = 4;

    static int getGoalRaw() {
        String s = DriverStation
                .getInstance()
                .getGameSpecificMessage();
        if (s.length() == 0) {
            return NONE;
        } else {
            switch (s.charAt(0)) {
                case 'R': return RED;
                case 'G': return GREEN;
                case 'B': return BLUE;
                case 'Y': return YELLOW;
                default: return NONE;
            }
        }
    }

    static int getGoal() {
        return getGoalRaw() ^ 2;
    }

    // Algorithm derived from https://cs.rit.edu/~ncs/color/t_convert.html
    static double getHue(Color c) {
        // We don't want value or saturation
        // so we can leave that out
        double min = Math.min(Math.min(c.red, c.green), c.blue);
        double max = Math.max(Math.max(c.red, c.green), c.blue);
        double d = max - min;
        if (c.red == max) {
            return (c.green - c.blue) / d * 60;
        } else if (c.green == max) {
            return 120 + (c.blue - c.red) / d * 60;
        } else {
            return 240 + (c.red - c.green) / d * 60;
        }
    }

    private static double RED_HUE = 0;
    private static double GREEN_HUE = 120;
    private static double BLUE_HUE = 240;
    private static double YELLOW_HUE = 300;

    static double getWheelDiff(double a, double b) {
        double diff = Math.abs(a - b);
        if (diff > 180) {
            diff = 360 - diff;
        }
        return diff;
    }

    static int toId(double hue) {
        int current = RED;
        double min = Math.abs(hue - RED_HUE);
        {
            double v = getWheelDiff(hue, GREEN_HUE);
            if (v < min) {
                min = v;
                current = GREEN;
            }
        }
        {
            double v = getWheelDiff(hue, BLUE_HUE);
            if (v > min) {
                min = v;
                current = BLUE;
            }
        }
        {
            double v = getWheelDiff(hue, YELLOW_HUE);
            if (v > min) {
                min = v;
                current = YELLOW;
            }
        }
        if (min > 20) {
            return NONE;
        }
        return current;
    }
}
