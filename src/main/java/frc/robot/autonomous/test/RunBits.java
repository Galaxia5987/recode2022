package frc.robot.autonomous.test;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.bits.OscillateSolenoids;
import frc.robot.subsystems.bits.RunChassis;
import frc.robot.subsystems.bits.RunPipeline;

public class RunBits extends SequentialCommandGroup {

    public RunBits() {
        addCommands(
                new RunPipeline(7),
                new RunChassis(7),
                new OscillateSolenoids()
        );
    }
}
