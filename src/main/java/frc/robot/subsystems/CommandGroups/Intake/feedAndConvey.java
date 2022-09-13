package frc.robot.subsystems.CommandGroups.Intake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.intake.Intake;

public class feedAndConvey extends ParallelCommandGroup {
    public feedAndConvey(Intake intake, Conveyor conveyor){
        addCommands(
                new RunCommand()
        );
    }
}
