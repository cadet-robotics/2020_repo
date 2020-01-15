package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;

public class DriveSubsystem extends SubsystemBase {
    private RobotDriveBase driveBase;

    public DriveSubsystem(RobotDriveBase driveBaseIn) {
        driveBase = driveBaseIn;
    }

    public RobotDriveBase getDriveBase() {
        return driveBase;
    }
}