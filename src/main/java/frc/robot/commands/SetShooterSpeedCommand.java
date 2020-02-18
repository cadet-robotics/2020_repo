package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * Command for setting shooter speed and running the shooter
 *
 * @author Matt Robinson
 */
public class SetShooterSpeedCommand extends SequentialCommandGroup {
    /**
    * Sets the shooter speed
     *
     * @param shooterSubsystemIn The shooter subsystem instance
     */
    public SetShooterSpeedCommand(ShooterSubsystem shooterSubsystemIn, double rpm) {
        super(new InstantCommand(() -> shooterSubsystemIn.setSpeed(rpm)));
        addRequirements(shooterSubsystemIn);
    }

    /**
     * Sets the shooter to run at a specific speed for a specific amount of time
     *
     * @param shooterSubsystemIn The shooter subsystem instance
     * @param rpm The speed of the motor, in RPM
     * @param seconds The amount of time to run the motor for, in seconds
     */
    public SetShooterSpeedCommand(ShooterSubsystem shooterSubsystemIn, double rpm, double seconds) {
        super(
                new SetShooterSpeedCommand(shooterSubsystemIn, rpm),
                new WaitCommand(seconds),
                new SetShooterSpeedCommand(shooterSubsystemIn, rpm)
        );
    }
}
