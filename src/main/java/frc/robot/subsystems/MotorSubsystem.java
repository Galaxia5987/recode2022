package frc.robot.subsystems;

public interface MotorSubsystem {

    void setPower(double output);

    double getPower();

    default void setVelocity(double velocity) {
        setPower(velocity);
    }

    default double getVelocity() {
        return getPower();
    }
}
