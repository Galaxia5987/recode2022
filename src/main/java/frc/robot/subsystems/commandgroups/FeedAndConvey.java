package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.commands.ConveyFromIntake;
import frc.robot.subsystems.intake.commands.Feed;

public class FeedAndConvey extends ParallelCommandGroup {
    public FeedAndConvey() {
        addCommands(
                new ConveyFromIntake(Constants.Conveyor.DEFAULT_POWER),
               new Feed(Constants.Intake.DEFAULT_POWER)
        );
    }
}
