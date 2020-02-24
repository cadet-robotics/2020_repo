package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.io.Motors;
import frc.robot.subsystems.PickupSubsystem;

/**
 * The overlord of all mag commands. He manifests power. He also takes in new balls
 * and stores them in the shooter. Either way: power.
 *
 * B1 - The position of the ball in the intake. Ready to be picked up
 * B2 - The position of the ball when entering the magazine.
 * B3 - Some ball above B2. Must be translated upwards.
 *
 * @author Matt Robinson
 */
public class IntakeNewBallCommand extends SequentialCommandGroup {
    PickupSubsystem pickupSubsystem;

    /**
     * Constructor. Executes B1 -> B2 & B2 -> B3
     */
    public IntakeNewBallCommand(PickupSubsystem pickupSubsystemIn) {
        super(
                new BallToMagCommand(pickupSubsystemIn),
                new TranslateMagCommand(pickupSubsystemIn)
        );
        pickupSubsystem = pickupSubsystemIn;
    }

    /**
     * Turns off motors and such
     *
     * @param interrupt Unsure what this does but its important
     */
    public void end(boolean interrupt) {
        super.end(interrupt);
        pickupSubsystem.setIntakeSpeed(0);
        pickupSubsystem.setMagazineSpeed(0);
    }
}
