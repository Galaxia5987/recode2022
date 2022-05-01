package frc.robot.utils;

import edu.wpi.first.math.controller.PIDController;

public class SupaDupaPIDController {
    private final PIDController baseController;
    private final double kIF;
    private double currentSetpoint = 0;

    public SupaDupaPIDController(PIDController baseController, double kIF) {
        this.baseController = baseController;
        this.kIF = kIF;
    }

    public double calculate(double measurement, double setpoint) {
        double lastSetpoint = currentSetpoint;
        currentSetpoint = setpoint;
        double changeInSetpoint = currentSetpoint - lastSetpoint;
        double value = baseController.calculate(measurement, currentSetpoint);
        return value + kIF * changeInSetpoint;
    }
}
