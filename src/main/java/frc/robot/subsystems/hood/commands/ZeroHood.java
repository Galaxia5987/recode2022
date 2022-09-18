package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

public class ZeroHood extends CommandBase {
    private final Hood hood = Hood.getInstance();

    public ZeroHood() {
    }

    @Override
    public void initialize() {
        hood.configSoftLimits(false);
    }

    @Override
    public void execute() {
        hood.setAngle(0);
    }

    @Override
    public void end(boolean interrupted) {
        hood.configSoftLimits(true);
    }

    @Override
    public boolean isFinished() {
        return hood.atSetpoint(1);
    }
}
