package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {
    private ColorSensorV3 colorSensor;

    public ArmSubsystem() {
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    }
}