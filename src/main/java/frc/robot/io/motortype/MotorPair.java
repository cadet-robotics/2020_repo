package frc.robot.io.motortype;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * A representation of two motors as if they are one
 * 
 * @author Alex Pickering
 * @param <T> The type of the motors
 */
public class MotorPair <T extends SpeedController> implements SpeedController {
    
    private T motorA, motorB;
    
    public MotorPair(T motorA, T motorB) {
        this.motorA = motorA;
        this.motorB = motorB;
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
    
    // We have to implement deprecated stuff because interfaces
    @SuppressWarnings("deprecation")
    @Override
    public void pidWrite(double output) {
        motorA.pidWrite(output);
        motorB.pidWrite(output);
    }

    @Override
    public void set(double speed) {
        motorA.set(speed);
        motorB.set(speed);
    }

    @Override
    public double get() {
        double a = motorA.get(),
               b = motorB.get();
        
        // Motors, theoretically, should return the same value
        if(a != b) throw new IllegalStateException(String.format("Motors in pair got different values (%s != %s)", a, b));
        
        return a;
    }

    @Override
    public void setInverted(boolean isInverted) {
        motorA.setInverted(isInverted);
        motorB.setInverted(isInverted);
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
    }

    @Override
    public void stopMotor() {
        motorA.stopMotor();
        motorB.stopMotor();
    }
    
    
}
