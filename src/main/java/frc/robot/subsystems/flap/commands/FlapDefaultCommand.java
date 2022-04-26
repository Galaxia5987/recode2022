package frc.robot.subsystems.flap.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Infrastructure;
import frc.robot.subsystems.flap.Flap;

public class FlapDefaultCommand extends CommandBase {
    private final Flap flap;

    public FlapDefaultCommand(Flap flap) {
        this.flap = flap;

        addRequirements(flap);
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().chassisGetLeftTrigger()) {
            flap.setMode(false);
        } else if (Infrastructure.getInstance().chassisGetRightTrigger()) {
            flap.setMode(true);
        }
    }
}
