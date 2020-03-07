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
    
    private boolean motorRunning = false;
    
    /**
     * uwu
     */
    public WinchSubsystem() {
        super();
    }
    
    /**
     * Locks or unlocks the winch
     * 
     * @param locked
     */
    public void setLockedState(boolean locked) {
        // Slap the pneumatics in place
        if(locked) {
            OtherIO.lockingPiston.set(Value.kReverse);
        } else {
            OtherIO.lockingPiston.set(Value.kForward);
        }
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
