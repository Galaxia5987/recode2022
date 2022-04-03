package frc.robot.subsystems;

import frc.robot.utils.Units;

public interface PeriodicSubsystem {

    void periodic();

    void outputTelemetry();

    default Units.Types getUnitType() {
        return Units.Types.NONE;
    }
}
