package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ShooterLogInputs implements LoggableInputs {
    private static ShooterLogInputs INSTANCE = null;
    public double currentAmps;
    public double tempCelsius;
    public double appliedVolts;
    public double velocityRpm;
    public double setpointRpm;
    public double kP;
    public double kI;
    public double kD;
    public double kF;

    private ShooterLogInputs() {
    }

    public static ShooterLogInputs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShooterLogInputs();
        }
        return INSTANCE;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("AppliedVolts", appliedVolts);
        table.put("CurrentAmps", currentAmps);
        table.put("TempCelsius", tempCelsius);
        table.put("VelocityRPM", velocityRpm);
        table.put("SetpointRPM", setpointRpm);
        table.put("kP", kP);
        table.put("kI", kI);
        table.put("kD", kD);
        table.put("kF", kF);
    }

    @Override
    public void fromLog(LogTable table) {
        appliedVolts = table.getDouble("AppliedVolts", appliedVolts);
        currentAmps = table.getDouble("CurrentAmps", currentAmps);
        tempCelsius = table.getDouble("TempCelsius", tempCelsius);
        velocityRpm = table.getDouble("VelocityRPM", velocityRpm);
        setpointRpm = table.getDouble("SetpointRPM", setpointRpm);
        kP = table.getDouble("kP", kP);
        kI = table.getDouble("kI", kI);
        kD = table.getDouble("kD", kD);
        kF = table.getDouble("kF", kF);
    }
}
