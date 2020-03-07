package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
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

    public int magBallCount = Constants.INITIAL_BALL_COUNT;
    private boolean autoIntakeEnabled = true;

    /**
     * Constructor for subsystem
     */
    public PickupSubsystem() {
        super();
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
    	SmartDashboard.putNumber("frick", 27.726 * Math.pow(Sensors.intakeSensor.getAverageVoltage(), -1.2045));
    	if (this.getCurrentCommand() == null && isBallInRange(Sensors.intakeSensor, 5.0, 22.54) && autoIntakeEnabled) {
    		//Starts mag management command
            new IntakeNewBallCommand(this).schedule();
    	}
    	SmartDashboard.putNumber("Magazine Ball Count",  magBallCount);
    }

    /**
     * Calculates if the ball is in a certain range in the intake
     *
     * @param sensor The analog sensor to detect on
     * @param low The lowest range value, in cm
     * @param high The highest range value, in cm
     * @return A boolean success result
     */
    public boolean isBallInRange(AnalogInput sensor, double low, double high) {
        double distance = 27.726 * Math.pow(sensor.getAverageVoltage(), -1.2045);
        if (distance >= low && distance <= high) {
            return true;
        }
        return false;
    }

    //Gets automatic intake enabled value
    public boolean getAutoIntakeEnabled() {
        return autoIntakeEnabled;
    }
    
    /**
     * Toggles if autointake is enabled
     */
    public void toggleAutoIntake() {
        autoIntakeEnabled = !autoIntakeEnabled;
    }

    /**
     * Used to cycle through the amount of balls in the intake. Useful for debug
     * <p>Increments by 1 in a range of 0-3
     */
    public void cycleBallCount() {
        magBallCount++;

        if (magBallCount == 4) {
            magBallCount = 0;
        }
    }
}
