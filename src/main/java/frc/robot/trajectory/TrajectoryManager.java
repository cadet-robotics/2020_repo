package frc.robot.trajectory;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;

import java.util.ArrayList;

/**
 * Stores trajectories
 * Currently, factories are generated statically without pathweaver
 *
 * -----
 *
 * DESTINATION
 * AGREEMENT
 * TRAJECTORY
 * AGREEMENT
 */
public class TrajectoryManager {
    private static final TrajectoryConfig config;
    static {
        config = new TrajectoryConfig(1.2, 0.5);
        //config.addConstraint(new CentripetalAccelerationConstraint(0.15));
    }

    public static final Trajectory BASE_TRAJECTORY;
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
        //ls.add(new Pose2d(new Translation2d(-1, 1), Rotation2d.fromDegrees(90)));
        //ls.add(new Pose2d(new Translation2d(0.5, 2), new Rotation2d()));
        //ls.add(new Pose2d(new Translation2d(0.8, 2), Rotation2d.fromDegrees(90)));
        ls.add(new Pose2d(new Translation2d(1, 2), Rotation2d.fromDegrees(180)/*new Rotation2d()*/));
        BASE_TRAJECTORY = TrajectoryGenerator.generateTrajectory(ls, config);
    }
}