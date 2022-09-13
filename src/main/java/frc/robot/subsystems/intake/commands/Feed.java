package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.Intake;

public class Feed extends CommandBase {
    private final Intake intake = Intake.getInstance();

    private final double power;

    public Feed(double power) {
        this.power = power;
    }

    @Override
    public void initialize() {
        intake.open();
    }

    @Override
    public void execute() {
        intake.setPower(power);
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPower(0);
    }
}
