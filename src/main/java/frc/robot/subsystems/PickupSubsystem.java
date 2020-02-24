package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
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

    boolean autoIntakeEnabled = true;

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
     * Periodic things aaaaaaaaa
     */
    public void periodic() {
        //Checks if ball is ready to be entered into mag
        if (Sensors.intakeSensor.get() && autoIntakeEnabled) {
            //Starts mag management command
            new IntakeNewBallCommand(this).schedule();
        }
    }

    //Gets automatic intake enabled value
    public boolean getAutoIntakeEnabled() {
        return autoIntakeEnabled;
    }
}
