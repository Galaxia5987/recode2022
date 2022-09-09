package frc.robot.subsystems.conveyor;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ConveyorLogInputs implements LoggableInputs {
    private static ConveyorLogInputs INSTANCE = null;
    public double powerFromIntake;
    public double powerToShooter;
    public double proximity;
    public double colorSensorRed;
    public double colorSensorGreen;
    public double colorSensorBlue;

    public boolean isCargoVisible;
    public boolean wasCargoVisible;
    public boolean preFBSensesObject;
    public boolean preFBSensedObject;
    public boolean postFBSensesObject;
    public boolean postFBSensedObject;

    public static ConveyorLogInputs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConveyorLogInputs();
        }
        return INSTANCE;
    }

    private ConveyorLogInputs() {}

    @Override
    public void toLog(LogTable table) {
        table.put("powerFromIntake", powerFromIntake);
        table.put("powerToShooter", powerToShooter);
        table.put("proximity", proximity);
        table.put("colorSensorRed", colorSensorRed);
        table.put("colorSensorGreen", colorSensorGreen);
        table.put("colorSensorBlue", colorSensorBlue);

        table.put("isCargoVisible", isCargoVisible);
        table.put("wasCargoVisible", wasCargoVisible);
        table.put("preFBSensesObject", preFBSensesObject);
        table.put("preFBSensedObject", preFBSensedObject);
        table.put("postFBSensesObject", postFBSensesObject);
        table.put("postFBSensedObject", postFBSensedObject);
    }

    @Override
    public void fromLog(LogTable table) {
        powerFromIntake = table.getDouble("powerFromIntake", powerFromIntake);
        powerToShooter = table.getDouble("powerToShooter", powerToShooter);
        proximity = table.getDouble("proximity", proximity);
        colorSensorRed = table.getDouble("colorSensorRed", colorSensorRed);
        colorSensorGreen = table.getDouble("colorSensorGreen", colorSensorGreen);
        colorSensorBlue = table.getDouble("colorSensorBlue", colorSensorBlue);

        isCargoVisible = table.getBoolean("isCargoVisible", isCargoVisible);
        wasCargoVisible = table.getBoolean("wasCargoVisible", wasCargoVisible);
        preFBSensesObject = table.getBoolean("preFBSensesObject", preFBSensesObject);
        preFBSensedObject = table.getBoolean("preFBSensedObject", preFBSensedObject);
        postFBSensesObject = table.getBoolean("postFBSensesObject", postFBSensesObject);
        postFBSensedObject = table.getBoolean("postFBSensedObject", postFBSensedObject);
    }
}
