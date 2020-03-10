package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.io.Sensors;
import frc.robot.subsystems.PickupSubsystem;

/**
 * Command for shooting ball(s) automagically
 *
 * @author Matt Robinson
 */
public class ShooterCommand extends CommandBase {
    boolean ignoreSensor; // Stuff for sensor return things
    PickupSubsystem pickupSubsystem;
    int lastBS; // Last ball shot. I swear this does not mean what you think it means.

    /**
     *  C o n s t r u c t    t h e    C o m m a n d
     * @param fireOne If the shooter fires one ball or multiple
     */
    public ShooterCommand(PickupSubsystem pickupSubsystemIn, boolean fireOne) {
        super();
        ignoreSensor = !fireOne;
        pickupSubsystem = pickupSubsystemIn;
        addRequirements(pickupSubsystemIn);
    }

    /**
     * E x e c u t e     t h e      C o m m a n d.
     */
    @Override
    public void execute() {
        System.out.println(lastBS + " : " + Sensors.shooterSensor.get());
        //Spin Motors
        pickupSubsystem.setIntakeSpeed(Constants.INTAKE_SPEED);
        pickupSubsystem.setMagazineSpeed(Constants.MAGAZINE_SPEED);

        //Get amount of time since last ball exit
        if (!Sensors.shooterSensor.get()) {
            lastBS = 0;
        } else {
            lastBS++;
        }

        System.out.println(lastBS);
    }

    /**
     *  F i n i s h     t h e    C o m m a n d
     */
    @Override
    public boolean isFinished() {
        if (ignoreSensor) {
            if (lastBS >= Constants.SHOOTER_COMMAND_END) {
                    return true;
            }
            return false;
        }
        return !Sensors.shooterSensor.get();
    }

    @Override
    public void end(boolean interrupted) {
        pickupSubsystem.setIntakeSpeed(0);
        pickupSubsystem.setMagazineSpeed(0);
    }
}
