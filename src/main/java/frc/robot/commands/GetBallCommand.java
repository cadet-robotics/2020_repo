package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot;
import frc.robot.commands.subsystems.ElevatorSubsystem;

/**
 * Gets the ball from the ground
 * 
 * @author Alex Pickering
 */
public class GetBallCommand extends Command {
	final boolean STATE_MOVING = true,
				  STATE_GETTING = false;
	
	boolean state = STATE_MOVING,
			ending = false;
	
	Robot nexus;
	
	/**
	 * Default constructor
	 * <p>Moves the elevator to the ground and gets a ball
	 * 
	 * @param nexus The robot instance
	 */
	public GetBallCommand(Robot nexus) {
		super("GetBallCommand");
		requires(ElevatorSubsystem.getInstance());
		
		this.nexus = nexus;
	}
	
	@Override
	protected void initialize() {
		//Take elevator and claw
		nexus.setElevatorRunning(true);
		nexus.setClawRunning(true);
		
		//Make sure claw is open
		nexus.getPneumatics().clawSolenoid.set(Robot.CLAW_OPEN);
	}
	
	@Override
	protected void execute() {
		//Set state
		if(nexus.getSensors().bottomLimitSwitch.get()) {
			state = STATE_GETTING;
		} else {
			state = STATE_MOVING;
		}
		
		//run stuff
		if(state == STATE_MOVING) {
			System.out.println("MOVING DOWN");
			
			//Move elevator down
			double speed = (Robot.ELEVATOR_MAINTENANCE_SPEED - ElevatorCommand.SPEED) / 6;
			
			nexus.getMotors().leftElevator.set(-speed);
			nexus.getMotors().rightElevator.set(speed);
			
			//Make sure claw isn't moving
			nexus.getMotors().leftClaw.set(0);
			nexus.getMotors().rightClaw.set(0);
		} else {
			System.out.println("GETTING BALL");
			
			//Make sure elevator isn't moving
			nexus.getMotors().leftElevator.set(-Robot.ELEVATOR_MAINTENANCE_SPEED);
			nexus.getMotors().rightElevator.set(Robot.ELEVATOR_MAINTENANCE_SPEED);
			
			//Run claw in
			double speed = Robot.CLAW_WHEEL_SPEED;
			
			nexus.getMotors().leftClaw.set(Robot.GET_BALL_DIRECTION ? -speed : speed);
			nexus.getMotors().rightClaw.set(Robot.GET_BALL_DIRECTION ? speed : -speed);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return ending;
	}
	
	@Override
	protected void end() {
		nexus.getMotors().leftElevator.set(0);
		nexus.getMotors().rightElevator.set(0);
		nexus.getMotors().leftClaw.set(0);
		nexus.getMotors().rightClaw.set(0);
		nexus.setElevatorRunning(false);
		nexus.setClawRunning(false);
	}
}
