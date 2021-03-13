package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.trajectory.TrajectoryManager;

public class AutoCommand extends SequentialCommandGroup {
    public AutoCommand(DriveSubsystem drive, PickupSubsystem pick, String type) {
        Trajectory[] tls;
        switch (type) {
            case "BARREL":
                tls = new Trajectory[] {TrajectoryManager.BARREL_TRAJECTORY};
                break;
            case "SLALOM":
                tls = new Trajectory[] {TrajectoryManager.SLALOM_TRAJECTORY};
                break;
            case "BOUNCE":
                tls = TrajectoryManager.BOUNCE_TRAJECTORY_LIST;
                break;
            default:
                throw new RuntimeException("Invalid autonomous");
        }
        addCommands(drive.trajectoryCommandBuilder(tls, tls[0].getInitialPose()));
        addRequirements(pick);
    }
}