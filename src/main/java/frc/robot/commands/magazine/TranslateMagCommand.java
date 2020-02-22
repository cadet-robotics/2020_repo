package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PickupSubsystem;

public class TranslateMagCommand extends CommandBase {
    PickupSubsystem pickupSubsystem;

    public TranslateMagCommand(PickupSubsystem pickupSubsystemIn) {
        pickupSubsystem = pickupSubsystemIn;
        addRequirements(pickupSubsystem);
    }

    public void execute() {

    }

    public boolean isFinished() {
        return false;
    }

}
