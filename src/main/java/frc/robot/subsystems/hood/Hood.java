package frc.robot.subsystems.hood;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.valuetuner.WebConstant;

public class Hood extends LoggedSubsystem {
    private static Hood INSTANCE = null;
    private final WPI_TalonFX motor;
    private final UnitModel unitModel = new UnitModel(Constants.Hood.TICKS_PER_DEGREE);

    private final WebConstant webKp = WebConstant.of("Hood", "kP", Constants.Hood.Kp);
    private final WebConstant webKi = WebConstant.of("Hood", "kP", Constants.Hood.Ki);
    private final WebConstant webKd = WebConstant.of("Hood", "kP", Constants.Hood.Kd);
    private final WebConstant webKf = WebConstant.of("Hood", "kP", Constants.Hood.Kf);

    private final HoodLogInputs inputs = HoodLogInputs.getInstance();

    private Hood() {
        super(HoodLogInputs.getInstance());
        motor = new WPI_TalonFX(Ports.Hood.MOTOR);
        motor.setSelectedSensorPosition(0);

        updatePID();
    }

    public static Hood getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Hood();
        }
        return INSTANCE;
    }

    public double getAngle() {
        return unitModel.toUnits(motor.getSelectedSensorPosition());
    }

    public void setAngle(double angle) {
        motor.set(ControlMode.Position, angle);
    }

    public double getVelocity() {
        return unitModel.toVelocity(motor.getSelectedSensorVelocity());
    }

    public void updatePID() {
        motor.config_kP(0, webKp.get());
        motor.config_kI(0, webKi.get());
        motor.config_kD(0, webKd.get());
        motor.config_kF(0, webKf.get());
    }

    @Override
    public void periodic() {
        updatePID();
    }

    @Override
    public void updateInputs() {
        inputs.ticks = motor.getSelectedSensorPosition();
        inputs.angle = getAngle();
        inputs.velocity = getVelocity();
        inputs.busVoltage = motor.getBusVoltage();
        inputs.outputCurrent = motor.getSupplyCurrent();
        inputs.temperatureCelsius = motor.getTemperature();
    }

    @Override
    public String getSubsystemName() {
        return "Hood";
    }
}
