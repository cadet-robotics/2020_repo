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
    int pInt = 0;

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
        pInt++;
    }

    /**
     * Finalizes the command if the magazine sensor is triggered
     *
     * @return The sensor value
     */
    @Override
    public boolean isFinished() {
        //After 3 balls are inside of the magazine, the bot attempts to store 2 in the intake
        if (pickupSubsystem.magBallCount > 999999) {
            return pInt >= Constants.INTAKE_COUNT_GR3;
        }

        //Sensor picks up ball, bring to b2. Stop command.
        return !Sensors.magSensor.get();
    }
}
