package frc.robot.io;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * C/P of Alex's motor pair, made for the drive train (3 motors now...)
 *
 * A representation of two motors as if they are one
 * This is better than a SpeedControllerGroup because it can invert motors separately
 *
 * @author Matthew Robinson
 * @author Alex Pickering
 */
public class MotorGroup implements SpeedController {

    private SpeedController motorA, motorB, motorC;

    public MotorGroup(SpeedController motorA, SpeedController motorB, SpeedController motorC) {
        this.motorA = motorA;
        this.motorB = motorB;
        this.motorC = motorC;
    }

    /**
     * Sets if Motor A is direction-inverted independently of Motor B
     *
     * @param isInverted
     */
    public void setAInverted(boolean isInverted) {
        motorA.setInverted(isInverted);
    }

    /**
     * Sets if Motor B is direction-inverted independently of Motor B
     *
     * @param isInverted
     */
    public void setBInverted(boolean isInverted) {
        motorB.setInverted(isInverted);
    }

    /**
     * Sets if Motor C is direction-inverted independently of Motor B
     *
     * @param isInverted
     */
    public void setCInverted(boolean isInverted) {
        motorC.setInverted(isInverted);
    }

    /**
     * Returns if Motor A is direction-inverted
     *
     * @return True if inverted
     */
    public boolean getAInverted() {
        return motorA.getInverted();
    }

    /**
     * Returns if Motor B is direction-inverted
     *
     * @return True if inverted
     */
    public boolean getBInverted() {
        return motorB.getInverted();
    }

    /**
     * Returns if Motor C is direction-inverted
     *
     * @return True if inverted
     */
    public boolean getCInverted() {
        return motorC.getInverted();
    }

    // We have to implement deprecated stuff because interfaces
    @Override
    @SuppressWarnings({"deprecation","removal"})
    public void pidWrite(double output) {
        motorA.pidWrite(output);
        motorB.pidWrite(output);
        motorC.pidWrite(output);
    }

    @Override
    public void set(double speed) {
        motorA.set(speed);
        motorB.set(speed);
        motorC.set(speed);
    }

    @Override
    public void setVoltage(double outputVolts) {
        motorA.setVoltage(outputVolts);
        motorB.setVoltage(outputVolts);
        motorC.setVoltage(outputVolts);
    }

    @Override
    public double get() {
        return (motorA.get() + motorB.get() + motorC.get()) / 3;
    }

    @Override
    public void setInverted(boolean isInverted) {
        motorA.setInverted(isInverted);
        motorB.setInverted(isInverted);
        motorC.setInverted(isInverted);
    }

    /**
     * Since each motor may be inverted differently, don't use this one!
     */
    @Deprecated
    @Override
    public boolean getInverted() {
        return false;
    }

    @Override
    public void disable() {
        motorA.disable();
        motorB.disable();
        motorC.disable();
    }

    @Override
    public void stopMotor() {
        motorA.stopMotor();
        motorB.stopMotor();
        motorC.stopMotor();
    }


}