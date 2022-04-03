package frc.robot;

public final class Ports {
    public static class ExampleSubsystem {
        public static final int MAIN = 0;
        public static final int AUX = 0;
        public static final boolean IS_MAIN_INVERTED = false;
        public static final boolean IS_AUX_INVERTED = false;
        public static final boolean IS_MAIN_SENSOR_INVERTED = false;
        public static final boolean IS_AUX_SENSOR_INVERTED = false;
    }

    public static class Controls {
        public static final int XBOX = 0;
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
}
