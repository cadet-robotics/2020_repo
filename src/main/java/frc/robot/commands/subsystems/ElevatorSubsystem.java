package frc.robot.commands.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem for the elevator
 * <p>Javadocs lovingly provided by Alex Pickering
 * 
 * @author Owen Avery
 */
public class ElevatorSubsystem extends Subsystem {
    private static ElevatorSubsystem instance = null;
    
    /**
     * Default constructor
     */
    private ElevatorSubsystem() {
    }
    
    /**
     * Gets the instance of the subsystem
     * 
     * @return The instance
     */
    public static ElevatorSubsystem getInstance() {
        if (instance == null) instance = new ElevatorSubsystem();
        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(null);
    }
}