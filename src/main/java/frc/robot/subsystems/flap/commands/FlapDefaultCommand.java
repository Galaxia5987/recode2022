package frc.robot.subsystems.flap.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.flap.Flap;

public class FlapDefaultCommand extends CommandBase {
    private final Flap flap;

    public FlapDefaultCommand(Flap flap) {
        this.flap = flap;

        addRequirements(flap);
    }

    @Override
    public void execute() {
        if (RobotContainer.getInstance().chassisGetLeftTrigger()) {
            flap.setMode(false);
        } else if (RobotContainer.getInstance().chassisGetRightTrigger()) {
            flap.setMode(true);
        }
    }
}
