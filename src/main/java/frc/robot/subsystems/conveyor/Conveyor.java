package frc.robot.subsystems.conveyor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.utils.TalonFXFactory;
import webapp.FireLog;

public class Conveyor extends SubsystemBase {
    private static Conveyor INSTANCE = null;
    private final WPI_TalonFX motor;
    private final DigitalInput preFlapBeamBreaker;
    private final DigitalInput postFlapBeamBreaker;
    private final ColorSensorV3 colorSensor;
    private final ColorMatch colorMatch;

    private Color currentColorSensed = Constants.Conveyor.NONE;
    private Color lastColorSensed;

    private Conveyor() {
        motor = TalonFXFactory.getInstance().createSimpleTalonFX(
                Ports.Conveyor.MOTOR,
                TalonFXInvertType.CounterClockwise,
                NeutralMode.Brake);
        preFlapBeamBreaker = new DigitalInput(Ports.Conveyor.PRE_FLAP_BEAM);
        postFlapBeamBreaker = new DigitalInput(Ports.Conveyor.POST_FLAP_BEAM);
        colorSensor = new ColorSensorV3(Ports.Conveyor.COLOR_SENSOR);
        colorMatch = new ColorMatch();

        colorMatch.addColorMatch(Constants.Conveyor.BLUE);
        colorMatch.addColorMatch(Constants.Conveyor.RED);
        colorMatch.addColorMatch(Constants.Conveyor.NONE);
    }

    public static Conveyor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Conveyor();
        }
        return INSTANCE;
    }

    public boolean isPreFlapBeamConnected() {
        return preFlapBeamBreaker.get();
    }

    public boolean isPostFlapBeamConnected() {
        return postFlapBeamBreaker.get();
    }

    public boolean newObjectSensed() {
        return !lastColorSensed.equals(currentColorSensed) && lastColorSensed.equals(Constants.Conveyor.NONE);
    }

    public boolean oldObjectExited() {
        return !lastColorSensed.equals(currentColorSensed) && !lastColorSensed.equals(Constants.Conveyor.NONE);
    }

    public Color getColor() {
        if (colorSensor.getProximity() < 100) {
            return Constants.Conveyor.NONE;
        }
        ColorMatchResult matchResult = colorMatch.matchColor(colorSensor.getColor());
        SmartDashboard.putNumber("Color measurement confidence", matchResult.confidence);
        return matchResult.color;
    }

    public Color getLastColor() {
        return lastColorSensed;
    }

    public Color allianceToColor(DriverStation.Alliance alliance) {
        if (alliance.equals(DriverStation.Alliance.Blue)) {
            return Constants.Conveyor.BLUE;
        } else if (alliance.equals(DriverStation.Alliance.Red)) {
            return Constants.Conveyor.RED;
        }
        return Constants.Conveyor.NONE;
    }

    public double getPower() {
        return motor.get();
    }

    public void setPower(double output) {
        motor.set(ControlMode.PercentOutput, output);
    }

    @Override
    public void periodic() {
        lastColorSensed = currentColorSensed;
        currentColorSensed = getColor();
        SmartDashboard.putNumber("Conveyor power", getPower());
        SmartDashboard.putBoolean("Pre flap beam connected", isPreFlapBeamConnected());
        SmartDashboard.putBoolean("Post flap beam connected", isPostFlapBeamConnected());
        FireLog.log("Conveyor power", getPower());
    }
}
