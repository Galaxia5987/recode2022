package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import edu.wpi.first.wpilibj.I2C;

public final class Ports { // TODO: Check canvas and all motors during off season

    public static class ExampleSubsystem {
        public static final int MAIN = 0;
        public static final int AUX = 0;
        public static final boolean IS_MAIN_INVERTED = false;
        public static final boolean IS_AUX_INVERTED = false;
        public static final boolean IS_MAIN_SENSOR_INVERTED = false;
        public static final boolean IS_AUX_SENSOR_INVERTED = false;
    }

    public static final class SwerveDrive {
        // front right
        public static final int DRIVE_MOTOR_FR = 1;
        public static final int ANGLE_MOTOR_FR = 2;
        public static final boolean DRIVE_INVERTED_FR = false;
        public static final boolean ANGLE_INVERTED_FR = true;
        public static final boolean ANGLE_SENSOR_PHASE_FR = false;

        // front left
        public static final int DRIVE_MOTOR_FL = 3;
        public static final int ANGLE_MOTOR_FL = 4;
        public static final boolean DRIVE_INVERTED_FL = false;
        public static final boolean ANGLE_INVERTED_FL = true;
        public static final boolean ANGLE_SENSOR_PHASE_FL = false;

        // rear right
        public static final int DRIVE_MOTOR_RR = 5;
        public static final int ANGLE_MOTOR_RR = 6;
        public static final boolean DRIVE_INVERTED_RR = false;
        public static final boolean ANGLE_INVERTED_RR = true;
        public static final boolean ANGLE_SENSOR_PHASE_RR = false;

        // rear left
        public static final int DRIVE_MOTOR_RL = 7;
        public static final int ANGLE_MOTOR_RL = 8;
        public static final boolean DRIVE_INVERTED_RL = false;
        public static final boolean ANGLE_INVERTED_RL = true;
        public static final boolean ANGLE_SENSOR_PHASE_RL = false;
    }

    public static class Shooter {
        public static final int MAIN_MOTOR = 0;
        public static final int SLAVE_MOTOR = 1;
    }

    public static class Conveyor {
        public static final int MOTOR = 0;
        public static final int PRE_FLAP_BEAM = 1;
        public static final int POST_FLAP_BEAM = 2;
        public static final I2C.Port COLOR_SENSOR = I2C.Port.kMXP;
    }

    public static class Intake {
        public static final int MOTOR = 0;
        public static final TalonFXInvertType INVERT_TYPE = TalonFXInvertType.Clockwise;
        public static final int SOLENOID = 1;
        public static final boolean IS_SOLENOID_INVERTED = false;
    }

    public static class Hood {
        public static final int MECHANISM = 0;
        public static final boolean IS_MECHANISM_INVERTED = false;
    }

    public static class Flap {
        public static final int MECHANISM = 0;
        public static final boolean IS_MECHANISM_INVERTED = false;
    }

    public static class UIControl {
        public static final int XBOX = 0;
    }
}
