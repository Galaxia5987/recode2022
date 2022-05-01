package frc.robot.subsystems.helicopter;

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
import webapp.FireLog;

public class Helicopter implements PeriodicSubsystem, MotorSubsystem {
    private static Helicopter INSTANCE = null;
    private final WPI_TalonFX masterMotor;
    private final WPI_TalonFX slaveMotor;
    private final ArmController controller;
    private final UnitModel unitModel = new UnitModel(Constants.Helicopter.TICKS_PER_RADIAN);

    private Helicopter() {
        masterMotor = TalonFXFactory.getInstance().createSimpleTalonFX(
                Ports.Helicopter.MASTER_MOTOR,
                TalonFXInvertType.Clockwise,
                NeutralMode.Brake
        );
        slaveMotor = TalonFXFactory.getInstance().createDefaultSlaveTalonFX(
                masterMotor,
                Ports.Helicopter.SLAVE_MOTOR,
                Ports.Helicopter.OPPOSING_MASTER,
                NeutralMode.Brake
        );
        controller = new ArmController(
                Constants.Helicopter.PID_CONSTANTS,
                Constants.Helicopter.MAX_VELOCITY,
                Constants.Helicopter.MAX_ACCELERATION
        );
    }

    public static Helicopter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Helicopter();
        }
        return INSTANCE;
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Helicopter angle", getAngle());
        FireLog.log("Helicopter angle", getAngle());
    }

    @Override
    public double getPower() {
        return masterMotor.get();
    }

    @Override
    public void setPower(double output) {
        masterMotor.set(ControlMode.PercentOutput, output);
    }

    public double getAngle() {
        return unitModel.toUnits(masterMotor.getSelectedSensorPosition());
    }

    public void setAngle(double angle) {
        masterMotor.set(ControlMode.Position, unitModel.toTicks(controller.calculate(getAngle(), angle)));
    }
}
