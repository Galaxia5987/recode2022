package frc.robot.subsystems;

import frc.robot.utils.Units;

public interface MotorSubsystem {

    double getPower();

    void setPower(double output);

    default double getVelocity() {
        return getPower();
    }

    default void setVelocity(double velocity) {
        setPower(velocity);
    }

    default Units.Types getUnitType() {
        return Units.Types.NONE;
    }
}
