package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Constants;
import frc.robot.Infrastructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.ShootAndDriveUtil;
import frc.robot.utils.Utils;

public class HolonomicDriveVirtualTarget extends HolonomicDrive {

    public HolonomicDriveVirtualTarget(SwerveDrive swerve, boolean usingSmoothing) {
        super(swerve, usingSmoothing);
    }

    @Override
    public void execute() {
        double vx = Infrastructure.getInstance().chassisGetLeftY() * Utils.boolToInt(Constants.ChassisUIControl.IS_LEFT_X_INVERTED) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double vy = Infrastructure.getInstance().chassisGetLeftX() * Utils.boolToInt(Constants.ChassisUIControl.IS_LEFT_Y_INVERTED) *
                Constants.SwerveDrive.HOLONOMIC_VELOCITY_CONSTRAINTS.maxVelocity;
        double theta = Infrastructure.getInstance().chassisGetRightX() * Utils.boolToInt(Constants.ChassisUIControl.IS_RIGHT_X_INVERTED) *
                Constants.SwerveDrive.HOLONOMIC_ANGLE_CONSTRAINTS.maxVelocity;

        currentSpeeds = swerve.getChassisSpeeds();
        ChassisSpeeds desiredSpeeds = new ChassisSpeeds(vx, vy, theta);
        double yaw = ShootAndDriveUtil.getVirtualYaw();
        calculatePID(desiredSpeeds, Infrastructure.getInstance().chassisGetRightTrigger(), yaw);

        double multiplier = Infrastructure.getInstance().chassisGetLeftTrigger() || Infrastructure.getInstance().chassisGetRightTrigger() ?
                0.5 * Constants.SwerveDrive.VELOCITY_MULTIPLIER :
                Constants.SwerveDrive.VELOCITY_MULTIPLIER;

        smoothing(desiredSpeeds);
        swerve.defaultHolonomicDrive(multiplier * vx, multiplier * vy, theta);
    }
}
