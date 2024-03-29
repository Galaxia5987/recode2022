package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.drivetrain.commands.ZeroModules;
import frc.robot.subsystems.hood.commands.RunHood;
import frc.robot.subsystems.intake.commands.ToggleIntake;

public class RunAllBits extends SequentialCommandGroup {

    public RunAllBits() {
        addCommands(
//                new RunHood(),
                new RunPipeline(),
                new ToggleIntake().andThen(new WaitCommand(3)),
                new ToggleIntake().andThen(new WaitCommand(3)),
                new ToggleIntake().andThen(new WaitCommand(3)),
                new ToggleIntake().andThen(new WaitCommand(3)),
                new RunChassis(),
                new ZeroModules()
        );
    }
}
