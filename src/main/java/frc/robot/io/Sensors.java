package frc.robot.io;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc6868.config.api.Config;

public class Sensors {
    public static Encoder topFlyEncoder,
                          bottomFlyEncoder,
                          driveEncoderLeft,
                          driveEncoderRight;
    public static Gyro gyro;

    public static void loadConfig(Config c) {
    }
}