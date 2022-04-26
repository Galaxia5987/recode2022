package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Infrastructure;
import frc.robot.subsystems.intake.Intake;

public class IntakeCargo extends CommandBase {
    private final Intake intake;
    private boolean isXPressed = false;
    private boolean wasXPressed = false;

    public IntakeCargo(Intake intake) {
        this.intake = intake;

        addRequirements(intake);
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().chassisGetLeftTrigger() || Infrastructure.getInstance().chassisGetLeftBumper()) {
            intake.open();
            intake.setPower(Constants.Intake.DEFAULT_POWER);
        } else {
            intake.setPower(0);
        }

        if (wasXPressed != isXPressed) {
            intake.toggleMechanism();
        }

        wasXPressed = isXPressed;
        isXPressed = Infrastructure.getInstance().chassisGetX();
    }
}
