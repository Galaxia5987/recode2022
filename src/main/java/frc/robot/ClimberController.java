package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ClimberController extends Subsystem {
    XboxController xboxController = new XboxController(Ports.UIControl.CHASSIS_XBOX);

    default boolean climberGetLeftTrigger() {
        return PeriodicIO.leftTrigger;
    }

    default boolean climberGetRightTrigger() {
        return PeriodicIO.rightTrigger;
    }

    default boolean climberGetA() {
        return PeriodicIO.aButton;
    }

    default boolean climberGetB() {
        return PeriodicIO.bButton;
    }

    default boolean climberGetX() {
        return PeriodicIO.xButton;
    }

    default boolean climberGetY() {
        return PeriodicIO.yButton;
    }

    default boolean climberGetLeftBumper() {
        return PeriodicIO.leftBumper;
    }

    default boolean climberGetRightBumper() {
        return PeriodicIO.rightBumper;
    }

    default int climberGetPOV() {
        return PeriodicIO.pov;
    }

    default double climberGetRightX() {
        return PeriodicIO.rightX;
    }

    default double climberGetLeftX() {
        return PeriodicIO.leftX;
    }

    default double climberGetLeftY() {
        return PeriodicIO.leftY;
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
}
