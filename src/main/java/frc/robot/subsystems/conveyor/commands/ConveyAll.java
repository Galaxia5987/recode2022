package frc.robot.subsystems.conveyor.commands;

import frc.robot.subsystems.conveyor.Conveyor;

import java.util.function.BooleanSupplier;

public class ConveyAll extends Convey {
    public ConveyAll(double power, BooleanSupplier convey) {
        super((output) -> {
            Conveyor.getInstance().feedToShooter(output * (convey.getAsBoolean() ? 1 : 0));
            Conveyor.getInstance().feedFromIntake(output * (convey.getAsBoolean() ? 1 : 0));
        }, power);
    }
}
