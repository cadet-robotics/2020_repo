package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;

import java.util.function.BooleanSupplier;

public class ShooterCommand extends ParallelCommandGroup {
    public ShooterCommand(double topRPM, double bottomRPM) {
        var bottom = new MaintainRPMCommand(Motors.bottomFly, Sensors.bottomFlyEncoder, 0.2, 0, 0, bottomRPM);
        var top = new MaintainRPMCommand(Motors.topFly, Sensors.topFlyEncoder, 0.2, 0, 0, topRPM);

        // TODO: Add a command for moving a ball into the firing mechanism
        var waitForThrow = new SequentialCommandGroup(
                new WaitUntilCommand(() ->
                    bottom.isReady() && top.isReady()
                ),
                new InstantCommand(() -> System.out.println("*THUNK*")),
                new WaitCommand(5),
                new InstantCommand(this::cancel)
        );

        addCommands(bottom, top, waitForThrow);
    }
}