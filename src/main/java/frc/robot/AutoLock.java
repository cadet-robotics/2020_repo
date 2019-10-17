package frc.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.io.Controls;
import frc.robot.io.Drive;
import frc.robot.sensors.Sensors;
import frc.robot.sensors.SightData;

/**
 * Controls motor PID loop for some auto-y stuff
 * <p>Javadoc comments lovingly provided by Alex Pickering
 * 
 * @author Owen Avery
 */
public class AutoLock extends Command {
	public static final double DEFAULT_TOLERANCE_POS = 1;
    
    //private int rotTarget;
    //private int xTarget;
    //private int yTarget;

    public static final double TURN_P = 0.02;
	public static final double TURN_I = 0;
	public static final double TURN_D = 0;

	public static final double TURN_DEFAULT_TOLERANCE = 1;
	
	public static final double POS_P = 0.02;
	public static final double POS_I = 0;
	public static final double POS_D = 0;

	public static final double MIN_MOTOR_SPEED = 0.4;
	//public static final double MAX_MOTOR_SPEED = 0.65;

	public Drive driveSystem;

	public Sensors sensors;

	public SightData sight;

	public Controls controls;

	private Double posChangeX = (double) 0, posChangeY = (double) 0, rotChange = (double) 0;
	
	private PIDController pidPosX = new PIDController(POS_P, POS_I, POS_D, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			return /*-sight.getXOffset()*/0;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	}, output -> {
		output = clampAbs(output, MIN_MOTOR_SPEED, Double.POSITIVE_INFINITY);
		
		synchronized (posChangeX) {
			posChangeX = output * 1.5;
		}
	});
	
	private PIDController pidPosY = new PIDController(POS_P, POS_I, POS_D, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			return /*-sight.getYOffset()*/0;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	}, output -> {
		output = clampAbs(output, MIN_MOTOR_SPEED, Double.POSITIVE_INFINITY);
		
		synchronized (posChangeY) {
			posChangeY = output * 1.5;
		}
	});
	
	private PIDController pidRot = new PIDController(TURN_P, TURN_I, TURN_D, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			double d = JavaIsCancerChangeMyMind.moduloIsCancer(sensors.gyro.getAngle(), 360);
			SmartDashboard.putNumber("rot", d);
			return d;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	}, output -> {
		synchronized (rotChange) {
			rotChange = output;
		}
	});
	
	/**
	 * Default constructor
	 * 
	 * @param nexus A Nexus instance
	 */
	public AutoLock(Nexus nexus) { // Moves the robot forward/backward
		//requires(DriveSubsystem.getInstance());
		//OI.leftEncoder.reset();
		//OI.rightEncoder.reset();
		//pidPos.setSetpoint(dist);
		////pidPos.setInputRange(-Math.abs(dist * 2), Math.abs(dist * 2));

		controls = nexus.getControls();
		driveSystem = nexus.getDriveSystem();
		sight = nexus.getSightData();
		sensors = nexus.getSensors();

		pidPosX.setSetpoint(0);
		pidPosY.setSetpoint(0);
		pidPosX.setAbsoluteTolerance(DEFAULT_TOLERANCE_POS);
		pidPosX.enable();
		pidPosY.setAbsoluteTolerance(DEFAULT_TOLERANCE_POS);
		pidPosY.enable();
		
		pidRot.setInputRange(0, 360);
		pidRot.setContinuous();
		double d = JavaIsCancerChangeMyMind.moduloIsCancer(sensors.gyro.getAngle(), 360);
		pidRot.setSetpoint(d);
		pidRot.setAbsoluteTolerance(TURN_DEFAULT_TOLERANCE);
		pidRot.enable();
    }
    
	/**
	 * Sets the offset for angles
	 * 
	 * @param off The offset
	 */
    public void setAngleOffset(double off) {
        double d = JavaIsCancerChangeMyMind.moduloIsCancer(sensors.gyro.getAngle(), 360);
		pidRot.setSetpoint(JavaIsCancerChangeMyMind.moduloIsCancer(d + off, 360));
    }
    
    /**
     * Gets whether or not autolock is enabled
     * 
     * @return Autolock enabled
     */
	public boolean isEnabled() {
    	return controls.isAutoLock();// && !sight.isTimeout();
	}
	
	@Override
	public void execute() {
    	super.execute();
		synchronized (posChangeX) {
			synchronized (posChangeY) {
			    synchronized (rotChange) {
				////System.out.println(posChange + ", " + rotChange);
				//double l = clampAbs(rotChange + posChange, 0, OI.MAX_MOTOR_SPEED);
				//double r = clampAbs(rotChange - posChange, 0, OI.MAX_MOTOR_SPEED);
				////System.out.println("left: " + l);
				////System.out.println("right: " + r);
				//OI.leftMotor.set(l);
                //OI.rightMotor.set(r);
                    if (isEnabled()) driveSystem.driveCartesian(posChangeY, posChangeX, rotChange);
			    }
		    }
		}
	}
	
	/**
	 * Maps numbers somehow
	 * <p>owen could explain better if he used javadoc comments
	 * 
	 * @param n The number to map
	 * @param low The low end of the clamping range
	 * @param high The high end of the clamping range
	 * @return The 'clamped' number
	 */
	public static double clamp(double n, double low, double high) {
		if (low > high) {
			double d = low;
			low = high;
			high = d;
		}
		return Math.min(Math.max(n, low), high);
	}
	
	/**
	 * Version of the clamp thing that respeccs negatives
	 * <p>owen pls
	 * 
	 * @param n The number to 'clamp'
	 * @param low The low end of the range
	 * @param high The high end of the range
	 * @return The 'clamped' number
	 */
	public static double clampAbs(double n, double low, double high) {
		if (n < 0) {
			return -clamp(-n, high, low);
		} else {
			return clamp(n, low, high);
		}
	}

	@Override
	protected boolean isFinished() {
        //return pidPos.onTarget();// && pidRot.onTarget();
        return false;
	}
}