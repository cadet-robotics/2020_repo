package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

public class ShooterSubsystem extends SubsystemBase {
    private PIDController topPid = new PIDController(0.2, 0, 0);
    private PIDController botPid = new PIDController(0.2, 0, 0);
    {
        topPid.setSetpoint(0);
        botPid.setSetpoint(0);
    }

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
            Motors.topFly.set(topPid.calculate(Sensors.topFlyEncoder.getRate()));
            Motors.bottomFly.set(botPid.calculate(Sensors.bottomFlyEncoder.getRate()));
        } else {
            Motors.topFly.set(0);
            Motors.bottomFly.set(0);
        }
    }
}