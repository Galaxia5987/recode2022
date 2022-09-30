package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.intake.commands.Feed;

public class Outtake extends ParallelCommandGroup {
    public Outtake() {
        addCommands(
                new ConveyAll(-Constants.Conveyor.DEFAULT_POWER, () -> true),
                new Feed(-Constants.Intake.DEFAULT_POWER)
        );
    }
}
