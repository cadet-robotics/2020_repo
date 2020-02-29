package frc.robot.commands;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SkitterCommand extends SequentialCommandGroup {
    private static final Trajectory TRAJECTORY_AGREEMENT;
    static {
        /*
        Trajectory tt;
        try {
            tt = TrajectoryUtil.fromPathweaverJson(new File(Filesystem.getDeployDirectory(), "paths/output/Unnamed.wpilib.json").toPath());
        } catch (IOException e) {
            tt = null;
            e.printStackTrace();
        }
        TRAJECTORY_AGREEMENT = tt;
        */
        ArrayList<Pose2d> ls = new ArrayList<>();
        ls.add(new Pose2d());
        ls.add(new Pose2d(new Translation2d(1, 0), new Rotation2d()));
        ls.add(new Pose2d(new Translation2d(3, 2), Rotation2d.fromDegrees(210)/*new Rotation2d()*/));
        TRAJECTORY_AGREEMENT = TrajectoryGenerator.generateTrajectory(ls, new TrajectoryConfig(1, 0.7));
    }

    public SkitterCommand(Trajectory t, DriveSubsystem drive) {
        addCommands(drive.trajectoryCommandBuilder(t, new Pose2d()));
    }

    public SkitterCommand(DriveSubsystem drive) {
        this(TRAJECTORY_AGREEMENT, drive);
    }
}