package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.utils.Utils;
import frc.robot.valuetuner.WebConstant;

import java.util.HashMap;

public class Shooter extends LoggedSubsystem {
    private static Shooter INSTANCE = null;

    private final WPI_TalonFX master;

    private final WebConstant webKp = WebConstant.of("Shooter", "kP", Constants.Shooter.PID_CONSTANTS.kP);
    private final WebConstant webKi = WebConstant.of("Shooter", "kI", Constants.Shooter.PID_CONSTANTS.kI);
    private final WebConstant webKd = WebConstant.of("Shooter", "kD", Constants.Shooter.PID_CONSTANTS.kD);
    private final WebConstant webKf = WebConstant.of("Shooter", "kF", Constants.Shooter.PID_CONSTANTS.kF);

    private final UnitModel unitModel = new UnitModel(Constants.Shooter.TICKS_PER_ROTATION);
    private final ShooterLogInputs inputs = ShooterLogInputs.getInstance();
    private double setpoint = 0;

    private Shooter() {
        super(ShooterLogInputs.getInstance());
        master = new WPI_TalonFX(Ports.Shooter.MAIN_MOTOR);
        master.setNeutralMode(NeutralMode.Coast);
        master.setInverted(TalonFXInvertType.Clockwise);
        master.enableVoltageCompensation(true);
        master.configVoltageCompSaturation(Constants.NOMINAL_VOLTAGE);
    }

    public static Shooter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Shooter();
        }
        return INSTANCE;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public boolean atSetpoint(double tolerance) {
        return Utils.deadband(getVelocity() - setpoint, tolerance) == 0;
    }

    public double getPower() {
        return master.get();
    }

    public void setPower(double output) {
        master.set(ControlMode.PercentOutput, output);
    }

    public double getVelocity() {
        return unitModel.toVelocity(master.getSelectedSensorVelocity()) * 60.0;
    }

    public void setVelocity(double velocity) {
        master.set(ControlMode.Velocity, unitModel.toTicks100ms(velocity / 60.0));
        setpoint = velocity;
    }

    public double velocityForDistance(double distance) {
        HashMap<Double, Double> measurements = Constants.Shooter.SHOOT_MEASUREMENTS;
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

    public void stop() {
        master.stopMotor();
    }

    public void updatePID() {
        master.config_kP(0, webKp.get());
        master.config_kI(0, webKi.get());
        master.config_kD(0, webKd.get());
        master.config_kF(0, webKf.get());
    }

    @Override
    public void periodic() {
        updatePID();
    }

    @Override
    public void updateInputs() {
        inputs.appliedVolts = master.getMotorOutputVoltage();
        inputs.currentAmps = master.getSupplyCurrent();
        inputs.tempCelsius = master.getTemperature();
        inputs.velocityRpm = getVelocity();
        inputs.setpointRpm = setpoint;
        inputs.kP = webKp.get();
        inputs.kI = webKi.get();
        inputs.kD = webKd.get();
        inputs.kF = webKf.get();
    }

    @Override
    public String getSubsystemName() {
        return "Shooter";
    }
}
