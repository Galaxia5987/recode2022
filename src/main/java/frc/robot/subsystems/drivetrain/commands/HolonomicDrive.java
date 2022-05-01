package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Infrastructure;
import frc.robot.Robot;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDrive extends CommandBase {
    protected final SwerveDrive swerve;
    protected final boolean usingSmoothing;
    protected final ProfiledPIDController velocityXController;
    protected final ProfiledPIDController velocityYController;
    protected final ProfiledPIDController thetaController;
    protected final PIDController adjustController;
    protected ChassisSpeeds currentSpeeds;

    public HolonomicDrive(SwerveDrive swerve, boolean usingSmoothing) {
        this.swerve = swerve;
        this.usingSmoothing = usingSmoothing;

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

        addRequirements(swerve);
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
        double yaw = Superstructure.getInstance().getYawFromTarget();
        calculatePID(desiredSpeeds, Infrastructure.getInstance().chassisGetRightTrigger(), yaw);

        double multiplier = Infrastructure.getInstance().chassisGetLeftTrigger() || Infrastructure.getInstance().chassisGetRightTrigger() ?
                0.5 * Constants.SwerveDrive.VELOCITY_MULTIPLIER :
                Constants.SwerveDrive.VELOCITY_MULTIPLIER;

        smoothing(desiredSpeeds);
        swerve.defaultHolonomicDrive(multiplier * vx, multiplier * vy, theta);
    }

    protected void smoothing(ChassisSpeeds speeds) {
        if (usingSmoothing) {
            speeds.vxMetersPerSecond = Utils.swerveSmoothing(speeds.vxMetersPerSecond / 4, Constants.SwerveDrive.NEUTRAL_VELOCITY_DEADBAND);
            speeds.vyMetersPerSecond = Utils.swerveSmoothing(speeds.vyMetersPerSecond / 4, Constants.SwerveDrive.NEUTRAL_VELOCITY_DEADBAND);
        }
    }

    protected void calculatePID(ChassisSpeeds desiredSpeeds, boolean isAdjusting, double yaw) {
        if (isAdjusting) {
            desiredSpeeds.omegaRadiansPerSecond = adjustController.calculate(Robot.getAngle().getRadians(), Robot.getAngle().getRadians() - yaw);
        } else {
            desiredSpeeds.omegaRadiansPerSecond = thetaController.calculate(currentSpeeds.omegaRadiansPerSecond, desiredSpeeds.omegaRadiansPerSecond);
        }
        desiredSpeeds.vxMetersPerSecond = velocityXController.calculate(currentSpeeds.vxMetersPerSecond, desiredSpeeds.vxMetersPerSecond);
        desiredSpeeds.vyMetersPerSecond = velocityYController.calculate(currentSpeeds.vyMetersPerSecond, desiredSpeeds.vyMetersPerSecond);
    }
}
