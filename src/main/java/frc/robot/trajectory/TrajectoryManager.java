package frc.robot.trajectory;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj.util.Units;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
    /*
    private static final TrajectoryConfig config;
    static {
        config = new TrajectoryConfig(0.5, 1000);
        //config.addConstraint(new CentripetalAccelerationConstraint(0.15));
    }

    private static final double MV_FACTOR = 1.0;
    public static Translation2d getPosFromLabel(String label) {
        double y;
        switch (label.charAt(0)) {
            case 'A':
                y = 0;
                break;
            case 'B':
                y = -MV_FACTOR;
                break;
            case 'C':
                y = -MV_FACTOR * 2;
                break;
            case 'D':
                y = -MV_FACTOR * 3;
                break;
            case 'E':
                y = -MV_FACTOR * 4;
                break;
            case 'F':
                y = -MV_FACTOR * 5;
                break;
            default:
                throw new IllegalArgumentException("unexpected letter");
        }
        double x = Integer.parseInt(label.substring(1)) * MV_FACTOR;
        return new Translation2d(x, y);
    }
    public static final Translation2d LOOP_POS = getPosFromLabel("C1");
    public static final Pose2d INITIAL_POS = new Pose2d(LOOP_POS, new Rotation2d(0));

    public static ArrayList<Translation2d> getPosListFromString(String s) {
        String[] s_list = s.split(",");
        ArrayList<Translation2d> ret = new ArrayList<>(s_list.length);
        for (String sub : s_list) {
            ret.add(getPosFromLabel(sub));
        }
        return ret;
    }
     */

    private static Trajectory loadTrajectory(String s) {
        try {
            return TrajectoryUtil.fromPathweaverJson(Filesystem.getDeployDirectory().toPath().resolve("paths/" + s + ".wpilib.json"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final Trajectory BARREL_TRAJECTORY = loadTrajectory("barrel");
    public static final Trajectory SLALOM_TRAJECTORY = loadTrajectory("slalom");
    public static final Trajectory[] BOUNCE_TRAJECTORY_LIST =
            new Trajectory[] {
                    loadTrajectory("bounce-1"),
                    loadTrajectory("bounce-2"),
                    loadTrajectory("bounce-3"),
                    loadTrajectory("bounce-4")
            };

    /*
    public static final Trajectory BASE_TRAJECTORY;
    static {
        ArrayList<Translation2d> ls =
                //getPosListFromString("C5,D6,E5,D4,C5,C8,B9,A8,B7,C8,E10,D11,C10");
                getPosListFromString("C5,D6,E5,D4,C5");
        Pose2d start = new Pose2d(LOOP_POS, new Rotation2d(0));
        //Pose2d end = new Pose2d(LOOP_POS, Rotation2d.fromDegrees(180));
        Pose2d end = new Pose2d(getPosFromLabel("C8"), new Rotation2d(0));
        BASE_TRAJECTORY = TrajectoryGenerator.generateTrajectory(start, ls, end, config);
        /*
        Pose2d start = INITIAL_POS;
        ArrayList<Translation2d> ls = getPosListFromString("C2");
        Pose2d end = new Pose2d(getPosFromLabel("E3"), Rotation2d.fromDegrees(270));
        BASE_TRAJECTORY = TrajectoryGenerator.generateTrajectory(start, ls, end, config);
         *\/
    }
    */
}