package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDrive extends CommandBase {
    private final SwerveDrive swerve;
    private final XboxController xboxController;
    private final ProfiledPIDController velocityXController;
    private final ProfiledPIDController velocityYController;
    private final ProfiledPIDController thetaController;

    public HolonomicDrive(SwerveDrive swerve, XboxController xboxController) {
        this.swerve = swerve;
        this.xboxController = xboxController;

        velocityXController = new ProfiledPIDController(
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_KP, 0, 0,
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS
        );
        velocityYController = new ProfiledPIDController(
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_KP, 0, 0,
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS
        );
        thetaController = new ProfiledPIDController(
                Constants.SwerveDrive.HOLONOMIC_ANGLE_KP, 0, 0,
                Constants.SwerveDrive.HOLONOMIC_ANGLE_CONSTRAINTS
        );
    }

    @Override
    public void execute() {
        double vx = xboxController.getLeftX() * Utils.boolToInt(Constants.UIControl.isLeftXInverted) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double vy = xboxController.getLeftX() * Utils.boolToInt(Constants.UIControl.isLeftYInverted) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double theta = xboxController.getRightX() * Utils.boolToInt(Constants.UIControl.isRightXInverted) *
                Constants.SwerveDrive.HOLONOMIC_ANGLE_CONSTRAINTS.maxVelocity;

        int signVx = (int) Math.signum(vx);
        int signVy = (int) Math.signum(vy);
        int signTheta = (int) Math.signum(theta);

        var currentSpeeds = swerve.getChassisSpeeds();

        vx = velocityXController.calculate(currentSpeeds.vxMetersPerSecond, Math.abs(vx));
        vy = velocityYController.calculate(currentSpeeds.vyMetersPerSecond, Math.abs(vy));
        theta = thetaController.calculate(currentSpeeds.omegaRadiansPerSecond, Math.abs(theta));

        swerve.defaultHolonomicDrive(vx * signVx, vy * signVy, theta * signTheta);
    }
}
