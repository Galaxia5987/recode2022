package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDrive extends CommandBase {
    private final SwerveDrive swerve;
    private final ProfiledPIDController velocityXController;
    private final ProfiledPIDController velocityYController;
    private final ProfiledPIDController thetaController;

    public HolonomicDrive(SwerveDrive swerve) {
        this.swerve = swerve;

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
        double vx = Infrastructure.getInstance().getLeftX() * Utils.boolToInt(Constants.UIControl.isLeftXInverted) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double vy = Infrastructure.getInstance().getLeftX() * Utils.boolToInt(Constants.UIControl.isLeftYInverted) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double theta = Infrastructure.getInstance().getRightX() * Utils.boolToInt(Constants.UIControl.isRightXInverted) *
                Constants.SwerveDrive.HOLONOMIC_ANGLE_CONSTRAINTS.maxVelocity;

        var currentSpeeds = swerve.getChassisSpeeds();

        vx = velocityXController.calculate(currentSpeeds.vxMetersPerSecond, vx);
        vy = velocityYController.calculate(currentSpeeds.vyMetersPerSecond, vy);
        theta = thetaController.calculate(currentSpeeds.omegaRadiansPerSecond, Math.abs(theta));

        swerve.defaultHolonomicDrive(vx, vy, theta);
    }
}
