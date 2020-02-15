package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

import java.io.File;
import java.io.IOException;

public class SkitterCommand extends SequentialCommandGroup {
    private static final Trajectory TRAJECTORY_AGREEMENT;
    static {
        Trajectory tt;
        try {
            tt = TrajectoryUtil.fromPathweaverJson(new File(Filesystem.getDeployDirectory(), "paths/output/Unnamed.wpilib.json").toPath());
        } catch (IOException e) {
            tt = null;
            e.printStackTrace();
        }
        TRAJECTORY_AGREEMENT = tt;
    }

    public SkitterCommand(Trajectory t, DriveSubsystem drive) {
        addCommands(drive.ramseteCommandBuilder(t));
    }

    public SkitterCommand(DriveSubsystem drive) {
        this(TRAJECTORY_AGREEMENT, drive);
    }
}