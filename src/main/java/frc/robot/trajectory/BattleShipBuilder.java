package frc.robot.trajectory;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

import java.util.ArrayList;

/*
    This interface models the robot as being in the center of a tile,
    while it is actually half a tile ahead of the center of the tile it's on

    Interface:
    *-----*
    |     |
    |  ^  |
    |     |
    *-----*
    Reality:
    *--^--*
    |     |
    |     |
    |     |
    *-----*

    This is used to build the list of robot translations more effectively
    The private code here should handle finishing and ending on the center of the desired tile
    Code using the interface can pretend that the interface is telling the truth,
    as it *basically* is
     */
public class BattleShipBuilder {
    private static double MV_FACTOR = 1.0;

    private enum MovementRaw {
        HALF_FORWARD(0.5 * MV_FACTOR, 0),
        FORWARD(MV_FACTOR, 0),
        LEFT(0.5 * MV_FACTOR, 0.5 * MV_FACTOR),
        RIGHT(0.5 * MV_FACTOR, -0.5 * MV_FACTOR);

        private final Translation2d translate;

        MovementRaw(double x, double y) {
            translate = new Translation2d(x, y);
        }
    }

    private enum Movement {
        // moves robot to tile in front of it
        FORWARD(MovementRaw.FORWARD),
        // moves robot to tile in front of it, then turns left
        FORWARD_LEFT(MovementRaw.LEFT),
        // moves robot to tile in front of it, then turns right
        FORWARD_RIGHT(MovementRaw.RIGHT);

        private final MovementRaw inner;

        Movement(MovementRaw raw) {
            inner = raw;
        }
    }

    private final ArrayList<Translation2d> mvList;

    public BattleShipBuilder() {
        mvList = new ArrayList<>();
        mvList.add(MovementRaw.HALF_FORWARD.translate);
    }

    private double curX = 0, curY = 0;
    private int rX = 1, rY = 0;

    private int df = 0;
    private boolean isStart = true;

    public void applyMovement(Movement mv) {
        switch (mv) {
            case FORWARD:
                df += 1;
                break;
            case FORWARD_LEFT:
                if ((df != 0) || isStart) {
                    double nextX = curX + rX * (df + (isStart ? 0.5 : 0)) * MV_FACTOR;
                    double nextY = curY + rY * (df + (isStart ? 0.5 : 0)) * MV_FACTOR;
                }
        }
        mvList.add(mv.inner.translate);
    }

    /*
    Ends the movement sequence on the tile strait in front of the robot
    Needed because of abstractions
     */
    public Trajectory endAhead() {
        mvList.add(MovementRaw.HALF_FORWARD.translate);
        return TrajectoryGenerator.generateTrajectory(initialPos, ls, end, new TrajectoryConfig(1.0, 0.5));
    }

    public Trajectory finishRaw(int nx, int ny, Rotation2d end) {

    }
}