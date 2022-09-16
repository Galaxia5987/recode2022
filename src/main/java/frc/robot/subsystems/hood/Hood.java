package frc.robot.subsystems.hood;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.utils.Utils;
import frc.robot.valuetuner.WebConstant;

public class Hood extends LoggedSubsystem {
    private static Hood INSTANCE = null;
    private final WPI_TalonFX motor;
    private final DutyCycleEncoder encoder;

    private final UnitModel unitModelPosition = new UnitModel(Constants.Hood.TICKS_PER_DEGREE);
    private final UnitModel unitModelPositionAbsolute = new UnitModel(Constants.Hood.TICKS_PER_RAD_ABSOLUTE_ENCODER);

    private final WebConstant webKp = WebConstant.of("Hood", "kP", Constants.Hood.Kp);
    private final WebConstant webKi = WebConstant.of("Hood", "kP", Constants.Hood.Ki);
    private final WebConstant webKd = WebConstant.of("Hood", "kP", Constants.Hood.Kd);
    private final WebConstant webKf = WebConstant.of("Hood", "kP", Constants.Hood.Kf);

    private double setpoint;

    private final HoodLogInputs inputs = HoodLogInputs.getInstance();

    private Hood() {
        super(HoodLogInputs.getInstance());
        motor = new WPI_TalonFX(Ports.Hood.MOTOR);
        motor.setNeutralMode(NeutralMode.Brake);

        encoder = new DutyCycleEncoder(8);
        motor.setInverted(TalonFXInvertType.Clockwise);
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, Constants.TALON_TIMEOUT);
        motor.setSelectedSensorPosition(encoder.get() * 2048 - Constants.Hood.ZERO_POSITION);
        motor.configReverseSoftLimitEnable(true);
        motor.configReverseSoftLimitThreshold(Constants.Hood.BOTTOM_SOFT_LIMIT);
        motor.configForwardSoftLimitEnable(true);
        motor.configForwardSoftLimitThreshold(Constants.Hood.TOP_SOFT_LIMIT);

        motor.configMotionCruiseVelocity(unitModelPosition.toTicks100ms(Constants.Hood.MAX_VELOCITY));
        motor.configMotionAcceleration(unitModelPosition.toTicks100ms(Constants.Hood.MAX_ACCELERATION));
    }

    public static Hood getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Hood();
        }
        return INSTANCE;
    }


    /**
     * @return the absolute position of the Helicopter.
     */
//    public double getAbsolutePosition() {
//        return Math.IEEEremainder(unitModelPositionAbsolute.toUnits(encoder.get() - Constants.Helicopter.ZERO_POSITION), 2 * Math.PI);
//    }

    public double getAngle() {
        return Math.IEEEremainder(unitModelPosition.toUnits(motor.getSelectedSensorPosition()), 360.0);
    }

    public void setAngle(double angle) {
        motor.set(ControlMode.MotionMagic, unitModelPosition.toTicks(angle));
        setpoint = angle;
    }

    public void setPower(double output) {
        motor.set(ControlMode.PercentOutput, output);
    }

    public double getVelocity() {
        return unitModelPosition.toVelocity(motor.getSelectedSensorVelocity());
    }

    public void stop() {
        motor.stopMotor();
    }

    public boolean atSetpoint(double tolerance) {
        return Utils.deadband(getAngle() - setpoint, tolerance) == 0;
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
        inputs.ticks = encoder.get();
        inputs.angle = getAngle();
        inputs.setpoint = setpoint;
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
