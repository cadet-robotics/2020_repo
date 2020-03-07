package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;

public class RotateWheelCountChangesCommand extends CommandBase {
    private ArmSubsystem arm;
    private int changes;

    private static final double SPEED = 0.2;

    public RotateWheelCountChangesCommand(ArmSubsystem armIn, int changesIn) {
        super();
        arm = armIn;
        addRequirements(arm);
        changes = changesIn;
    }

    public RotateWheelCountChangesCommand(ArmSubsystem arm) {
        this(arm, 26);
    }

    private int old = Integer.MIN_VALUE;

    @Override
    public void execute() {
        if (old == Integer.MIN_VALUE) {
            old = arm.getColorWheel().getCount();
        }
        int cnt = arm.getColorWheel().getCount() - old;
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

    @Override
    public void end(boolean interrupted) {
        arm.stopWheelSpinner();
    }
}