package frc.robot.subsystems.drivetrain;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveModuleLogInputs implements LoggableInputs {
    private static final SwerveModuleLogInputs[] INSTANCES = new SwerveModuleLogInputs[]{null, null, null, null};
    public double aVelocity;
    public double aPosition;
    public double aAngle;
    public double aKp;
    public double aKi;
    public double aKd;
    public double aKf;

    public double dVelocity;
    public double dJ;

    private SwerveModuleLogInputs() {
    }

    public static SwerveModuleLogInputs getInstance(int wheel) {
        if (INSTANCES[wheel] == null) {
            INSTANCES[wheel] = new SwerveModuleLogInputs();
        }
        return INSTANCES[wheel];
    }

    @Override
    public void toLog(LogTable table) {
        table.put("aVelocity", aVelocity);
        table.put("aPosition", aPosition);
        table.put("aAngle", aAngle);
        table.put("aKp", aKp);
        table.put("aKi", aKi);
        table.put("aKd", aKd);
        table.put("aKf", aKf);

        table.put("dVelocity", dVelocity);
        table.put("dJ", dJ);
    }

    @Override
    public void fromLog(LogTable table) {
        aVelocity = table.getDouble("aVelocity", aVelocity);
        aPosition = table.getDouble("aPosition", aPosition);
        aAngle = table.getDouble("aAngle", aAngle);
        aKp = table.getDouble("aKp", aKp);
        aKi = table.getDouble("aKi", aKi);
        aKd = table.getDouble("aKd", aKd);
        aKf = table.getDouble("aKf", aKf);

        dVelocity = table.getDouble("dVelocity", dVelocity);
        dJ = table.getDouble("dJ", dJ);
    }
}
