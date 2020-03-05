package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Robot;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
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
     * @param shooter the ShooterSubsystem
     * @param pick the PickupSubsystem
     * @param rpm the RPM to fire at
     * @return a FDriver command
     */
    public static Command produce(ShooterSubsystem shooter, PickupSubsystem pick, double rpm) {
        return new ShooterCommand(pick, false).deadlineWith(new SetShooterSpeedCommand(shooter, rpm, -1));
    }

    /**
     * Used to produce an FDriver command
     *
     * The produced command must be executed at exactly the right time
     * https://worm.fandom.com/wiki/String_Theory
     *
     * Uses a Supplier to get the distance when scheduled
     *
     * @param shooter the ShooterSubsystem
     * @param pick the PickupSubsystem
     * @param distanceSupplier supplies the distance
     * @return a FDriver command
     */
    public static Command produce(ShooterSubsystem shooter, PickupSubsystem pick, Supplier<Double> distanceSupplier) {
        SelectCommand cmd = new SelectCommand(() -> {
            Robot.crosshairs.setVelocityDistance(distanceSupplier.get());
            return produce(shooter, pick, Robot.crosshairs.getRPM());
        });
        cmd.addRequirements(shooter, pick);
        return cmd;
    }
}