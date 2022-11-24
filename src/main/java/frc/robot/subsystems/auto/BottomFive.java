package frc.robot.subsystems.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.intake.commands.Feed;
import frc.robot.subsystems.intake.commands.ToggleIntake;

public class BottomFive extends AutoFunctions {
    public BottomFive() {
        super("BottomFive", "Bottom5.2");
        {
            //addCommands(followPath("Bottom5.1", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
            addCommands(new ShootCargo().withTimeout(1.8));
            addCommands(followPathAndPickUp("Bottom5.2", 4, 2.5));
            addCommands(new ShootCargo().withTimeout(1.8));
            addCommands(followPathAndPickUp("Bottom5.3", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
            addCommands(new Feed(Constants.Intake.DEFAULT_POWER).withTimeout(1), adjustAndShoot(1.8));
        }
    }
}