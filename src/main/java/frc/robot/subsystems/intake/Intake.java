package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.utils.TalonFXFactory;
import org.littletonrobotics.junction.Logger;
import webapp.FireLog;

public class Intake extends LoggedSubsystem {
    private static Intake INSTANCE = null;
    private final WPI_TalonFX motor;
    private final Solenoid retractingMechanism;
    private final IntakeLogInputs inputs = IntakeLogInputs.getInstance();

    private Intake() {
        super(IntakeLogInputs.getInstance());
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

    public double getPower() {
        return motor.get();
    }

    public void setPower(double output) {
        motor.set(ControlMode.PercentOutput, output);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake power", getPower());
        FireLog.log("Intake power", getPower());

        updateInputs();
    }

    @Override
    public void updateInputs() {
        inputs.appliedVoltage = motor.getBusVoltage();
        inputs.tempCelsius = motor.getTemperature();
        inputs.current = motor.getSupplyCurrent();
        inputs.output = motor.get();

        Logger.getInstance().processInputs("Intake subsystem", inputs);
    }

    @Override
    public String getSubsystemName() {
        return "Intake";
    }
}
