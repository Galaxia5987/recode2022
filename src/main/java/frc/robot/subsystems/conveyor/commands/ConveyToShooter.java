package frc.robot.subsystems.conveyor.commands;

import frc.robot.subsystems.conveyor.Conveyor;

public class ConveyToShooter extends Convey {

    public ConveyToShooter(double power) {
        super((output) -> Conveyor.getInstance().feedToShooter(output), power);
    }
}
