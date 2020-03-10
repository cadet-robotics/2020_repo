package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.trajectory.TrajectoryManager;

public class AutoCommand extends SequentialCommandGroup {
    public AutoCommand(Limelight lime, DriveSubsystem drive, ShooterSubsystem shooter, PickupSubsystem pick, TrajectoryManager.StartPos pos) {
        super(drive.trajectoryCommandBuilder(pos.getTrajectory(), new Pose2d(new Translation2d(0, (1 - pos.getIndex()) * 1.8), new Rotation2d())), new RotateToLightCommand(lime, drive), new WaitCommand(0.5), FDriverFactory.produce(shooter, pick, lime::getDistance, false));
    }
}