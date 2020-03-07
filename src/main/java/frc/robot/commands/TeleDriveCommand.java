package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlSubsystem;
import frc.robot.subsystems.DriveSubsystem;

public class TeleDriveCommand extends CommandBase {
    private DriveSubsystem drive;
    private ControlSubsystem controls;

    public TeleDriveCommand(DriveSubsystem driveIn, ControlSubsystem controlsIn) {
        super();
        drive = driveIn;
        addRequirements(drive);
        controls = controlsIn;
    }

    @Override
    public void execute() {
        drive.getDriveBase().arcadeDrive(-controls.getYAxis(), controls.getZAxis(), true);
    }
}