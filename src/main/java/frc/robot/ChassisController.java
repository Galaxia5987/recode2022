package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ChassisController extends Subsystem {
    XboxController xboxController = new XboxController(Ports.UIControl.CHASSIS_XBOX);

    default boolean chassisGetLeftTrigger() {
        return PeriodicIO.leftTrigger;
    }

    default boolean chassisGetRightTrigger() {
        return PeriodicIO.rightTrigger;
    }

    default boolean chassisGetA() {
        return PeriodicIO.aButton;
    }

    default boolean chassisGetB() {
        return PeriodicIO.bButton;
    }

    default boolean chassisGetX() {
        return PeriodicIO.xButton;
    }

    default boolean chassisGetY() {
        return PeriodicIO.yButton;
    }

    default boolean chassisGetLeftBumper() {
        return PeriodicIO.leftBumper;
    }

    default boolean chassisGetRightBumper() {
        return PeriodicIO.rightBumper;
    }

    default int chassisGetPOV() {
        return PeriodicIO.pov;
    }

    default double chassisGetRightX() {
        return PeriodicIO.rightX;
    }

    default double chassisGetLeftX() {
        return PeriodicIO.leftX;
    }

    default double chassisGetLeftY() {
        return PeriodicIO.leftY;
    }

    class PeriodicIO {
        public static boolean leftTrigger = false;
        public static boolean rightTrigger = false;
        public static boolean aButton = false;
        public static boolean bButton = false;
        public static boolean xButton = false;
        public static boolean yButton = false;
        public static boolean leftBumper = false;
        public static boolean rightBumper = false;
        public static double leftX = 0;
        public static double rightX = 0;
        public static double leftY = 0;
        public static double rightY = 0;
        public static int pov = 0;
    }

    @Override
    default void periodic() {
        PeriodicIO.leftTrigger = xboxController.getLeftTriggerAxis() > 0.2;
        PeriodicIO.rightTrigger = xboxController.getRightTriggerAxis() > 0.2;
        PeriodicIO.aButton = xboxController.getAButton();
        PeriodicIO.bButton = xboxController.getBButton();
        PeriodicIO.xButton = xboxController.getXButton();
        PeriodicIO.yButton = xboxController.getYButton();
        PeriodicIO.leftBumper = xboxController.getLeftBumper();
        PeriodicIO.rightBumper = xboxController.getRightBumper();
        PeriodicIO.leftX = xboxController.getLeftX();
        PeriodicIO.rightX = xboxController.getRightX();
        PeriodicIO.leftY = xboxController.getLeftY();
        PeriodicIO.rightY = xboxController.getRightY();
        PeriodicIO.pov = xboxController.getPOV();
    }
}
