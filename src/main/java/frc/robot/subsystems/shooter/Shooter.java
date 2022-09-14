package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.subsystems.UnitModel;
import frc.robot.utils.TalonFXFactory;
import frc.robot.utils.Utils;
import frc.robot.valuetuner.WebConstant;

public class Shooter extends LoggedSubsystem {
    private static Shooter INSTANCE = null;

    private final WPI_TalonFX master;
    private final WPI_TalonFX slave;

    private final WebConstant webKp = WebConstant.of("Shooter", "kP", Constants.Shooter.PID_CONSTANTS.kP);
    private final WebConstant webKi = WebConstant.of("Shooter", "kI", Constants.Shooter.PID_CONSTANTS.kI);
    private final WebConstant webKd = WebConstant.of("Shooter", "kD", Constants.Shooter.PID_CONSTANTS.kD);
    private final WebConstant webKf = WebConstant.of("Shooter", "kF", Constants.Shooter.PID_CONSTANTS.kF);

    private final UnitModel unitModel = new UnitModel(Constants.Shooter.TICKS_PER_ROTATION);
    private final ShooterLogInputs inputs = ShooterLogInputs.getInstance();
    private double setpoint = 0;

    private Shooter() {
        super(ShooterLogInputs.getInstance());

        master = TalonFXFactory.getInstance().createDefaultPIDTalonFX(
                Ports.Shooter.MAIN_MOTOR,
                Constants.TALON_TIMEOUT,
                Constants.Shooter.PID_CONSTANTS,
                TalonFXInvertType.Clockwise,
                NeutralMode.Coast
        );
        slave = TalonFXFactory.getInstance().createDefaultPIDTalonFX(
                Ports.Shooter.AUX_MOTOR,
                Constants.TALON_TIMEOUT,
                Constants.Shooter.PID_CONSTANTS,
                TalonFXInvertType.Clockwise,
                NeutralMode.Coast
        );
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

    @Override
    public void periodic() {
        master.config_kP(0, webKp.get());
        master.config_kI(0, webKi.get());
        master.config_kD(0, webKd.get());
        master.config_kF(0, webKf.get());
    }

    @Override
    public void updateInputs() {
        inputs.currentAmps = new double[]{master.getSupplyCurrent(), slave.getSupplyCurrent()};
        inputs.tempCelsius = new double[]{master.getTemperature(), slave.getTemperature()};
        inputs.appliedVolts = (master.getMotorOutputVoltage() + slave.getMotorOutputVoltage()) / 2;
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
