package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Ports;
import frc.robot.subsystems.MotorSubsystem;
import frc.robot.subsystems.PeriodicSubsystem;
import frc.robot.utils.TalonFXFactory;
import webapp.FireLog;

public class Intake implements PeriodicSubsystem, MotorSubsystem {
    private static Intake INSTANCE = null;
    private final WPI_TalonFX motor;
    private final Solenoid retractingMechanism;

    private Intake() {
        motor = TalonFXFactory.getInstance().createSimpleTalonFX(Ports.Intake.MOTOR, Ports.Intake.INVERT_TYPE, NeutralMode.Brake);
        retractingMechanism = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Intake.SOLENOID);
    }

    public static Intake getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Intake();
        }
        return INSTANCE;
    }

    public void open() {
        retractingMechanism.set(!Ports.Intake.IS_SOLENOID_INVERTED);
    }

    public void close() {
        retractingMechanism.set(Ports.Intake.IS_SOLENOID_INVERTED);
    }

    public void toggleMechanism() {
        retractingMechanism.toggle();
    }

    @Override
    public double getPower() {
        return motor.get();
    }

    @Override
    public void setPower(double output) {
        motor.set(ControlMode.PercentOutput, output);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Intake power", getPower());
        FireLog.log("Intake power", getPower());
    }
}
