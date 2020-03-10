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

    public static final Trajectory[] TRAJECTORIES = new Trajectory[3];
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
        for (int i = 0; i < 3; i++) {
            ArrayList<Pose2d> ls = new ArrayList<>();
            ls.add(new Pose2d());
            //ls.add(new Pose2d(new Translation2d(-1, 1), Rotation2d.fromDegrees(90)));
            //ls.add(new Pose2d(new Translation2d(0.5, 2), new Rotation2d()));
            //ls.add(new Pose2d(new Translation2d(0.8, 2), Rotation2d.fromDegrees(90)));
            /*
            if (i == 0) {
                ls.add(new Pose2d(new Translation2d(1, 1), Rotation2d.fromDegrees(-90)));
            } else if (i == 2) {
                ls.add(new Pose2d(new Translation2d(1, 1), Rotation2d.fromDegrees(90)));
            }
             */
            ls.add(new Pose2d(new Translation2d(1, 2 /*+ (i - 1) * 1.8288*/), Rotation2d.fromDegrees(180)/*new Rotation2d()*/));
            TRAJECTORIES[i] = TrajectoryGenerator.generateTrajectory(ls, config);
        }
    }

    public enum StartPos {
        Left,
        Center,
        Right;

        public int getIndex() {
            switch (this) {
                case Left: return 0;
                case Center: return 1;
                case Right: return 2;
                default: throw new IllegalArgumentException("invalid start pos");
            }
        }

        public Trajectory getTrajectory() {
            return TRAJECTORIES[getIndex()];
        }
    }
}