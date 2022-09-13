package frc.robot.subsystems.conveyor.commands;

import frc.robot.subsystems.conveyor.Conveyor;

public class ConveyAll extends Convey {
    public ConveyAll(double power) {
        super((output) -> {
            Conveyor.getInstance().feedToShooter(output);
            Conveyor.getInstance().feedFromIntake(output);
        }, power);
    }
}
