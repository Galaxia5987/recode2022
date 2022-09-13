package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.intake.Intake;

public class ToggleIntake extends InstantCommand {

    public ToggleIntake() {
        super(() -> Intake.getInstance().toggleMechanism());
        addRequirements(Intake.getInstance());
    }
}
