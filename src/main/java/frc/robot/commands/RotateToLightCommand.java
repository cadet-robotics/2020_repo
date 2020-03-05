package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;

public class RotateToLightCommand extends CommandBase {
    private static final SimpleMotorFeedforward feed = new SimpleMotorFeedforward(0.25, 0.15);

    private DriveSubsystem drive;
    private PIDController pid;
    private Limelight lime;

    public RotateToLightCommand(Limelight limeIn, DriveSubsystem driveIn) {
        super();
        pid = new PIDController(15e-2, 1e-2, 0);
        drive = driveIn;
        addRequirements(driveIn);
        lime = limeIn;
    }

    private double past = 0.2;

    private static double maxAdjust(double old, double n, double maxDelta) {
        return absMin(n - old, maxDelta) + old;
    }

    private static double absMin(double v1, double clamp) {
        double v1s = Math.signum(v1);
        return Math.min(v1 * v1s, clamp) * v1s;
    }

    @Override
    public void execute() {
        double hAngle = lime.hasTarget() ? lime.getHAngle() : -20;
        double r = feed.calculate(pid.calculate(hAngle, 0));
        SmartDashboard.putNumber("pid-val", r);
        r = absMin(r, 0.45);
        r *= 12 / RobotController.getBatteryVoltage();
        past = r;//maxAdjust(past, r, 1);
        SmartDashboard.putNumber("past", past);
        drive.getDriveBase().tankDrive(-past, past, false);
    }

    @Override
    public boolean isFinished() {
        return pid.atSetpoint();
    }
}