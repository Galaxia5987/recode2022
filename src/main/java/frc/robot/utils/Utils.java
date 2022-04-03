package frc.robot.utils;

import java.util.function.DoubleUnaryOperator;

public class Utils {
    public static DoubleUnaryOperator f = x -> Math.pow(Math.abs(x), 0.375);

    /**
     * sets the value of the joystick to 0 if the value is less than the threshold
     *
     * @param val       the joystick value
     * @param threshold the threshold value
     * @return 0 if val is less than the threshold else val
     */
    public static double deadband(double val, double threshold) {
        if (Math.abs(val) < threshold)
            return 0;
        return val;
    }

    /**
     * @param input the joystick input
     * @param threshold the joystick deadband threshold
     * @return the updated value after the deadband
     */
    public static double rotationalDeadband(double input, double threshold) {
        if (Math.abs(input) < threshold)
            return 0;
        return (input - (Math.signum(input) * threshold)) / (1 - threshold);
    }


    public static double swerveSmoothing(double value, double deadband) {
        value = deadband(value, deadband);
        return (f.applyAsDouble(value) - f.applyAsDouble(deadband)) *
                ((Math.signum(value)) / (1 - f.applyAsDouble(deadband)));
    }
}