package frc.robot.subsystems.hood;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.utils.Utils;
import frc.robot.valuetuner.WebConstant;

import java.util.HashMap;

public class Hood extends LoggedSubsystem {
    private static Hood INSTANCE = null;
    private final WPI_TalonFX motor;
    private final DutyCycleEncoder encoder = new DutyCycleEncoder(9);

    private double initialAngle;

    private final UnitModel unitModelPosition = new UnitModel(Constants.Hood.TICKS_PER_DEGREE);

    private final WebConstant webKp = WebConstant.of("Hood", "kP", Constants.Hood.Kp);
    private final WebConstant webKi = WebConstant.of("Hood", "kI", Constants.Hood.Ki);
    private final WebConstant webKd = WebConstant.of("Hood", "kD", Constants.Hood.Kd);
    private final WebConstant webKf = WebConstant.of("Hood", "kF", Constants.Hood.Kf);
    private final HoodLogInputs inputs = HoodLogInputs.getInstance();
    private double setpoint;
    private boolean hasSetInitialValue = false;

    private Hood() {
        super(HoodLogInputs.getInstance());
        motor = new WPI_TalonFX(Ports.Hood.MOTOR);
        motor.setNeutralMode(NeutralMode.Brake);

        motor.setInverted(Ports.Hood.inversion);
        motor.configNeutralDeadband(0.01);
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, Constants.TALON_TIMEOUT);
        configSoftLimits(false);

        System.out.println("Absolute: " + (encoder.isConnected()));
        //motor.configIntegratedSensorInitializationStrategy(SensorInitializationStrategy.BootToZero);
        motor.setSelectedSensorPosition(0);
        motor.configMotionCruiseVelocity(unitModelPosition.toTicks100ms(Constants.Hood.MAX_VELOCITY));
        motor.configMotionAcceleration(unitModelPosition.toTicks100ms(Constants.Hood.MAX_ACCELERATION));

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(10);
    }

    public static Hood getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Hood();
        }
        return INSTANCE;
    }

    public double getAngle() {
        return Math.IEEEremainder(unitModelPosition.toUnits(motor.getSelectedSensorPosition()) + initialAngle, 360.0);
    }


    public void setAngle(double angle) {
        double currentAngle = getAngle();
        double error = angle - currentAngle;

        motor.set(ControlMode.MotionMagic, motor.getSelectedSensorPosition() + unitModelPosition.toTicks(error));
        setpoint = angle;
    }

    public double angleToDistance(double distance) {
        HashMap<Double, Double> measurements = Constants.Hood.HOOD_MEASUREMENTS;
        double prevMeasuredDistance = 0, nextMeasuredDistance = 0;
        double minPrevDifference = Double.POSITIVE_INFINITY, minNextDifference = Double.POSITIVE_INFINITY;

        for (var measuredDistance : measurements.keySet()) {
            double difference = measuredDistance - distance;
            if (difference < 0) {
                if (Math.abs(difference) < Math.abs(minPrevDifference)) {
                    minPrevDifference = difference;
                    prevMeasuredDistance = measuredDistance;
                }
            } else {
                if (Math.abs(difference) < Math.abs(minNextDifference)) {
                    minNextDifference = difference;
                    nextMeasuredDistance = measuredDistance;
                }
            }
        }
        double y1 = measurements.get(prevMeasuredDistance);
        double y2 = measurements.get(nextMeasuredDistance);
        double t = (distance - prevMeasuredDistance) / (nextMeasuredDistance - prevMeasuredDistance);
        return (1 - t) * y1 + t * y2;
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

    public void configSoftLimits(boolean enable) {
        motor.configReverseSoftLimitEnable(enable);
        motor.configReverseSoftLimitThreshold(Constants.Hood.BOTTOM_SOFT_LIMIT);
        motor.configForwardSoftLimitEnable(enable);
        motor.configForwardSoftLimitThreshold(Constants.Hood.TOP_SOFT_LIMIT);
    }

    @Override
    public void periodic() {
        if (encoder.isConnected() && !hasSetInitialValue) {
            hasSetInitialValue = true;
            initialAngle = (encoder.getAbsolutePosition() * 360.0 - Constants.Hood.ZERO_POSITION / 2048 * 360.0);
        }
        updatePID();
    }

    @Override
    public void updateInputs() {
//        inputs.ticks = encoder.getAbsolutePosition();
        inputs.ticks = encoder.getAbsolutePosition() * 2048;
        inputs.relativeTicks = motor.getSelectedSensorPosition();
        inputs.angle = getAngle();
        inputs.setpoint = setpoint;
        inputs.velocity = getVelocity();
        inputs.busVoltage = Constants.Hood.ZERO_POSITION / 2048 * 360.0;
        inputs.outputCurrent = encoder.getAbsolutePosition() * 360.0;
        inputs.temperatureCelsius = initialAngle;
    }

    @Override
    public String getSubsystemName() {
        return "Hood";
    }
}
