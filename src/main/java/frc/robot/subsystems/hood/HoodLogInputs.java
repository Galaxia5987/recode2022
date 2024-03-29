package frc.robot.subsystems.hood;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class HoodLogInputs implements LoggableInputs {
    private static HoodLogInputs INSTANCE = null;
    public double ticks;
    public double relativeTicks;
    public double angle;
    public double setpoint;
    public double velocity;
    public double busVoltage;
    public double outputCurrent;
    public double temperatureCelsius;

    private HoodLogInputs() {
    }

    public static HoodLogInputs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoodLogInputs();
        }
        return INSTANCE;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("Ticks", ticks);
        table.put("RelativeTicks", relativeTicks);
        table.put("Angle", angle);
        table.put("Setpoint", setpoint);
        table.put("Velocity", velocity);
        table.put("BusVoltage", busVoltage);
        table.put("OutputCurrent", outputCurrent);
        table.put("TemperatureCelsius", temperatureCelsius);
    }

    @Override
    public void fromLog(LogTable table) {
        ticks = table.getDouble("Ticks", ticks);
        relativeTicks = table.getDouble("RelativeTicks", relativeTicks);
        angle = table.getDouble("Angle", angle);
        setpoint = table.getDouble("Setpoint", setpoint);
        velocity = table.getDouble("Velocity", velocity);
        busVoltage = table.getDouble("BusVoltage", busVoltage);
        outputCurrent = table.getDouble("OutputCurrent", outputCurrent);
        temperatureCelsius = table.getDouble("TemperatureCelsius", temperatureCelsius);
    }
}
