package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

public class ResetHood extends CommandBase {
    private final Hood hood = Hood.getInstance();

    public ResetHood() {
        addRequirements(hood);
    }

    @Override
    public void execute() {
        hood.setPower(-0.05);
    }

    @Override
    public void end(boolean interrupted) {
        hood.setInitialAngle(hood.getAbsoluteTicks());
        hood.stop();
    }
}

