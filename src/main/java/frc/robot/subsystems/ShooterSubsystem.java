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
        topPid.setSetpoint(rpm);
        botPid.setSetpoint(rpm);
    }

    @Override
    public void periodic() {
        if (!DriverStation.getInstance().isDisabled()) {
            Motors.topFly.set(topPid.calculate(Sensors.topFlyEncoder.getVelocity()) + FF);
            Motors.bottomFly.set(botPid.calculate(Sensors.bottomFlyEncoder.getVelocity()) + FF);
        } else {
            Motors.topFly.set(0);
            Motors.bottomFly.set(0);
        }
    }
}