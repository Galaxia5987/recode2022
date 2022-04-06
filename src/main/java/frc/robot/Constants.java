package frc.robot;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.SwerveModuleConfigBase;
import frc.robot.utils.Units;
import frc.robot.valuetuner.WebConstant;
import org.photonvision.SimVisionTarget;

import java.util.HashMap;

import static frc.robot.Ports.SwerveDrive.*;

public final class Constants {
    public static final int TALON_TIMEOUT = 10; // Waiting period for configurations [ms].

    public static final double LOOP_PERIOD = 0.02; // [s]
    public static final double NOMINAL_VOLTAGE = 12; // [volts]
    public static final double FIELD_WIDTH = 8.23; // Width of the field. [m]
    public static final double FIELD_LENGTH = 16.46; // Length of the field. [m]

    public static final boolean ENABLE_VOLTAGE_COMPENSATION = true;
    public static final boolean ENABLE_CURRENT_LIMIT = true;
    public static final Pose2d HUB_POSE = new Pose2d( // Position of the hub relative to the field.
            new Translation2d(FIELD_LENGTH / 2, FIELD_WIDTH / 2), new Rotation2d());


    // The order of modules is ALWAYS front-right (fr), front-left (fl), rear-right (rr), rear-left (rl)
    public static final class SwerveDrive {
        public static final int TICKS_PER_ROTATION_DRIVE_MOTOR = 2048;
        public static final int TICKS_PER_ROTATION_ANGLE_MOTOR = 1024;
        public static final double GEAR_RATIO_DRIVE_MOTOR = 7.5;
        public static final double GEAR_RATIO_ANGLE_MOTOR = 1;
        public static final double DRIVE_MOTOR_TICKS_PER_METER = GEAR_RATIO_DRIVE_MOTOR * TICKS_PER_ROTATION_DRIVE_MOTOR / (0.11 * Math.PI); // 4 * 0.0254
        public static final double ANGLE_MOTOR_TICKS_PER_RADIAN = GEAR_RATIO_ANGLE_MOTOR * TICKS_PER_ROTATION_ANGLE_MOTOR / (2 * Math.PI);

        public static final int MAX_CURRENT = 15; // [amps]

        // State Space
        public static final double VELOCITY_TOLERANCE = 5; // [rps]
        public static final double COST_LQR = 11;
        // Note that the values of MODEL_TOLERANCE and ENCODER_TOLERANCE should be a lot smaller (something like 1e-6)
        public static final double MODEL_TOLERANCE = 0.01;
        public static final double ENCODER_TOLERANCE = 0.01; // [ticks]

        public static final double HEADING_KP = 5;
        public static final double HEADING_KI = 0;
        public static final double HEADING_KD = 0;
        public static final TrapezoidProfile.Constraints HEADING_CONTROLLER_CONSTRAINTS = new TrapezoidProfile.Constraints(10, 5); // [rads/sec], [rad/sec^2]

        // The heading is responsible for the angle of the whole chassis, while the angle is used in the angle motor itself.
        public static final double ALLOWABLE_HEADING_ERROR = Math.toRadians(5); // [rad]
        public static final double ALLOWABLE_ANGLE_ERROR = Math.toRadians(3); // [rad]
        public static final double WHEEL_RADIUS = 0.04688; // [m]

        public static final double ROBOT_LENGTH = 0.6624; // [m]
        public static final double ROBOT_WIDTH = 0.5224; // [m]

        // the rotational velocity of the robot, this constant multiplies the rotation output of the joystick
        public static final int ANGLE_CURVE_STRENGTH = 1;
        public static final int ANGLE_CRUISE_VELOCITY = 400;
        public static final int ANGLE_MOTION_ACCELERATION = 1300;
        public static final double HOLONOMIC_ANGLE_KP = 1;
        public static final TrapezoidProfile.Constraints HOLONOMIC_ANGLE_CONSTRAINTS = new TrapezoidProfile.Constraints(Math.PI, Math.PI / 2);
        public static final double HOLONOMIC_VELOCITY_KP = 1;
        public static final TrapezoidProfile.Constraints HOLONOMIC_VELOCITY_CONSTRAINTS = new TrapezoidProfile.Constraints(4, 2);
        private static final double Rx = SwerveDrive.ROBOT_LENGTH / 2; // [m]
        private static final double Ry = SwerveDrive.ROBOT_WIDTH / 2; // [m]
        // Axis systems
        public static final Translation2d[] SWERVE_POSITIONS = new Translation2d[]{
                new Translation2d(Rx, -Ry),
                new Translation2d(Rx, Ry),
                new Translation2d(-Rx, -Ry),
                new Translation2d(-Rx, Ry)
        };
    }

    public static final class SwerveModule {
        public static final int TRIGGER_THRESHOLD_CURRENT = 2; // [amps]

        public static final double TRIGGER_THRESHOLD_TIME = 0.02; // [secs]
        public static final double RAMP_RATE = 0; // seconds from neutral to max

        // -1612, -840, 1189, 1562
        public static final int[] ZERO_POSITIONS = {-602, 2184, 157, -1481}; // fr, fl, rr, rl

        public static final SwerveModuleConfigBase frConfig = new SwerveModuleConfigBase.Builder(0)
                .configPorts(DRIVE_MOTOR_FR, ANGLE_MOTOR_FR)
                .configInversions(DRIVE_INVERTED_FR, ANGLE_INVERTED_FR, ANGLE_SENSOR_PHASE_FR)
                .configAnglePID(6, 0, 0, 0)
                .configZeroPosition(ZERO_POSITIONS[0])
                .configJ(0.115)
                .build();

