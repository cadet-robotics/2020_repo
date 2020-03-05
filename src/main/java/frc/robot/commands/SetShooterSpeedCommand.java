package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * Command for setting shooter speed and running the shooter
 *
 * @author Matt Robinson + Owen
 */
public class SetShooterSpeedCommand extends SequentialCommandGroup {
    /**
     * shouldReset is set to true if the shooter speed should be set to old on termination
     *
     * Remains false if using the constructor that doesn't reset shooter speed
     */
    private double old;
    private boolean shouldReset = false;

    /**
     * Remains null if using the constructor that doesn't reset shooter speed
     */
    private ShooterSubsystem shooterSubsystem;

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
     *                if negative, waits until the command is terminated
     * @param preserve Whether to set the shooter to its previous RPM target
     *                 instead of zero after the command finishes
     */
    public SetShooterSpeedCommand(ShooterSubsystem shooterSubsystemIn, double rpm, double seconds, boolean preserve) {
        super();
        shooterSubsystem = shooterSubsystemIn;
        addCommands(
                new InstantCommand(() -> {
                    old = preserve ? shooterSubsystem.getTargetSpeed() : 0;
                    shooterSubsystem.setSpeed(rpm);
                    shouldReset = true;
                }),
                (seconds < 0) ? new WaitUntilCommand(() -> false) : new WaitCommand(seconds)
        );
    }

    /**
     * Sets the shooter to run at a specific speed for a specific amount of time
     *
     * @param shooterSubsystemIn The shooter subsystem instance
     * @param rpm The speed of the motor, in RPM
     * @param seconds The amount of time to run the motor for, in seconds
     *                if negative, waits until the command is terminated
     */
    public SetShooterSpeedCommand(ShooterSubsystem shooterSubsystemIn, double rpm, double seconds) {
        this(shooterSubsystemIn, rpm, seconds, true);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        if (shouldReset) {
            shooterSubsystem.setSpeed(old);
        }
    }
}
