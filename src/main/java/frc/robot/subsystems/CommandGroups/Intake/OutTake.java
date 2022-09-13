package frc.robot.subsystems.CommandGroups.Intake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.intake.Intake;

public class OutTake extends ParallelCommandGroup {
    public OutTake(Intake intake, Conveyor conveyor) {
        addCommands(
                new RunCommand(() -> intake.setPower(-0.5)),
                new RunCommand(() -> conveyor.feedFromIntake(-0.5))
        );
    }
}
