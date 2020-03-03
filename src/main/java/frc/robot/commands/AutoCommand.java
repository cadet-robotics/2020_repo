package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.greeneva.Limelight;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoCommand extends SequentialCommandGroup {
    public AutoCommand(Limelight lime, DriveSubsystem drive, ShooterSubsystem shooter) {
        super(new SkitterCommand(drive), new RotateToLightCommand(lime, drive), new SetShooterSpeedCommand(shooter, 2000, 5));
    }
}