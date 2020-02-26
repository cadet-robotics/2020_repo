package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.io.Sensors;
import frc.robot.subsystems.PickupSubsystem;

/**
 * Brings ball from position B1 (base) to position B2 (mag entry)
 *
 * @author Matt Robinson
 */
public class BallToMagCommand extends CommandBase {
    PickupSubsystem pickupSubsystem;

    /**
     * Constructor requires pickupSubsystem
     *
     * @param pickupSubsystemIn The pickup subsystem instance
     */
    public BallToMagCommand(PickupSubsystem pickupSubsystemIn) {
        pickupSubsystem = pickupSubsystemIn;
        addRequirements(pickupSubsystem);
    }

    /**
     * Enables motors
     */
    @Override
    public void execute() {
        //Enables motors
        pickupSubsystem.setIntakeSpeed(Constants.INTAKE_SPEED);
    }

    /**
     * Finalizes the command if the magazine sensor is triggered
     *
     * @return The sensor value
     */
    @Override
    public boolean isFinished() {
        //Sensor picks up ball, bring to b2. Stop command.
        return Sensors.magSensor.get();
    }


}
