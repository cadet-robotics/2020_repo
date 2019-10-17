package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.subsystems.ElevatorSubsystem;

/**
 * Command that moves the elevator to a certain position
 * 
 * @author Alex Pickering
 */
public class ElevatorCommand extends Command {
	static final double SPEED = 0.45;
	
	int currentPosition,
		toPosition,
		startPosition;
	
	Robot nexus;
	
	/**
	 * Default constructor
	 * <p>Moves the elevator to the given position
	 * 
	 * @param position The position to move to
	 * @param startPos The position the elevator started in
	 * @param nexus The robot instance
	 */
	public ElevatorCommand(int position, int startPosition, Robot nexus) {
		super("ElevatorCommand");
		requires(ElevatorSubsystem.getInstance());
		
		this.nexus = nexus;
		this.startPosition = startPosition;
		currentPosition = startPosition;
		toPosition = position;
	}
	
	@Override
	protected void initialize() {
		nexus.setElevatorRunning(true);
	}
	
	@Override
	protected void execute() {
		System.out.print("ELEVATOR FROM " + startPosition + " TO " + toPosition + " AT " + currentPosition);
		
		//Find current position
		find: {
			//Look through each proximity sensor
			for(int i = 0; i < nexus.getSensors().elevatorSensors.length; i++) {
				if(nexus.getSensors().elevatorSensors[i].get()) {
					currentPosition = i;
					
					//Skip the 'not found' part without needing to check currentPosition
					break find;
				}
			}
			
			//Not found, check bottom
			if(nexus.getSensors().bottomLimitSwitch.get()) {
				currentPosition = -1;
			}
		}
		
		if(isFinished()) {
			System.out.println();
			return;
		}
		
		double speed = Robot.ELEVATOR_MAINTENANCE_SPEED;
		
		if(currentPosition > toPosition) { //Move down
			System.out.print(" GOING DOWN");
			speed -= (SPEED / 1.2);
		} else { //Move up
			System.out.print(" GOING UP");
			speed += SPEED;
		}
		
		if(Math.abs(toPosition - currentPosition) == 1)
			speed /= 1.5;
		
		nexus.getMotors().leftElevator.set(-speed);
		nexus.getMotors().rightElevator.set(speed);
		
		System.out.println();
	}
	
	@Override
	protected boolean isFinished() {
		return currentPosition == toPosition;
	}
	
	@Override
	protected void end() {
		System.out.println("ELEVATOR FINISHED AT " + currentPosition);
		nexus.getMotors().leftElevator.set(0);
		nexus.getMotors().rightElevator.set(0);
		nexus.setElevatorRunning(false);
	}
}
