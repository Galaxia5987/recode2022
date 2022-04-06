package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDrive extends CommandBase {
    protected final SwerveDrive swerve;
    protected final ProfiledPIDController velocityXController;
    protected final ProfiledPIDController velocityYController;
    protected final ProfiledPIDController thetaController;
    protected final PIDController adjustController;

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
        adjustController = new PIDController(Constants.SwerveDrive.HEADING_KP, 0, 0);
        adjustController.setTolerance(Constants.SwerveDrive.ALLOWABLE_HEADING_ERROR);
        adjustController.enableContinuousInput(-Math.PI, Math.PI);
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

        if (Infrastructure.getInstance().getRightTrigger()) {
            double yaw = Superstructure.getInstance().getYawFromTarget();
            theta = adjustController.calculate(Robot.getAngle().getRadians(), Robot.getAngle().getRadians() - yaw);
        } else {
            theta = thetaController.calculate(currentSpeeds.omegaRadiansPerSecond, theta);
        }

        vx = velocityXController.calculate(currentSpeeds.vxMetersPerSecond, vx);
        vy = velocityYController.calculate(currentSpeeds.vyMetersPerSecond, vy);

        swerve.defaultHolonomicDrive(vx, vy, theta);
    }
}
