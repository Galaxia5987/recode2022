package frc.robot.subsystems;

public interface MotorSubsystem {

    double getPower();

    void setPower(double output);

    default double getVelocity() {
        return getPower();
    }

    default void setVelocity(double velocity) {
        setPower(velocity);
    }
}
