package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.io.Motors;
import frc.robot.io.OtherIO;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value;


/**
 * Controls the climbing winch
 * 
 * @author Alex Pickering
 */
public class WinchSubsystem extends SubsystemBase {
    
    private static final Value LOCKED = Value.kForward,
                               UNLOCKED = Value.kReverse;
    
    private boolean motorRunning = false;
    
    private Value currentValue;
    
    /**
     * uwu
     */
    public WinchSubsystem() {
        super();
        
        currentValue = UNLOCKED;
        OtherIO.lockingPiston.set(currentValue);
    }
    
    /**
     * Locks or unlocks the winch
     * 
     * @param locked
     */
    public void setLockedState(boolean locked) {
        // Slap the pneumatics in place
        if(locked) {
            currentValue = LOCKED;
        } else {
            currentValue = UNLOCKED;
        }
        
        OtherIO.lockingPiston.set(currentValue);
    }
    
    /**
     * Toggles if the winch is locked
     */
    public void toggleLockedState() {
        if(currentValue == LOCKED || currentValue == Value.kOff) {
            currentValue = UNLOCKED;
        } else {
            currentValue = LOCKED;
        }
        
        OtherIO.lockingPiston.set(currentValue);
    }
    
    /**
     * Runs the winch up
     */
    public void runUp() {
        Motors.winch.set(Constants.WINCH_UP_SPEED);
        motorRunning = true;
    }
    
    /**
     * Runs the winch down
     */
    public void runDown() {
        Motors.winch.set(Constants.WINCH_DOWN_SPEED);
        motorRunning = true;
    }
    
    @Override
    public void periodic() {
        // Reset motors if they don't have something to do
        if(!motorRunning) {
            Motors.winch.set(0);
        }
        
        // I think this would work??
        motorRunning = false;
    }
}
