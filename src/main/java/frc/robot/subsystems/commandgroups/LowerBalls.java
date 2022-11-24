package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.conveyor.commands.ConveyAll;

public class LowerBalls extends ParallelCommandGroup {

    public LowerBalls() {
        addCommands(
                new ConveyAll(-0.2, () -> true)
                        .withTimeout(0.2)
        );
    }
}
