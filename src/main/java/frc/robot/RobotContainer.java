package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {
    private static RobotContainer INSTANCE = null;

    private RobotContainer() {
    }

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
    }

    public Command getAutonomousCommand() {
        return null;
    }
}
