package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;

public class Intake extends LoggedSubsystem {
    private static Intake INSTANCE = null;
    private final WPI_VictorSPX motor;
    private final Solenoid retractingMechanism;
    private final IntakeLogInputs inputs = IntakeLogInputs.getInstance();

    private Intake() {
        super(IntakeLogInputs.getInstance());

        motor = new WPI_VictorSPX(Ports.Intake.MOTOR);
        motor.setInverted(true);
        motor.setNeutralMode(NeutralMode.Brake);

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

    }

    @Override
    public void updateInputs() {
        inputs.appliedVoltage = motor.getBusVoltage();
        inputs.tempCelsius = motor.getTemperature();
//        inputs.current = motor.getSupplyCurrent();
        inputs.output = motor.get();
    }

    @Override
    public String getSubsystemName() {
        return "Intake";
    }
}
