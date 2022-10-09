package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class IntakeLogInputs implements LoggableInputs {
    private static IntakeLogInputs INSTANCE = null;
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
        table.put("Output", output);
    }

    @Override
    public void fromLog(LogTable table) {
        output = table.getDouble("Output", output);
    }
}
