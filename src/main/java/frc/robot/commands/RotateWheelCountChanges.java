package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;

public class RotateWheelCountChanges extends CommandBase {
    private ArmSubsystem arm;
    private int changes;

    private static final double SPEED = 0.2;

    public RotateWheelCountChanges(ArmSubsystem armIn, int changesIn) {
        arm = armIn;
        addRequirements(arm);
        changes = changesIn;
    }

    public RotateWheelCountChanges(ArmSubsystem arm) {
        this(arm, 26);
    }

    @Override
    public void execute() {
        int cnt = arm.getColorWheel().getCount();
        if (cnt >= changes) {
            arm.stopWheelSpinner();
        } else {
            arm.activateWheelSpinner(SPEED);
        }
    }

    @Override
    public boolean isFinished() {
        return arm.isWheelSpinnerStopped();
    }
}