package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;

public class LimelightFacer extends CommandBase {
    private Limelight lime;
    private DriveSubsystem driveSubsystem;

    public LimelightFacer(Limelight limeIn, DriveSubsystem driveSubsystemIn) {
        super();
        lime = limeIn;
        driveSubsystem = driveSubsystemIn;
        addRequirements(driveSubsystem);
    }

    private static final SimpleMotorFeedforward feed = new SimpleMotorFeedforward(0.2, 0.8);

    private boolean isDone = false;

    @Override
    public void execute() {
        // CCW rotation speed
        double speed = lime.hasTarget() ? lime.getHAngle() : -27;
        if (speed > 1) {
            speed /= 27;
            speed *= 0.75;
            speed = feed.calculate(speed);
            driveSubsystem.getDriveBase().tankDrive(-speed, speed);
        } else {
            isDone = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}