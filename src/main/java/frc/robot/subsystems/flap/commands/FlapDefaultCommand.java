package frc.robot.subsystems.flap.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.flap.Flap;

public class FlapDefaultCommand extends CommandBase {
    private final Flap flap;

    public FlapDefaultCommand(Flap flap) {
        this.flap = flap;
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().getLeftTrigger()) {
            flap.setMode(false);
        } else if (Infrastructure.getInstance().getRightTrigger()) {
            flap.setMode(true);
        }
    }
}
