package frc.robot.subsystems.flap;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Ports;
import frc.robot.subsystems.LoggedSubsystem;

public class Flap extends LoggedSubsystem {
    private static Flap INSTANCE = null;
    private final Solenoid mechanism;

    private final FlapLogInputs inputs = FlapLogInputs.getInstance();

    private Flap() {
        super(FlapLogInputs.getInstance());
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

    public void toggle() {
        mechanism.toggle();
    }

    @Override
    public void updateInputs() {
        inputs.modeName = getMode();
        inputs.modeInt = getMode().value ? 1 : 0;
    }

    @Override
    public String getSubsystemName() {
        return null;
    }

    public enum Mode {
        ALLOW_SHOOTING(Ports.Flap.IS_MECHANISM_INVERTED),
        DISALLOW_SHOOTING(!Ports.Flap.IS_MECHANISM_INVERTED);

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

        public static Mode of(String value) {
            if (value.equals(DISALLOW_SHOOTING.name())) {
                return DISALLOW_SHOOTING;
            }
            return ALLOW_SHOOTING;
        }
    }
}
