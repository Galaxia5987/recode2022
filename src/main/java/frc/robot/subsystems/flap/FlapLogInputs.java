package frc.robot.subsystems.flap;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class FlapLogInputs implements LoggableInputs {
    private static FlapLogInputs INSTANCE = null;
    public Flap.Mode modeName;
    public int modeInt;

    private FlapLogInputs() {
    }

    public static FlapLogInputs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FlapLogInputs();
        }
        return INSTANCE;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("modeName", modeName.name());
        table.put("modeInt", modeName.value ? 1 : 0);
    }

    @Override
    public void fromLog(LogTable table) {
        modeName = Flap.Mode.of(table.getString("flapMode", modeName.name()));
        modeInt = modeName.value ? 1 : 0;
    }
}
