package frc.robot.subsystems.drivetrain.commands;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Infrastructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.ShootAndDriveUtil;
import frc.robot.utils.Utils;

public class HolonomicDriveVirtualTarget extends HolonomicDrive {

    public HolonomicDriveVirtualTarget(SwerveDrive swerve) {
        super(swerve);
    }

    @Override
    public void execute() {
        double vx = Infrastructure.getInstance().chassisGetLeftY() * Utils.boolToInt(Constants.ChassisUIControl.IS_LEFT_X_INVERTED) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double vy = Infrastructure.getInstance().chassisGetLeftX() * Utils.boolToInt(Constants.ChassisUIControl.IS_LEFT_Y_INVERTED) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double theta = Infrastructure.getInstance().chassisGetRightX() * Utils.boolToInt(Constants.ChassisUIControl.IS_RIGHT_X_INVERTED) *
                Constants.SwerveDrive.HOLONOMIC_ANGLE_CONSTRAINTS.maxVelocity;

        var currentSpeeds = swerve.getChassisSpeeds();

        if (Infrastructure.getInstance().chassisGetRightTrigger()) {
            double yaw = ShootAndDriveUtil.getVirtualYaw();
            theta = adjustController.calculate(Robot.getAngle().getRadians(), Robot.getAngle().getRadians() - yaw);
        } else {
            theta = thetaController.calculate(currentSpeeds.omegaRadiansPerSecond, theta);
        }

        double multiplier = Infrastructure.getInstance().chassisGetLeftTrigger() || Infrastructure.getInstance().chassisGetRightTrigger() ?
                0.5 * Constants.SwerveDrive.VELOCITY_MULTIPLIER :
                Constants.SwerveDrive.VELOCITY_MULTIPLIER;
        vx = velocityXController.calculate(multiplier * currentSpeeds.vxMetersPerSecond, vx);
        vy = velocityYController.calculate(multiplier * currentSpeeds.vyMetersPerSecond, vy);

        swerve.defaultHolonomicDrive(vx, vy, theta);
    }
}
