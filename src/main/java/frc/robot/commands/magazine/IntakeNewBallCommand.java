package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.PickupSubsystem;

/**
 * The overlord of all mag commands. He manifests power. He also takes in new balls
 * and stores them in the shooter. Either way: power.
 *
 * @author Matt Robinson
 */
public class IntakeNewBallCommand extends SequentialCommandGroup {

    /**
     * Constructor. Executes B1 -> B2 & B2 -> B3
     */
    public IntakeNewBallCommand(PickupSubsystem pickupSubsystemIn) {
        super(
                new InstantCommand(() -> new BallToMagCommand(pickupSubsystemIn)),
                new InstantCommand(() -> new TranslateMagCommand(pickupSubsystemIn))
        );
    }

}
