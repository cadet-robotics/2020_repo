package frc.robot.trajectory;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

import java.util.ArrayList;

public class BattleShipBuilder {
    private static double MV_FACTOR = 1.0;

    // Coordinate system origin is at bottom left of grid (A1) (0, 0)
    // x is letter, y is number
    private int x;
    private int y;
    private Pose2d initialPos;
    private ArrayList<Translation2d> ls;

    public BattleShipBuilder() {
        ls = new ArrayList<>();
        x = 2;
        y = 0;
        initialPos = new Pose2d(x, y, Rotation2d.fromDegrees(90));
    }

    public void addRaw(int nx, int ny) {
        ls.add(new Translation2d(MV_FACTOR * (nx - x), MV_FACTOR * (ny - y)));
        x = nx;
        y = ny;
    }

    public Trajectory finishRaw(int nx, int ny, Rotation2d end) {
        return TrajectoryGenerator.generateTrajectory(initialPos, ls, end, new TrajectoryConfig(1.0, 0.5));
    }
}
