package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class RunAllBits extends SequentialCommandGroup {

    public RunAllBits() {
        addCommands(
                new RunPipeline(5),
                new OscillateSolenoids()
        );
    }
}
