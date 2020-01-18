package frc.robot.wheel;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.ColorShim;

public class ColorWheel {
    ColorSensorV3 sensor;
    ColorChangeTracker tracker;

    public ColorWheel() {
        sensor = new ColorSensorV3(I2C.Port.kOnboard);
        tracker = new ColorChangeTracker(this);
        tracker.start();
    }

    private static final int NUM_SAMPLES = 4;

    Color getColor() {
        double r = 0, g = 0, b = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            Color c = sensor.getColor();
            r += c.red;
            g += c.green;
            b += c.blue;
        }
        return new ColorShim(
                r / NUM_SAMPLES,
                g / NUM_SAMPLES,
                b / NUM_SAMPLES);
    }

    double getHue() {
        return ColorWheelUtil.getHue(getColor());
    }

    int getId() {
        return ColorWheelUtil.toId(getHue());
    }

    public int getCount() {
        return tracker.getCount();
    }
}
