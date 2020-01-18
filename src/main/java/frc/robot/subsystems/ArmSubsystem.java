package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.ColorShim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.wheel.ColorWheel;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;

public class ArmSubsystem extends SubsystemBase {
    private ColorWheel colorSensor;

    public ArmSubsystem() {
        colorSensor = new ColorWheel();
    }

    @Override
    public void periodic() {
        System.out.println("CNT: " + colorSensor.getCount());
    }
}