package frc.robot.commands;

import com.kauailabs.navx.IMUProtocol;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;

import static frc.robot.Constants.MAX_SPEED;

public class LimelightFacer extends CommandBase {
    private Limelight lime;
    private DriveSubsystem driveSubsystem;
    private PIDController pidLeft;
    private PIDController pidRight;

    private PIDController pidRot;

    public LimelightFacer(Limelight limeIn, DriveSubsystem driveSubsystemIn) {
        super();
        lime = limeIn;
        driveSubsystem = driveSubsystemIn;
        addRequirements(driveSubsystem);
        pidLeft = new PIDController(1e-3, 0, 0);
        pidRight = new PIDController(1e-3, 0, 0);
        pidRot = new PIDController(1 / 30.0, 0, 0);
    }

    private static final SimpleMotorFeedforward feed = new SimpleMotorFeedforward(0, 1 / MAX_SPEED);
    private static final SimpleMotorFeedforward rotFeed = new SimpleMotorFeedforward(0.2, 0);

    private boolean isDone = false;

    @Override
    public void execute() {
        /*
        // CW rotation speed
        double ang = lime.hasTarget() ? lime.getHAngle() : -27;
        if (Math.abs(ang) < 1) {
            isDone = true;
        } else {
            double rot = pidRot.calculate(ang);
        }
        double speed = pid.calculate(ang);
        if (pid.atSetpoint()) {
            //isDone = true;
        } else {
            speed = feed.calculate(speed);
            driveSubsystem.getDriveBase().tankDrive(speed, -speed);
        }
         */
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}