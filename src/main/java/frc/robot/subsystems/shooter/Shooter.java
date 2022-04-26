package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.MotorSubsystem;
import frc.robot.subsystems.PeriodicSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.utils.TalonFXFactory;
import frc.robot.utils.Units;
import frc.robot.utils.Utils;
import frc.robot.valuetuner.WebConstant;
import webapp.FireLog;

public class Shooter implements PeriodicSubsystem, MotorSubsystem {
    private static Shooter DEFAULT_INSTANCE = null;
    private static Shooter TUNE_LEFT_INSTANCE = null;
    private static Shooter TUNE_RIGHT_INSTANCE = null;

    private final WPI_TalonFX leftMotor;
    private final WPI_TalonFX rightMotor;

    private final WebConstant left_webKp = WebConstant.of("Shooter", "kP", Constants.Shooter.LEFT_PID_CONSTANTS.kP);
    private final WebConstant left_webKi = WebConstant.of("Shooter", "kI", Constants.Shooter.LEFT_PID_CONSTANTS.kI);
    private final WebConstant left_webKd = WebConstant.of("Shooter", "kD", Constants.Shooter.LEFT_PID_CONSTANTS.kD);
    private final WebConstant left_webKf = WebConstant.of("Shooter", "kF", Constants.Shooter.LEFT_PID_CONSTANTS.kF);

    private final WebConstant right_webKp = WebConstant.of("Shooter", "kP", Constants.Shooter.LEFT_PID_CONSTANTS.kP);
    private final WebConstant right_webKi = WebConstant.of("Shooter", "kI", Constants.Shooter.LEFT_PID_CONSTANTS.kI);
    private final WebConstant right_webKd = WebConstant.of("Shooter", "kD", Constants.Shooter.LEFT_PID_CONSTANTS.kD);
    private final WebConstant right_webKf = WebConstant.of("Shooter", "kF", Constants.Shooter.LEFT_PID_CONSTANTS.kF);

    private final UnitModel unitModel = new UnitModel(Constants.Shooter.TICKS_PER_ROTATION);
    private final boolean tuneLeft;
    private final boolean tuneRight;
    private double setpoint = 0;

    private Shooter(boolean tuneLeft, boolean tuneRight) {
        this.tuneLeft = tuneLeft;
        this.tuneRight = tuneRight;

        leftMotor = TalonFXFactory.getInstance().createDefaultPIDTalonFX(
                Ports.Shooter.LEFT_MOTOR,
                Constants.TALON_TIMEOUT,
                Constants.Shooter.LEFT_PID_CONSTANTS,
                TalonFXInvertType.Clockwise,
                NeutralMode.Coast
        );
        rightMotor = TalonFXFactory.getInstance().createDefaultPIDTalonFX(
                Ports.Shooter.RIGHT_MOTOR,
                Constants.TALON_TIMEOUT,
                Constants.Shooter.RIGHT_PID_CONSTANTS,
                TalonFXInvertType.Clockwise,
                NeutralMode.Coast
        );
    }

    public static Shooter getDefaultInstance() {
        if (DEFAULT_INSTANCE == null) {
            DEFAULT_INSTANCE = new Shooter(false, false);
        }
        return DEFAULT_INSTANCE;
    }

    public static Shooter getTuneLeftInstance() {
        if (TUNE_LEFT_INSTANCE == null) {
            TUNE_LEFT_INSTANCE = new Shooter(true, false);
        }
        return TUNE_LEFT_INSTANCE;
    }

    public static Shooter getTuneRightInstance() {
        if (TUNE_RIGHT_INSTANCE == null) {
            TUNE_RIGHT_INSTANCE = new Shooter(false, true);
        }
        return TUNE_RIGHT_INSTANCE;
    }

    public double getSetpoint() {
        return setpoint;
    }

    @Override
    public void periodic() {
        if (Utils.deadband(getLeftVelocity() - getRightVelocity(), 1) != 0) {
            System.out.println("Shooter : large difference in velocity between slave and master!");
        }

        leftMotor.config_kP(0, left_webKp.get());
        leftMotor.config_kI(0, left_webKi.get());
        leftMotor.config_kD(0, left_webKd.get());
        leftMotor.config_kF(0, left_webKf.get());

        rightMotor.config_kP(0, right_webKp.get());
        rightMotor.config_kI(0, right_webKi.get());
        rightMotor.config_kD(0, right_webKd.get());
        rightMotor.config_kF(0, right_webKf.get());
    }

    @Override
    public double getPower() {
        return leftMotor.get();
    }

    @Override
    public void setPower(double output) {
        leftMotor.set(ControlMode.PercentOutput, output);
    }

    @Override
    public double getVelocity() {
        return ((getLeftVelocity() + getRightVelocity()) / 2) * 60;
    }

    @Override
    public void setVelocity(double velocity) {
        if (tuneLeft) {
            leftMotor.set(ControlMode.Velocity, unitModel.toTicks100ms(velocity / 60));
            rightMotor.neutralOutput();
        } else if (tuneRight) {
            rightMotor.set(ControlMode.Velocity, unitModel.toTicks100ms(velocity / 60));
            leftMotor.neutralOutput();
        } else {
            rightMotor.set(ControlMode.Velocity, unitModel.toTicks100ms(velocity / 60));
            leftMotor.set(ControlMode.Velocity, unitModel.toTicks100ms(velocity / 60));
        }
        setpoint = velocity;
    }

    private double getLeftVelocity() {
        double leftVelocityRps = unitModel.toVelocity(leftMotor.getSelectedSensorVelocity());
        return leftVelocityRps * 60;
    }

    private double getRightVelocity() {
        double rightVelocityRps = unitModel.toVelocity(rightMotor.getSelectedSensorVelocity());
        return rightVelocityRps * 60;
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Shooter : velocity [" + getUnitType().name + "]", getVelocity());
        FireLog.log("Shooter : velocity [" + getUnitType().name + "]", getVelocity());
    }

    @Override
    public Units.Types getUnitType() {
        return Constants.Shooter.DEFAULT_UNIT_TYPE;
    }
}
