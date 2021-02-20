package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import static frc.robot.trajectory.TrajectoryManager.BASE_TRAJECTORY;
import static frc.robot.trajectory.TrajectoryManager.INITIAL_POS;

public class AutoCommand extends SequentialCommandGroup {
    public AutoCommand(Limelight lime, DriveSubsystem drive, ShooterSubsystem shooter, PickupSubsystem pick) {
        super(drive.trajectoryCommandBuilder(BASE_TRAJECTORY, INITIAL_POS));
    }
}