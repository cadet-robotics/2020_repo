package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Sensors;
import frc.robot.subsystems.DriveSubsystem;

public class RotateToCommand extends CommandBase {
    private static final SimpleMotorFeedforward feed = new SimpleMotorFeedforward(0.1, 0.3);

    private DriveSubsystem drive;
    private PIDController pid;
    private double angleTarget = Double.NaN;
    private double angle;

    public RotateToCommand(double angleIn, DriveSubsystem driveIn) {
        super();
        pid = new PIDController(2e-1, 5e-2, 0);
        angle = angleIn;
        drive = driveIn;
        addRequirements(driveIn);
    }

    @Override
    public void execute() {
        if (Double.isNaN(angleTarget)) {
            angleTarget = Sensors.getGyro() + angle;
        }
        double r = feed.calculate(pid.calculate(Sensors.getGyro(), angleTarget));
        SmartDashboard.putNumber("pid-val", r);
        r = Math.min(r, 0.5);
        drive.getDriveBase().tankDrive(-r, r, false);
    }

    @Override
    public boolean isFinished() {
        return pid.atSetpoint();
    }
}