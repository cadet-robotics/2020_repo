package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

import java.util.ArrayList;

public class SkitterCommand extends SequentialCommandGroup {
    public static final TrajectoryConfig config;
    static {
        config = new TrajectoryConfig(0.8, 0.7);
        config.addConstraint(new CentripetalAccelerationConstraint(0.15));
    }

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
        //ls.add(new Pose2d(new Translation2d(0.5, 0), Rotation2d.fromDegrees(45)));
        //ls.add(new Pose2d(new Translation2d(0.5, 2), new Rotation2d()));
        //ls.add(new Pose2d(new Translation2d(0.8, 2), Rotation2d.fromDegrees(90)));
        ls.add(new Pose2d(new Translation2d(0.5, 2), Rotation2d.fromDegrees(180)/*new Rotation2d()*/));
        TRAJECTORY_AGREEMENT = TrajectoryGenerator.generateTrajectory(ls, config);
    }

    public SkitterCommand(Trajectory t, DriveSubsystem drive) {
        super();
        addCommands(drive.trajectoryCommandBuilder(t, new Pose2d()));
    }

    public SkitterCommand(DriveSubsystem drive) {
        this(TRAJECTORY_AGREEMENT, drive);
    }
}