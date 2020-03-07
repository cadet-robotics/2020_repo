package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import static frc.robot.trajectory.TrajectoryManager.BASE_TRAJECTORY;

public class AutoCommand extends SequentialCommandGroup {
    public AutoCommand(Limelight lime, DriveSubsystem drive, ShooterSubsystem shooter, PickupSubsystem pick) {
        super(drive.trajectoryCommandBuilder(BASE_TRAJECTORY, new Pose2d()), new RotateToLightCommand(lime, drive), new WaitCommand(0.5), FDriverFactory.produce(shooter, pick, lime::getDistance, false));
    }
}