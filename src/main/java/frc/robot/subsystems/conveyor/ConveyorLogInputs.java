package frc.robot.subsystems.conveyor;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ConveyorLogInputs implements LoggableInputs {
    public double power;
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

    @Override
    public void toLog(LogTable table) {
        table.put("power", power);
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
        power = table.getDouble("power", power);
        proximity = table.getDouble("proximity", proximity);
        colorSensorRed = table.getDouble("colorSensorRed", colorSensorRed);
        table.getDouble("colorSensorGreen", colorSensorGreen);
        table.getDouble("colorSensorBlue", colorSensorBlue);

        table.getBoolean("isCargoVisible", isCargoVisible);
        table.getBoolean("wasCargoVisible", wasCargoVisible);
        table.getBoolean("preFBSensesObject", preFBSensesObject);
        table.getBoolean("preFBSensedObject", preFBSensedObject);
        table.getBoolean("postFBSensesObject", postFBSensesObject);
        table.getBoolean("postFBSensedObject", postFBSensedObject);
    }
}