        public static final SwerveModuleConfigBase flConfig = new SwerveModuleConfigBase.Builder(1)
                .configPorts(DRIVE_MOTOR_FL, ANGLE_MOTOR_FL)
                .configInversions(DRIVE_INVERTED_FL, ANGLE_INVERTED_FL, ANGLE_SENSOR_PHASE_FL)
                .configAnglePID(6, 0, 0, 0)
                .configZeroPosition(ZERO_POSITIONS[1])
                .configJ(0.115)
                .build();

        public static final SwerveModuleConfigBase rrConfig = new SwerveModuleConfigBase.Builder(2)
                .configPorts(DRIVE_MOTOR_RR, ANGLE_MOTOR_RR)
                .configInversions(DRIVE_INVERTED_RR, ANGLE_INVERTED_RR, ANGLE_SENSOR_PHASE_RR)
                .configAnglePID(6, 0, 0, 0)
                .configZeroPosition(ZERO_POSITIONS[2])
                .configJ(0.115)
                .build();

        public static final SwerveModuleConfigBase rlConfig = new SwerveModuleConfigBase.Builder(3)
                .configPorts(DRIVE_MOTOR_RL, ANGLE_MOTOR_RL)
                .configInversions(DRIVE_INVERTED_RL, ANGLE_INVERTED_RL, ANGLE_SENSOR_PHASE_RL)
                .configAnglePID(6, 0, 0, 0)
                .configZeroPosition(ZERO_POSITIONS[3])
                .configJ(0.115)
                .build();
    }

    public static final class Shooter {
        public static final Units.Types DEFAULT_UNIT_TYPE = Units.Types.RPM;

        public static final double SHOOTER_VELOCITY_DEADBAND = 50;
        public static final int TICKS_PER_ROTATION = 2048;
        public static final double WARMUP_VELOCITY = 3600;

        public static final HashMap<Double, Double> SHORT_MEASUREMENTS = new HashMap<>() {{
            put(-99999.0, 3530.0);
            put(2.3, 3530.0);
            put(2.6, 3600.0);
            put(2.77, 3650.0);
            put(2.95, 3770.0);
            put(3.11, 3800.0);
            put(3.33, 3900.0);
            put(99999.0, 3900.0);
        }};
        public static final HashMap<Double, Double> LONG_MEASUREMENTS = new HashMap<>() {{
            put(-99999.0, 3675.0);
            put(3.33, 3765.0);
            put(3.52, 3900.0);
            put(3.7, 3965.0);
            put(3.92, 4000.0);
            put(4.13, 4070.0);
            put(4.25, 4155.0);
            put(4.48, 4250.0);
            put(4.81, 4398.0);
            put(5.0, 4575.0);
            put(5.25, 4620.0);
            put(5.6, 4795.0);
            put(6.0, 4890.0);
            put(6.41, 5060.0);
            put(99999.0, 5060.0);
        }};

        public static double distanceToTimeOfFlight(double distance) {
            return 0.1959 * distance + 0.4946;
        }
    }

    public static class Vision { //TODO: change for competition
        public static final int CAM_RESOLUTION_HEIGHT = 480; // Height of camera resolution. [pixel]
        public static final int CAM_RESOLUTION_WIDTH = 640; // Width of camera resolution. [pixel]

        public static final double CAMERA_HEIGHT = 0.73; // [m]
        public static final double TARGET_HEIGHT_FROM_GROUND = 2.62; // [m] Pefzener 2.62
        public static final double BIT_CAMERA_HEIGHT = 1.2; // [m]
        public static final double BIT_TARGET_HEIGHT_FROM_GROUND = 2.1; // [m] Pefzener 2.62
        public static final double CAMERA_PITCH = 36.2; // Pitch of the vision. [deg]
        public static final double DIAG_FOV = 75; // Diagonal FOV. [deg]
        public static final double LED_RANGE = 6; // Visible range of LEDs. [m]
        public static final double MIN_TARGET_AREA = 10; // Minimal area of target. [pixel^2]
        public static final double TARGET_WIDTH = 1.36; // Width of vision target strip. [m]
        public static final double TARGET_RADIUS = 0.678; // [m]

        public static final Pose2d HUB_POSE = new Pose2d( // Position of the hub relative to the field.
                new Translation2d(FIELD_LENGTH / 2, FIELD_WIDTH / 2), new Rotation2d());
        public static final Transform2d CAMERA_TO_ROBOT = new Transform2d(
                new Translation2d(0.038, 0.171), new Rotation2d()); // Position of the vision relative to the robot.

        public static final SimVisionTarget SIM_TARGET_HUB = new SimVisionTarget( // Hub target for vision simulation.
                HUB_POSE, TARGET_HEIGHT_FROM_GROUND, TARGET_WIDTH, TARGET_HEIGHT_FROM_GROUND);
    }

    public static class Conveyor {
        public static final Color BLUE = new Color(0, 0, 0);
        public static final Color RED = new Color(0, 0, 0);
        public static final Color NONE = new Color(0, 0, 0);
        public static final WebConstant DEFAULT_POWER = WebConstant.of("Conveyor", "Power", 0.8);
    }

    public static class UIControl {
        public static final boolean isLeftXInverted = false;
        public static final boolean isLeftYInverted = true;
        public static final boolean isRightXInverted = false;
    }

    public static class Hood {
        public static final double DISTANCE_FROM_TARGET_THRESHOLD = 3.33; // [m]
    }

    public static class Intake {
        public static final double DEFAULT_POWER = 0.3;
    }

    public static class ExampleSubsystem {
        public static final double POWER = 0.5; // [%]
    }

}
