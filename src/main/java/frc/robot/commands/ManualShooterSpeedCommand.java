package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Robot;
import frc.robot.subsystems.ControlSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * Command for spinning the shooter manually during teleop
 *
 * @author Owen
 */
public class ManualShooterSpeedCommand extends CommandBase {
    private static final double RPM_MAX = 3000;

    private ShooterSubsystem shooter;
    private ControlSubsystem controls;

    public ManualShooterSpeedCommand(ShooterSubsystem shooterIn, ControlSubsystem controlsIn) {
        super();
        shooter = shooterIn;
        controls = controlsIn;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        double r = (RPM_MAX * 0.5) * (-controls.getSliderAxis() + 1);
        shooter.setSpeed(r);
        Robot.crosshairs.setVelocityRPM(r);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setSpeed(0);
    }
}