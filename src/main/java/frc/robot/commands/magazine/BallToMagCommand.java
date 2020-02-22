package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;
import frc.robot.subsystems.PickupSubsystem;

/**
 * Brings ball from position B1 (base) to position B2 (mag entry)
 *
 * @author Matt Robinson
 */
public class BallToMagCommand extends CommandBase {
    boolean isComplete = false;
    PickupSubsystem pickupSubsystem;

    public BallToMagCommand(PickupSubsystem pickupSubsystemIn) {
        pickupSubsystem = pickupSubsystemIn;
        addRequirements(pickupSubsystem);
    }

    @Override
    public void execute() {
        //Enables motors
        Motors.intake.set(0.2);
    }

    @Override
    public boolean isFinished() {
        //Sensor picks up ball, bring to b2. Stop command.
        return Sensors.intakeSensor.get();
    }
}
