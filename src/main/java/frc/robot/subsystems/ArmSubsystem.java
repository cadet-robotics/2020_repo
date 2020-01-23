package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.ColorShim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.Motors;
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
        int cnt = colorSensor.getCount();
        SmartDashboard.putNumber("CNT", cnt);
        if (cnt < 30) {
            Motors.wheelSpinner.set(0.18);
        } else {
            Motors.wheelSpinner.set(0);
        }
    }
}