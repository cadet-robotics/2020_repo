package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.PickupSubsystem;

/**
 * Brings ball from position B2 to its finishing position, B3
 *
 * @author Matt Robinson
 */
public class TranslateMagCommand extends CommandBase {
    PickupSubsystem pickupSubsystem;
    int count = 0;

    /**
     * Constructor requires pickup subsystem
     *
     * @param pickupSubsystemIn The pickup subsystem instance
     */
    public TranslateMagCommand(PickupSubsystem pickupSubsystemIn) {
        pickupSubsystem = pickupSubsystemIn;
        addRequirements(pickupSubsystem);
    }

    /**
     * Translates by a given amount
     */
    public void execute() {
        if (pickupSubsystem.magBallCount == 3) {
            count = 69420; //Just needs to be higher than the constants value "MAGAZINE_SPEED"
            return;
        }

        pickupSubsystem.setMagazineSpeed(Constants.MAGAZINE_SPEED);
        count++;
    }

    /**
     * Finishes when a set amount of 20ms intervals has occurred.
     *
     * @return If the intervals exceeds or equals a given amount
     */
    public boolean isFinished() {
        if (count >= Constants.MAGAZINE_COUNT) {
            pickupSubsystem.magBallCount += 1;
            return true;
        }

        return false;
    }

}
