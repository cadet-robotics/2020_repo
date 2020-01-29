package frc.robot.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Arrays;
import java.util.List;

public class MaintainRPMCommand extends PIDCommand {
    private MaintainRPMCommand(SpeedController motor, Encoder encoder, PIDController controller, double targetRPM, Subsystem... requirements) {
        super(controller, encoder::get, targetRPM, motor::set, requirements);
    }

    public MaintainRPMCommand(SpeedController motor, Encoder encoder, double p, double i, double d, double targetRPM, Subsystem... requirements) {
        this(motor, encoder, new PIDController(p, i, d), targetRPM, requirements);
    }

    public boolean isReady() {
        return getController().atSetpoint();
    }
}