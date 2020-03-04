package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ShooterCommand;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

public class ShooterSubsystem extends SubsystemBase {
    private PIDController topPid = new PIDController(6e-5 * 3, 1e-6 * 3, 0);
    private PIDController botPid = new PIDController(6e-5 * 3, 1e-6 * 3, 0);
    {
        topPid.setSetpoint(0);
        botPid.setSetpoint(0);
    }

    PickupSubsystem pickupSubsystem;

    private static final double FF = /*1.5*/19e-5 * 3;

    private double currentRPM = 0;

    public ShooterSubsystem(PickupSubsystem pickupSubsystemIn) {
        super();
        pickupSubsystem = pickupSubsystemIn;
    }

    public void setSpeed(double rpm) {
        currentRPM = rpm;
        topPid.setSetpoint(rpm);
        botPid.setSetpoint(rpm);
    }

    public double getTargetSpeed() {
        return currentRPM;
    }

    @Override
    public void periodic() {
        if (!DriverStation.getInstance().isDisabled()) {
            if (Math.abs(currentRPM) < 1e-6) {
                Motors.topFly.set(0);
            } else {
                Motors.topFly.set(motorFilter(topPid.calculate(Sensors.topFlyEncoder.getVelocity()) + FF * topPid.getSetpoint()));
            }
            if (Math.abs(currentRPM) < 1e-6) {
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

    /**
     * Runs the auto command for shooting ball, or cancels if it already exists
     *
     * @param fireOne If the shooter should fire one ball or fire until a certain amount of time between balls shot is reached.
     */
    public void triggerAutoShooter(boolean fireOne) {
        Command c = pickupSubsystem.getCurrentCommand();
        if (c == null) {
            new ShooterCommand(pickupSubsystem, fireOne).schedule();
        } else {
            c.cancel();
        }
    }
}