package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.hood.commands.AdjustAngle;

public class RunHood extends SequentialCommandGroup {

    public RunHood() {
        addCommands(
                new AdjustAngle(() -> Constants.Hood.MAX_ANGLE),
                new AdjustAngle(() -> Constants.Hood.MIN_ANGLE)
        );
    }
}
