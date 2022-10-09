package frc.robot.subsystems.helicopter;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class HelicopterLogInputs implements LoggableInputs {
    private static HelicopterLogInputs INSTANCE = null;
    public double encoderPosition; // [ticks]
    public double velocity; // [rad/s]
    public double power; // [%]
    public double angle; // [rad]
    public double maxAcceleration; // [rad/s^2]
    public double cruiseVelocity; // [rad/s]
    public double kP;
    public double kI;
    public double kD;
    public double kF;

    private HelicopterLogInputs() {
    }

    public static HelicopterLogInputs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HelicopterLogInputs();
        }
        return INSTANCE;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("encoderPosition", encoderPosition);
        table.put("velocity", velocity);
        table.put("power", power);
        table.put("angle", angle);
        table.put("maxAcceleration", maxAcceleration);
        table.put("cruiseVelocity", cruiseVelocity);
        table.put("kP", kP);
        table.put("kI", kI);
        table.put("kD", kD);
        table.put("kF", kF);
    }

    @Override
    public void fromLog(LogTable table) {
        encoderPosition = table.getDouble("encoderPosition", encoderPosition);
        velocity = table.getDouble("velocity", velocity);
        power = table.getDouble("power", power);
        angle = table.getDouble("angle", angle);
        maxAcceleration = table.getDouble("maxAcceleration", maxAcceleration);
        cruiseVelocity = table.getDouble("cruiseVelocity", cruiseVelocity);
        kP = table.getDouble("kP", kP);
        kI = table.getDouble("kI", kI);
        kD = table.getDouble("kD", kD);
        kF = table.getDouble("kF", kF);
    }
}
