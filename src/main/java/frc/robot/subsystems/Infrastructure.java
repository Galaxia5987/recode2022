package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.commands.HolonomicDrive;

public class Infrastructure extends Superstructure {
    private static Infrastructure INSTANCE = null;
    private final XboxController xboxController = new XboxController(0);

    private Infrastructure() {
    }

    private static class PeriodicIO {
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

    public static Infrastructure getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Infrastructure();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        swerve.setDefaultCommand(new HolonomicDrive(swerve));
    }

    public Command getAutonomousCommand() {
        return null;
    }

    public void configureButtonBindings() {

    }

    public boolean getLeftTrigger() {
        return xboxController.getLeftTriggerAxis() > 0.2;
    }

    public boolean getRightTrigger() {
        return xboxController.getRightTriggerAxis() > 0.2;
    }

    public boolean getA() {
        return PeriodicIO.aButton;
    }

    public boolean getB() {
        return PeriodicIO.bButton;
    }

    public boolean getX() {
        return PeriodicIO.xButton;
    }

    public boolean getY() {
        return PeriodicIO.yButton;
    }

    public boolean getLeftBumper() {
        return PeriodicIO.leftBumper;
    }

    public boolean getRightBumper() {
        return PeriodicIO.rightBumper;
    }

    public int getPOV() {
        return PeriodicIO.pov;
    }

    public double getRightX() {
        return PeriodicIO.rightX;
    }

    public double getLeftX() {
        return PeriodicIO.leftX;
    }

    public double getLeftY() {
        return PeriodicIO.leftY;
    }

    @Override
    public void periodic() {
        super.periodic();

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
