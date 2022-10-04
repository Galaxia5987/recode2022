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
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;

public class Conveyor extends LoggedSubsystem {
    private static Conveyor INSTANCE = null;
    private final WPI_TalonFX motorFromIntake;
    private final WPI_TalonFX motorToShooter;
    private final DigitalInput preFlapBeamBreaker;
    private final DigitalInput postFlapBeamBreaker;
    private final ColorSensorV3 colorSensor;
    private final ColorMatch colorMatch;
    private final ConveyorLogInputs inputs;
    private Color currentColorSensed = Constants.Conveyor.NONE;
    private Color lastColorSensed;

    private Conveyor() {
        super(ConveyorLogInputs.getInstance());
        inputs = ConveyorLogInputs.getInstance();

        motorFromIntake = new WPI_TalonFX(Ports.Conveyor.MOTOR_FROM_INTAKE);
        motorFromIntake.setInverted(TalonFXInvertType.Clockwise);
        motorFromIntake.setNeutralMode(NeutralMode.Brake);
        motorFromIntake.enableVoltageCompensation(true);
        motorFromIntake.configVoltageCompSaturation(10);

        motorToShooter = new WPI_TalonFX(Ports.Conveyor.MOTOR_TO_SHOOTER);
        motorToShooter.setInverted(TalonFXInvertType.CounterClockwise);
        motorToShooter.setNeutralMode(NeutralMode.Brake);
        motorToShooter.enableVoltageCompensation(true);
        motorToShooter.configVoltageCompSaturation(10);


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

    public boolean preFlapBeamSeesObject() {
        return preFlapBeamBreaker.get();
    }

    public boolean postFlapBeamSeesObject() {
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

    public void feedFromIntake(double power) {
        motorFromIntake.set(ControlMode.PercentOutput, power);
    }

    public void feedToShooter(double power) {
        motorToShooter.set(ControlMode.PercentOutput, power);
    }

    public MotorsState getPower() {
        return new MotorsState(motorFromIntake.get(), motorToShooter.get());
    }

    @Override
    public void updateInputs() {
        inputs.powerFromIntake = motorFromIntake.get();
        inputs.powerToShooter = motorToShooter.get();
        inputs.proximity = colorSensor.getProximity();
        inputs.colorSensorRed = colorSensor.getRed();
        inputs.colorSensorGreen = colorSensor.getGreen();
        inputs.colorSensorBlue = colorSensor.getBlue();

        inputs.wasCargoVisible = inputs.isCargoVisible;
        inputs.isCargoVisible = inputs.proximity >= Constants.Conveyor.MINIMUM_PROXIMITY;
        inputs.preFBSensedObject = inputs.preFBSensesObject;
        inputs.preFBSensesObject = preFlapBeamSeesObject();
        inputs.postFBSensedObject = inputs.postFBSensesObject;
        inputs.postFBSensesObject = postFlapBeamSeesObject();
    }

    @Override
    public String getSubsystemName() {
        return "Conveyor";
    }

    @Override
    public void periodic() {
        lastColorSensed = currentColorSensed;
//        currentColorSensed = getColor();
    }

    public static class MotorsState {
        public double motorFromIntakePower;
        public double motorToShooterPower;

        public MotorsState(double motorFromIntakePower, double motorToShooterPower) {
            this.motorFromIntakePower = motorFromIntakePower;
            this.motorToShooterPower = motorToShooterPower;
        }

        public double[] toArray() {
            return new double[]{motorFromIntakePower, motorToShooterPower};
        }
    }
}