package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;

public class LimelightFacer extends CommandBase {
    private Limelight lime;
    private DriveSubsystem driveSubsystem;
    private PIDController pid;

    public LimelightFacer(Limelight limeIn, DriveSubsystem driveSubsystemIn) {
        super();
        lime = limeIn;
        driveSubsystem = driveSubsystemIn;
        addRequirements(driveSubsystem);
        pid = new PIDController(1 / 20.0, 0, 0);
        pid.setSetpoint(0);
    }

    private static final SimpleMotorFeedforward feed = new SimpleMotorFeedforward(0.4, 0.8);

    private boolean isDone = false;

    @Override
    public void execute() {
        // CW rotation speed
        double ang = lime.hasTarget() ? lime.getHAngle() : -27;
        double speed = pid.calculate(ang);
        if (pid.atSetpoint()) {
            //isDone = true;
        } else {
            speed = feed.calculate(speed);
            driveSubsystem.getDriveBase().tankDrive(speed, -speed);
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}