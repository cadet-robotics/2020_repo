package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.wheel.ColorChangeTracker;

public class RotateWheelCountChanges extends CommandBase {
    private ArmSubsystem arm;
    private ColorChangeTracker track;
    private int changes;
    private boolean isDone = false;

    private static final double SPEED = 0.2;

    public RotateWheelCountChanges(ArmSubsystem armIn, int changesIn) {
        arm = armIn;
        addRequirements(arm);
        changes = changesIn;
        track = new ColorChangeTracker(arm.getColorWheel());
    }

    public RotateWheelCountChanges(ArmSubsystem arm) {
        this(arm, 26);
    }

    @Override
    public void execute() {
        if (isDone) {
            return;
        }
        System.out.println("CNT: " + track.getCount());
        if (track.getCount() >= changes) {
            arm.stopWheelSpinner();
            isDone = true;
        } else {
            arm.activateWheelSpinner(SPEED);
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}