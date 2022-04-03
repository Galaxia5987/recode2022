package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.MotorSubsystem;
import frc.robot.subsystems.PeriodicSubsystem;
import frc.robot.utils.PIDConstants;
import frc.robot.utils.TalonFXFactory;
import frc.robot.utils.Units;
import frc.robot.utils.Utils;
import webapp.FireLog;

public class Shooter implements
        PeriodicSubsystem,
        MotorSubsystem {
    private static Shooter INSTANCE = null;

    private final TalonFX mainMotor;
    private final TalonFX slaveMotor;

    private Shooter() {
        mainMotor = TalonFXFactory.getInstance().createDefaultPIDTalon(
                0,
                Constants.TALON_TIMEOUT,
                new PIDConstants(1, 0, 0, 0),
                TalonFXInvertType.Clockwise
        );
        slaveMotor = TalonFXFactory.getInstance().createDefaultSlaveTalon(mainMotor, 1, true);
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
        double auxVelocity = Constants.Shooter.getUnitModelOutput(Units.Types.RPM, slaveMotor.getSelectedSensorVelocity());

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
        return Constants.Shooter.getUnitModelOutput(Units.Types.RPM, mainMotor.getSelectedSensorVelocity());
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
