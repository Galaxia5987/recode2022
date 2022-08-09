package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class IntakeLogInputs implements LoggableInputs {
    private static IntakeLogInputs INSTANCE = null;
    public double appliedVoltage;
    public double tempCelsius;
    public double current;
    public double output;

    private IntakeLogInputs() {
    }

    public static IntakeLogInputs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IntakeLogInputs();
        }
        return INSTANCE;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("AppliedVoltage", appliedVoltage);
        table.put("TempCelsius", tempCelsius);
        table.put("Current", current);
        table.put("Output", output);
    }

    @Override
    public void fromLog(LogTable table) {
        appliedVoltage = table.getDouble("AppliedVoltage", appliedVoltage);
        tempCelsius = table.getDouble("TempCelsius", tempCelsius);
        current = table.getDouble("Current", current);
        output = table.getDouble("Output", output);
    }
}
