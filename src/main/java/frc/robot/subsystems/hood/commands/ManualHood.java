package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

public class ManualHood extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final double power;

    public ManualHood(double power) {
        this.power = power;
    }

    @Override
    public void execute() {
        hood.setPower(power);
    }

    @Override
    public void end(boolean interrupted) {
        hood.stop();
    }
}
