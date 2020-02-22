package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.magazine.IntakeNewBallCommand;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

/**
 * Subsystem containing methods for ball intake and magazine management
 *
 * @author Matt Robinson
 */
public class PickupSubsystem extends SubsystemBase {
    PWMVictorSPX intake;
    PWMVictorSPX mag;

    /**
     * Constructor for subsystem
     */
    public PickupSubsystem() {
        intake = Motors.intake;
        mag = Motors.magazine;
    }

    /**
     * Sets the motor speed for the intake wheels
     *
     * @param speedIn The speed to set the motor to (-1.0 to 1.0)
     */
    public void setIntakeSpeed(double speedIn) {
        intake.setSpeed(speedIn);
    }

    /**
     * Sets the motor speed for the magazine wheels
     *
     * @param speedIn The speed to set the motor to (-1.0 to 1.0)
     */
    public void setMagazineSpeed(double speedIn) {
        mag.setSpeed(speedIn);
    }

    /**
     * The initial method for the magazine loading.
     *
     * B1 - The position of the ball in the intake. Ready to be picked up
     * B2 - The position of the ball when entering the magazine.
     * B3 - Some ball above B2. Must be translated upwards.
     * TODO: WIP
     */
    public void intakeBall() {
        //Ball has been detected and ready to be picked up. Bring ball from B1 to B2

    }

    /**
     * Periodic things aaaaaaaaa
     */
    public void periodic() {
        //Checks if ball is ready to be entered into mag
        if (Sensors.intakeSensor.get()) {
            //Starts mag management command
            new IntakeNewBallCommand().schedule();
        }
    }
}
