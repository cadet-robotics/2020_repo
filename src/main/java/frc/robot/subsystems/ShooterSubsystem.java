package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

public class ShooterSubsystem extends SubsystemBase {
    private PIDController topPid = new PIDController(6e-5 * 3, 1e-6 * 3, 0);
    private PIDController botPid = new PIDController(6e-5 * 3, 1e-6 * 3, 0);
    {
        topPid.setSetpoint(0);
        botPid.setSetpoint(0);
    }
    private static final double FF = /*1.5*/19e-5 * 3;

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
            if (Math.abs(topPid.getSetpoint()) < 1e-6) {
                Motors.topFly.set(0);
            } else {
                Motors.topFly.set(motorFilter(topPid.calculate(Sensors.topFlyEncoder.getVelocity()) + FF * topPid.getSetpoint()));
            }
            if (Math.abs(botPid.getSetpoint()) < 1e-6) {
                Motors.bottomFly.set(0);
            } else {
                Motors.bottomFly.set(motorFilter(botPid.calculate(Sensors.bottomFlyEncoder.getVelocity()) + FF * botPid.getSetpoint()));
            }
        } else {
            Motors.topFly.set(0);
            Motors.bottomFly.set(0);
        }
    }

    private static double motorFilter(double d) {
        return Math.max(Math.min(d, 1), -1);
    }
}