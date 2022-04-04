package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.commands.HolonomicDrive;

public class Infrastructure extends Superstructure {
    private static Infrastructure INSTANCE = null;

    private Infrastructure() {
    }

    public static Infrastructure getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Infrastructure();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        swerve.setDefaultCommand(new HolonomicDrive(swerve, xboxController));
    }

    public Command getAutonomousCommand() {
        return null;
    }

    public void configureButtonBindings() {

    }
}
