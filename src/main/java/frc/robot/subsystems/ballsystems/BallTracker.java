package frc.robot.subsystems.ballsystems;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Tracks the balls in the magazine
 * Basically all of this is subject to change based on how the robot ends up being constructed
 * 
 * idk what to do i need a robot to figure this out
 * 
 * @author Alex Pickering
 */
public class BallTracker {
    
    private DigitalInput top,
                         middle,
                         bottom;
    
    private int ballCount = 0; // Number of balls in the magazine itself
    
    private boolean previousTopState = false,
                    previousMiddleState = false;
    
    public BallTracker() {
        
    }
    
    /**
     * Returns if there is a ball in the highest position, ready to be fired
     * 
     * @return True if there is a ball in the top position
     */
    public boolean readyToFire() {
        return top.get();
    }
    
    /**
     * Returns if a ball is being succ'd in by the intake
     * 
     * @return True if there is a ball in the top position
     */
    public boolean gettingBall() {
        return bottom.get();
    }
    
    /**
     * Gets the number of balls in the magazine
     * 
     * @return The number of b a l l
     */
    public int getBallCount() {
        return ballCount;
    }
    
    /**
     * Updates the state based on the sensors
     */
    public void update() {
        boolean t = top.get(),
                m = middle.get(),
                b = bottom.get();
        
        // there's a new ball in the mag
        if(m && !previousMiddleState) {
            ballCount++;
        }
        previousMiddleState = m;
        
        // a ball left the shooter
        if(!t && previousTopState) {
            ballCount--;
        }
        previousTopState = t;
    }
}
