package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class DefaultDrive extends CommandBase {
    private final SwerveDrive swerveDrive = SwerveDrive.getInstance();
    private final XboxController xboxController;

    public DefaultDrive(XboxController xboxController) {
        this.xboxController = xboxController;
    }

    @Override
    public void execute() {
        swerveDrive.defaultHolonomicDrive(-xboxController.getLeftY(), xboxController.getLeftX(), xboxController.getRightX());
    }
}
