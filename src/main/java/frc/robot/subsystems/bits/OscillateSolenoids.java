package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OscillateSolenoids extends SequentialCommandGroup {

    public OscillateSolenoids() {
        addCommands(
                new OscillateHood(2)
                        .andThen(new WaitCommand(3)),
                new OscillateIntake(7)
        );
    }
}
