package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import java.util.function.Supplier;

/**
 * Automatically magdumps the robot
 *
 * Can also be used to threaten to split the moon in half
 *
 * @author Owen
 */
public class FDriverFactory {
    private FDriverFactory() {
    }

    /**
     * Used to produce an FDriver command
     *
     * The produced command must be executed at exactly the right time
     * https://worm.fandom.com/wiki/String_Theory
     *
     * @param shooter The ShooterSubsystem
     * @param pick The PickupSubsystem
     * @param rpm The RPM to fire at
     * @return A FDriver command
     */
    public static Command produce(ShooterSubsystem shooter, PickupSubsystem pick, double rpm) {
        return new ShooterCommand(pick, false).deadlineWith(new SetShooterSpeedCommand(shooter, rpm, -1));
    }
}