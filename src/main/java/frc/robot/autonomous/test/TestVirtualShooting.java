package frc.robot.autonomous.test;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.autonomous.builder.AutoShooting;
import frc.robot.autonomous.builder.PathFollower;

public class TestVirtualShooting extends SequentialCommandGroup {

    public TestVirtualShooting() {
        addCommands(
                PathFollower.getInstance().defaultFollowPath("TestVirtualShooting1", 2, 5, true, true)
                        .alongWith(AutoShooting.getInstance().getVirtualShoot()),
                PathFollower.getInstance().defaultFollowPath("TestVirtualShooting2", 2, 5, true, true)
                        .alongWith(AutoShooting.getInstance().getVirtualShoot())
        );
    }
}
