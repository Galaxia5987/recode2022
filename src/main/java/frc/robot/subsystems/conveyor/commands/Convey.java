package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.DoubleConsumer;

/*
Skeleton class for conveying
 */
public class Convey extends CommandBase {
    private final DoubleConsumer convey;
    private final double power;

    public Convey(DoubleConsumer convey, double power) {
        this.convey = convey;
        this.power = power;
    }

    @Override
    public void execute() {
        convey.accept(power);
    }

    @Override
    public void end(boolean interrupted) {
        convey.accept(0);
    }
}
