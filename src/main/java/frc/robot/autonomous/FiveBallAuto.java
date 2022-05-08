package frc.robot.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.autonomous.builder.AutoIntake;
import frc.robot.autonomous.builder.AutoShooting;
import frc.robot.autonomous.builder.BuilderUtil;
import frc.robot.autonomous.builder.PathFollower;

public class FiveBallAuto extends SequentialCommandGroup {

    public FiveBallAuto() {
        addCommands(
                AutoShooting.getInstance().getSimpleShoot(),
                PathFollower.getInstance().defaultFollowPath(BuilderUtil.getJsonFromPathName("FiveBall1"), 1.2, 5, false, false)
                        .raceWith(AutoIntake.getInstance().simpleIntakeCargo())
                        .alongWith(AutoShooting.getInstance().getOdometryWarmup()),
                PathFollower.getInstance().defaultFollowPath(BuilderUtil.getJsonFromPathName("FiveBall2"), 2.6, 5, true, false)
                        .raceWith(AutoIntake.getInstance().simpleIntakeCargo())
                        .alongWith(AutoShooting.getInstance().getOdometryWarmup()),
                AutoShooting.getInstance().getSimpleShoot(),
                PathFollower.getInstance().defaultFollowPath(BuilderUtil.getJsonFromPathName("FiveBall3"), 4, 5, false, false)
                        .alongWith(AutoIntake.getInstance().simpleIntakeCargo())
                        .alongWith(AutoShooting.getInstance().getOdometryWarmup())
                        .withTimeout(3),
                PathFollower.getInstance().defaultFollowPath(BuilderUtil.getJsonFromPathName("FiveBall4"), 3, 5, true, false)
                        .alongWith(AutoShooting.getInstance().getOdometryWarmup()),
                AutoShooting.getInstance().getSimpleShoot()
        );
    }
}
