package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.intake.Intake;

public class IntakeCargo extends CommandBase {
    private final Intake intake;
    private boolean isXPressed = false;
    private boolean wasXPressed = false;

    public IntakeCargo(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().getLeftTrigger() || Infrastructure.getInstance().getLeftBumper()) {
            intake.open();
            intake.setPower(Constants.Intake.DEFAULT_POWER);
        } else {
            intake.setPower(0);
        }

        if (wasXPressed != isXPressed) {
            intake.toggleMechanism();
        }

        wasXPressed = isXPressed;
        isXPressed = Infrastructure.getInstance().getX();
    }
}
