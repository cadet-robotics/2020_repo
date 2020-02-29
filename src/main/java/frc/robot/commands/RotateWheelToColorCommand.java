package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.wheel.ColorEnum;

public class RotateWheelToColorCommand extends CommandBase {
    private ArmSubsystem arm;
    private ColorEnum target;

    public RotateWheelToColorCommand(ArmSubsystem armIn, ColorEnum targetIn) {
        super();
        arm = armIn;
        addRequirements(arm);
        target = targetIn;
    }

    @Override
    public void execute() {
        if (arm.getColorWheel().getColor() == target) {
            arm.stopWheelSpinner();
        } else {
            arm.activateWheelSpinner(0.15);
        }
    }

    @Override
    public boolean isFinished() {
        return arm.getColorWheel().getColor() == target && arm.isWheelSpinnerStopped();
    }
}