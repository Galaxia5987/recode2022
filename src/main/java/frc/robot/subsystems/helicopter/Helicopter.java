package frc.robot.subsystems.helicopter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.valuetuner.WebConstant;

public class Helicopter extends LoggedSubsystem {
    private static Helicopter INSTANCE = null;
    private final WPI_TalonFX masterMotor;
    private final WPI_TalonFX slaveMotor;
    private final UnitModel unitModel = new UnitModel(Constants.Helicopter.TICKS_PER_RADIAN);

    private final WebConstant webKp = WebConstant.of("Helicopter", "kP", Constants.Helicopter.PID_CONSTANTS.kP);
    private final WebConstant webKi = WebConstant.of("Helicopter", "kI", Constants.Helicopter.PID_CONSTANTS.kI);
    private final WebConstant webKd = WebConstant.of("Helicopter", "kD", Constants.Helicopter.PID_CONSTANTS.kD);
    private final WebConstant webKf = WebConstant.of("Helicopter", "kF", Constants.Helicopter.PID_CONSTANTS.kF);
    private final WebConstant webCruiseVelocity = WebConstant.of("Helicopter", "cruiseVelocity", Constants.Helicopter.MAX_VELOCITY);
    private final WebConstant webMaxAcceleration = WebConstant.of("Helicopter", "maxAcceleration", Constants.Helicopter.MAX_ACCELERATION);

    private final HelicopterLogInputs inputs = HelicopterLogInputs.getInstance();

    private Helicopter() {
        super(HelicopterLogInputs.getInstance());

        masterMotor = new WPI_TalonFX(Ports.Helicopter.MASTER_MOTOR);
        masterMotor.setInverted(TalonFXInvertType.Clockwise);
        masterMotor.setNeutralMode(NeutralMode.Brake);

        slaveMotor = new WPI_TalonFX(Ports.Helicopter.SLAVE_MOTOR);
        slaveMotor.follow(masterMotor);
        slaveMotor.setInverted(TalonFXInvertType.OpposeMaster);
        slaveMotor.setNeutralMode(NeutralMode.Brake);
    }

    public static Helicopter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Helicopter();
        }
        return INSTANCE;
    }

    public void updateController() {
        masterMotor.config_kP(0, webKp.get());
        masterMotor.config_kI(0, webKi.get());
        masterMotor.config_kD(0, webKd.get());
        masterMotor.config_kF(0, webKf.get());

        masterMotor.configMotionCruiseVelocity(webCruiseVelocity.get());
        masterMotor.configMotionAcceleration(webMaxAcceleration.get());
    }

    public double getPower() {
        return masterMotor.get();
    }

    public void setPower(double output) {
        masterMotor.set(ControlMode.PercentOutput, output);
    }

    public double getAngle() {
        return unitModel.toUnits(masterMotor.getSelectedSensorPosition());
    }

    public void setAngle(double angle) {
        masterMotor.set(ControlMode.MotionMagic, angle);
    }

    public double getVelocity() {
        return unitModel.toVelocity(masterMotor.getSelectedSensorVelocity());
    }

    public void setVelocity(double velocity) {
        masterMotor.set(ControlMode.Velocity, unitModel.toTicks100ms(velocity));
    }

    @Override
    public void periodic() {
        updateController();
    }

    @Override
    public void updateInputs() {
        inputs.appliedCurrent = new double[]{masterMotor.getSupplyCurrent(), slaveMotor.getSupplyCurrent()};
        inputs.tempCelsius = new double[]{masterMotor.getTemperature(), slaveMotor.getTemperature()};
        inputs.busVoltage = new double[]{masterMotor.getBusVoltage(), slaveMotor.getBusVoltage()};
        inputs.encoderPosition = masterMotor.getSelectedSensorPosition();
        inputs.velocity = getVelocity();
        inputs.power = getPower();
        inputs.angle = getAngle();
        inputs.maxAcceleration = webMaxAcceleration.get();
        inputs.cruiseVelocity = webCruiseVelocity.get();
        inputs.kP = webKp.get();
        inputs.kI = webKi.get();
        inputs.kD = webKd.get();
        inputs.kF = webKf.get();
    }

    @Override
    public String getSubsystemName() {
        return "Helicopter";
    }
}
