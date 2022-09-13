package frc.robot.subsystems.conveyor.commands;

import frc.robot.subsystems.conveyor.Conveyor;

public class ConveyFromIntake extends Convey {

    public ConveyFromIntake(double power) {
        super((output) -> Conveyor.getInstance().feedFromIntake(output), power);
    }
}
