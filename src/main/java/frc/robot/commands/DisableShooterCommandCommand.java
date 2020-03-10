package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class DisableShooterCommandCommand extends CommandBase {
    public DisableShooterCommandCommand(ShooterSubsystem shoot) {
        super();
        addRequirements(shoot);
    }

    public static void setEnabled(ShooterSubsystem shoot, boolean isEnabled) {
        if (isEnabled) {
            Command c = shoot.getCurrentCommand();
            if (c instanceof DisableShooterCommandCommand) {
                c.cancel();
            }
        } else {
            new DisableShooterCommandCommand(shoot).schedule();
        }
    }
}