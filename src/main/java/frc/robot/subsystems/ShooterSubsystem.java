package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

public class ShooterSubsystem extends SubsystemBase {
    private PIDController topPid = new PIDController(6e-5, 1e-6, 0);
    private PIDController botPid = new PIDController(6e-5, 1e-6, 0);
    {
        topPid.setSetpoint(0);
        botPid.setSetpoint(0);
    }
    private static final double FF = 1.5e-5;

    public ShooterSubsystem() {
        super();
    }

    public void setSpeed(double rpm) {
        System.out.println("SETTING SPEED TO: " + rpm + "RPM");
        topPid.setSetpoint(rpm);
        botPid.setSetpoint(rpm);
    }

    @Override
    public void periodic() {
        if (!DriverStation.getInstance().isDisabled()) {
            Motors.topFly.set(motorFilter(topPid.calculate(Sensors.topFlyEncoder.getVelocity() + FF * topPid.getSetpoint())));
            Motors.bottomFly.set(motorFilter(botPid.calculate(Sensors.bottomFlyEncoder.getVelocity() + FF * botPid.getSetpoint())));
        } else {
            Motors.topFly.set(0);
            Motors.bottomFly.set(0);
        }
    }

    private static double motorFilter(double d) {
        return Math.max(Math.min(d, 0.5), -0.5);
    }
}