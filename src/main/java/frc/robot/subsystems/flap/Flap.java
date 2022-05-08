package frc.robot.subsystems.flap;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Ports;
import frc.robot.subsystems.PeriodicSubsystem;

public class Flap implements PeriodicSubsystem {
    private static Flap INSTANCE = null;
    private final Solenoid mechanism;

    private Flap() {
        mechanism = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Flap.MECHANISM);
    }

    public static Flap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Flap();
        }
        return INSTANCE;
    }

    public Mode getMode() {
        return Mode.of(mechanism.get());
    }

    public void setMode(boolean allowShooting) {
        if (allowShooting) {
            mechanism.set(Mode.ALLOW_SHOOTING.value);
        } else {
            mechanism.set(Mode.DISALLOW_SHOOTING.value);
        }
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putString("Flap mode", getMode().name());
    }

    public enum Mode {
        DISALLOW_SHOOTING(!Ports.Flap.IS_MECHANISM_INVERTED),
        ALLOW_SHOOTING(Ports.Flap.IS_MECHANISM_INVERTED);

        public final boolean value;

        Mode(boolean value) {
            this.value = value;
        }

        public static Mode of(boolean value) {
            if (value == DISALLOW_SHOOTING.value) {
                return DISALLOW_SHOOTING;
            }
            return ALLOW_SHOOTING;
        }
    }
}
