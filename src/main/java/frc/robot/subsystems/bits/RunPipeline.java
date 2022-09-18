package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.intake.commands.Feed;
import frc.robot.subsystems.shooter.commands.Shoot;

public class RunPipeline extends SequentialCommandGroup {

    public RunPipeline() {
        addCommands(
                new ParallelCommandGroup(
                        new ConveyAll(Constants.Conveyor.DEFAULT_POWER),
                        new Feed(Constants.Intake.DEFAULT_POWER),
                        new Shoot(() -> Constants.Shooter.MAX_WARMUP_VELOCITY)
                ).withTimeout(5),
                new ParallelCommandGroup(
                        new ConveyAll(-Constants.Conveyor.DEFAULT_POWER),
                        new Feed(-Constants.Intake.DEFAULT_POWER),
                        new Shoot(() -> -Constants.Shooter.MAX_WARMUP_VELOCITY)
                ).withTimeout(5)
        );
    }
}
