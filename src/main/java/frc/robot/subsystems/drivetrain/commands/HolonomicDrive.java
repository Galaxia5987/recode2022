package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDrive extends CommandBase {
    private final SwerveDrive swerve;
    private final XboxController xboxController;
    private final ProfiledPIDController bakarMehirut;
    private final ProfiledPIDController bakarZavit;

    public HolonomicDrive(SwerveDrive swerve, XboxController xboxController) {
        this.swerve = swerve;
        this.xboxController = xboxController;

        bakarMehirut = new ProfiledPIDController(
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_KP, 0, 0,
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS
        );
        bakarZavit = new ProfiledPIDController(
                Constants.SwerveDrive.HOLONOMIC_ANGLE_KP, 0, 0,
                Constants.SwerveDrive.HOLONOMIC_ANGLE_CONSTRAINTS
        );
    }

    @Override
    public void execute() {
        double vx = xboxController.getLeftX() * Utils.boolToInt(Constants.UIControl.isLeftXInverted);
        double vy = xboxController.getLeftX() * Utils.boolToInt(Constants.UIControl.isLeftYInverted);
        double theta = xboxController.getRightX() * Utils.boolToInt(Constants.UIControl.isRightXInverted);

        int signVx = (int) Math.signum(vx);
        int signVy = (int) Math.signum(vy);
        int signTheta = (int) Math.signum(theta);

        vx = bakarMehirut.calculate(Math.abs(vx));
        vy = bakarMehirut.calculate(Math.abs(vy));
        theta = bakarZavit.calculate(Math.abs(theta));

        swerve.defaultHolonomicDrive(vx * signVx, vy * signVy, theta * signTheta);
    }
}
