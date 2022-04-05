package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.MotorSubsystem;
import frc.robot.subsystems.PeriodicSubsystem;
import frc.robot.utils.*;
import webapp.FireLog;

public class Shooter implements PeriodicSubsystem, MotorSubsystem {
    private static Shooter INSTANCE = null;

    private final TalonFX mainMotor;
    private final TalonFX slaveMotor;

    private Shooter() {
        mainMotor = TalonFXFactory.getInstance().createDefaultPIDTalonFX(
                Ports.Shooter.MAIN_MOTOR,
                Constants.TALON_TIMEOUT,
                new PIDConstants(1, 0, 0, 0),
                TalonFXInvertType.Clockwise,
                NeutralMode.Coast
        );
        slaveMotor = TalonFXFactory.getInstance().createDefaultSlaveTalonFX(
                mainMotor, Ports.Shooter.SLAVE_MOTOR, true, NeutralMode.Coast);
    }

    public static Shooter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Shooter();
        }
        return INSTANCE;
    }

    @Override
    public void periodic() {
        double mainVelocity = getVelocity();
        double auxVelocity = new UnitObject(
                Units.Types.TICKS_PER_100MS, slaveMotor.getSelectedSensorVelocity(), Constants.Shooter.TICKS_PER_ROTATION).getRps();

        if (Utils.deadband(mainVelocity - auxVelocity, 1) != 0) {
            System.out.println("Shooter : large difference in velocity between slave and master!");
        }
    }

    @Override
    public double getPower() {
        return mainMotor.getMotorOutputPercent();
    }

    @Override
    public void setPower(double output) {
        mainMotor.set(ControlMode.PercentOutput, output);
    }

    @Override
    public double getVelocity() {
        return new UnitObject(Units.Types.TICKS_PER_100MS, slaveMotor.getSelectedSensorVelocity(), Constants.Shooter.TICKS_PER_ROTATION).getRps();
    }

    @Override
    public void setVelocity(double velocity) {
        mainMotor.set(ControlMode.Velocity, velocity);
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
