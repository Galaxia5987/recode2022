package frc.robot.subsystems.hood;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class HoodLogInputs implements LoggableInputs {
    private static HoodLogInputs INSTANCE = null;
    public Hood.Mode mode;

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
        table.put("ModeName", mode.name());
        table.put("ModeValue", mode.value);
    }

    @Override
    public void fromLog(LogTable table) {
        mode = Hood.Mode.of(table.getBoolean("ModeValue", mode.value));
    }
}
