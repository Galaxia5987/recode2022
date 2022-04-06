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

    public void setMode(boolean open) {
        if (open) {
            mechanism.set(Mode.OPEN.value);
        } else {
            mechanism.set(Mode.CLOSED.value);
        }
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putString("Flap mode", getMode().name());
    }

    public enum Mode {
        OPEN(!Ports.Flap.IS_MECHANISM_INVERTED),
        CLOSED(Ports.Flap.IS_MECHANISM_INVERTED);

        public final boolean value;

        Mode(boolean value) {
            this.value = value;
        }

        public static Mode of(boolean value) {
            if (value == OPEN.value) {
                return OPEN;
            }
            return CLOSED;
        }
    }
}
