package frc.robot.subsystems.hood;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Ports;
import frc.robot.subsystems.PeriodicSubsystem;

public class Hood implements PeriodicSubsystem {
    private static Hood INSTANCE = null;
    private final Solenoid mechanism;

    private Hood() {
        mechanism = new Solenoid(PneumaticsModuleType.CTREPCM, Ports.Hood.MECHANISM);
    }

    public static Hood getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Hood();
        }
        return INSTANCE;
    }

    public Mode getMode() {
        return Mode.of(mechanism.get());
    }

    public void setMode(boolean isLongDistance) {
        if (isLongDistance) {
            mechanism.set(Mode.LONG_DISTANCE.value);
        } else {
            mechanism.set(Mode.SHORT_DISTANCE.value);
        }
    }

    public void toggle() {
        mechanism.toggle();
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putString("Hood mode", getMode().name());
    }

    public enum Mode {
        LONG_DISTANCE(!Ports.Hood.IS_MECHANISM_INVERTED),
        SHORT_DISTANCE(Ports.Hood.IS_MECHANISM_INVERTED);

        public final boolean value;

        Mode(boolean value) {
            this.value = value;
        }

        public static Mode of(boolean value) {
            if (value == LONG_DISTANCE.value) {
                return LONG_DISTANCE;
            }
            return SHORT_DISTANCE;
        }
    }
}
