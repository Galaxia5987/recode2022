package frc.robot.subsystems.auto;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.intake.commands.Feed;

public class TopThree extends AutoFunctions {
    public TopThree() {
        super("TopThree", "Top3.2");
       // addCommands(followPath("Top3.1", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
        addCommands(new ShootCargo().withTimeout(3));
        addCommands(followPathAndPickUp("Top3.2", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
        addCommands(turnToAngle(() -> Robot.getAngle().getDegrees() - 170, 2)
                .raceWith(new FeedAndConvey()));
        addCommands(new ShootCargo().withTimeout(3)
                .raceWith(new Feed(Constants.Intake.DEFAULT_POWER)));
    }
}

