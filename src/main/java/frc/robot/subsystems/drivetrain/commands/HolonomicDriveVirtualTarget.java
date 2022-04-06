package frc.robot.subsystems.drivetrain.commands;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.ShootAndDriveUtil;
import frc.robot.utils.Utils;

public class HolonomicDriveVirtualTarget extends HolonomicDrive {

    public HolonomicDriveVirtualTarget(SwerveDrive swerve) {
        super(swerve);
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
            double yaw = ShootAndDriveUtil.getVirtualYaw();
            theta = adjustController.calculate(Robot.getAngle().getRadians(), Robot.getAngle().getRadians() - yaw);
        } else {
            theta = thetaController.calculate(currentSpeeds.omegaRadiansPerSecond, theta);
        }

        vx = velocityXController.calculate(currentSpeeds.vxMetersPerSecond, vx);
        vy = velocityYController.calculate(currentSpeeds.vyMetersPerSecond, vy);

        swerve.defaultHolonomicDrive(vx, vy, theta);
    }
}
