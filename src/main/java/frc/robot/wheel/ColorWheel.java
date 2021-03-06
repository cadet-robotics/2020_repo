package frc.robot.wheel;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.ColorShim;

public class ColorWheel {
    private ColorSensorV3 sensor;
    private ColorChangeTracker tracker;

    public ColorWheel() {
        sensor = new ColorSensorV3(I2C.Port.kOnboard);
        tracker = new ColorChangeTracker(this);
        tracker.start();
    }

    private static final int NUM_SAMPLES = 1;

    /**
     * Gets the color from the color sensor
     * Takes NUM_SAMPLES samples and averages them
     *
     * @return The color from the sensor as RGB
     */
    private Color getColorRGB() {
        double r = 0, g = 0, b = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            Color c = sensor.getColor();
            r += c.red;
            g += c.green;
            b += c.blue;
        }
        r *= 2.2 / 3.0;
        g *= 0.5;
        b *= 1;
        //System.out.println("R: " + r + ", G: " + g + ", B: " + b);
        return new ColorShim(
                r / NUM_SAMPLES,
                g / NUM_SAMPLES,
                b / NUM_SAMPLES);
    }

    /**
     * Gets the color from the color sensor
     * Takes NUM_SAMPLES samples and averages them
     *
     * @return The color from the sensor as a wheel color, or null
     */
    public ColorEnum getColor() {
        return ColorEnum.from(getColorRGB());
    }

    public int getCount() {
        return tracker.getCount();
    }
}
