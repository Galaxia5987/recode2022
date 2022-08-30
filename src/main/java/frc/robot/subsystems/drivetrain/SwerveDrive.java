package frc.robot.subsystems.drivetrain;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.LoggedSubsystem;
import org.littletonrobotics.junction.Logger;

/**
 * The {@code SwerveDrive} Subsystem is responsible for the integration of modules together in order to move the robot honolomicaly.
 * The class contains several convenient methods for controlling the robot and retrieving information about his state.
 */
public class SwerveDrive extends LoggedSubsystem {
    private static SwerveDrive INSTANCE = null;
    private final SwerveModule[] modules = new SwerveModule[4];
    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(Constants.SwerveDrive.SWERVE_POSITIONS);
    private final SwerveDriveOdometry odometry = new SwerveDriveOdometry(kinematics, new Rotation2d());

    private final ProfiledPIDController headingController = new ProfiledPIDController(
            Constants.SwerveDrive.HEADING_KP,
            Constants.SwerveDrive.HEADING_KI,
            Constants.SwerveDrive.HEADING_KD,
            Constants.SwerveDrive.HEADING_CONTROLLER_CONSTRAINTS
    );

    private final SwerveDriveLogInputs inputs = SwerveDriveLogInputs.getInstance();

    private SwerveDrive() {
        super(SwerveDriveLogInputs.getInstance());
        modules[Constants.SwerveModule.frConfig.wheel()] = new SwerveModule(Constants.SwerveModule.frConfig);
        modules[Constants.SwerveModule.flConfig.wheel()] = new SwerveModule(Constants.SwerveModule.flConfig);
        modules[Constants.SwerveModule.rrConfig.wheel()] = new SwerveModule(Constants.SwerveModule.rrConfig);
        modules[Constants.SwerveModule.rlConfig.wheel()] = new SwerveModule(Constants.SwerveModule.rlConfig);

        headingController.enableContinuousInput(-Math.PI, Math.PI);
        headingController.reset(0, 0);
        headingController.setTolerance(Constants.SwerveDrive.ALLOWABLE_HEADING_ERROR);
    }

    /**
     * @return the swerve in field oriented mode.
     */
    public static SwerveDrive getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SwerveDrive();
        }
        return INSTANCE;
    }

    /**
     * Gets the kinematics of the swerve.
     *
     * @return the kinematics of the swerve.
     */
    public SwerveDriveKinematics getKinematics() {
        return kinematics;
    }

    /**
     * Move the swerve in the specified direction, rotation and velocity.
     *
     * @param forward  the velocity on the X-axis. [m/s]
     * @param strafe   the velocity on the Y-axis. [m/s]
     * @param rotation the rotational velocity counter-clockwise positive. [rad/s]
     */
    public void defaultHolonomicDrive(double forward, double strafe, double rotation) {
        ChassisSpeeds speeds =
                ChassisSpeeds.fromFieldRelativeSpeeds(forward, strafe, rotation, Robot.getAngle());
        setStates(kinematics.toSwerveModuleStates(speeds));
    }

    /**
     * Move the swerve in the specified direction, rotation and velocity.
     *
     * @param forward  the velocity on the X-axis. [m/s]
     * @param strafe   the velocity on the Y-axis. [m/s]
     * @param rotation the rotational velocity counter-clockwise positive. [rad/s]
     */
    public void errorRelativeHolonomicDrive(double forward, double strafe, double rotation) {
        ChassisSpeeds speeds =
                ChassisSpeeds.fromFieldRelativeSpeeds(forward, strafe, rotation, Robot.getAngle());
        errorRelativeSetStates(kinematics.toSwerveModuleStates(speeds));
    }

    /**
     * Set the states of the modules, but the velocities are relative to the angle error of the modules.
     *
     * @param states the states of the modules.
     */
    private void errorRelativeSetStates(SwerveModuleState[] states) {
        for (SwerveModule module : modules) {
            states[module.getWheel()] = SwerveModuleState.optimize(states[module.getWheel()], module.getAngle());
            double diff = states[module.getWheel()].angle.minus(module.getAngle()).getRadians();
            module.setAngle(states[module.getWheel()].angle);
            module.setVelocity(states[module.getWheel()].speedMetersPerSecond * Math.cos(diff), true);
        }
    }

    /**
     * Check whether all modules have reached their desired angles.
     *
     * @param forward  the velocity on the X-axis. [m/s]
     * @param strafe   the velocity on the Y-axis. [m/s]
     * @param rotation the rotational velocity counter-clockwise positive. [rad/s]
     */
    public boolean haveModulesReachedAngles(double forward, double strafe, double rotation) {
        ChassisSpeeds chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                forward, strafe, rotation, Robot.getAngle());
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassisSpeeds);
        for (SwerveModule module : modules) {
            states[module.getWheel()] = SwerveModuleState.optimize(states[module.getWheel()], module.getAngle());
            if (!(Math.abs(states[module.getWheel()].angle.minus(module.getAngle())
                    .getDegrees()) < 7)) { // TODO: Remove the magic number
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the states of every module.
     *
     * @return the states of every module.
     */
    public SwerveModuleState[] getStates() {
        SwerveModuleState[] swerveModuleState = new SwerveModuleState[modules.length];
        for (SwerveModule module : modules) {
            swerveModuleState[module.getWheel()] = module.getState();
        }
        return swerveModuleState;
    }

    /**
     * Sets the state of the modules.
     *
     * @param states the states of the modules.
     */
    public void setStates(SwerveModuleState[] states) {
        for (SwerveModule module : modules) {
            states[module.getWheel()] = SwerveModuleState.optimize(states[module.getWheel()], module.getAngle());
            module.setState(states[module.getWheel()]);
        }
    }

    /**
     * Gets the chassis speeds of the entire robot.
     *
     * @return the speed of the robot in each axis.
     */
    public ChassisSpeeds getChassisSpeeds() {
        ChassisSpeeds chassisSpeeds = kinematics.toChassisSpeeds(getStates());
        return ChassisSpeeds.fromFieldRelativeSpeeds(
                chassisSpeeds.vxMetersPerSecond,
                chassisSpeeds.vyMetersPerSecond,
                chassisSpeeds.omegaRadiansPerSecond,
                Robot.getAngle()
        );
    }

    /**
     * Gets a specific module, shouldn't be used for regular cases.
     *
     * @param index the index of the module.
     * @return the corresponding module.
     */
    public SwerveModule getModule(int index) {
        return modules[index];
    }

    /**
     * Gets the pose of the robot.
     *
     * @return the pose of the robot.
     */
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    /**
     * Resets the odometry.
     */
    public void resetOdometry() {
        resetOdometry(new Pose2d(), new Rotation2d());
    }

    /**
     * Resets the odometry to a specified position.
     *
     * @param pose the current pose.
     */
    public void resetOdometry(Pose2d pose, Rotation2d angle) {
        odometry.resetPosition(new Pose2d(pose.getTranslation(), angle), angle);
    }

    /**
     * Resets the heading controller target angle.
     */
    public void resetHeadingController() {
        headingController.reset(0, getChassisSpeeds().omegaRadiansPerSecond);
    }

    /**
     * Set the power of the angle motors for each module to a specified percent power for testing purposes.
     *
     * @param power percent power to give to the angel motors. [%]
     */
    public void setPower(double power) {
        for (var module : modules) {
            module.setPower(power);
        }
    }

    /**
     * Sets the state of the modules without optimizing them.
     * USE ONLY FOR TESTING & TUNING!
     *
     * @param states the states of the modules.
     */
    public void noOptimizeSetStates(SwerveModuleState[] states) {
        for (SwerveModule module : modules) {
            module.setState(states[module.getWheel()]);
        }
    }

    /**
     * Terminates the modules from moving.
     */
    public void terminate() {
        for (SwerveModule module : modules) {
            module.stopDriveMotor();
            module.stopAngleMotor();
        }
    }

    public void lock() {
        modules[0].setAngle(Rotation2d.fromDegrees(45));
        modules[1].setAngle(Rotation2d.fromDegrees(-45));
        modules[3].setAngle(Rotation2d.fromDegrees(45));
        modules[2].setAngle(Rotation2d.fromDegrees(-45));
    }

    @Override
    public void periodic() {
        odometry.updateWithTime(
                Timer.getFPGATimestamp(),
                Robot.getAngle(),
                getStates()
        );
    }

    @Override
    public void updateInputs() {
        ChassisSpeeds speeds = getChassisSpeeds();
        inputs.velocityX = speeds.vxMetersPerSecond;
        inputs.velocityY = speeds.vyMetersPerSecond;
        inputs.velocityOmega = speeds.omegaRadiansPerSecond;

        Pose2d pose = getPose();
        inputs.positionX = pose.getX();
        inputs.positionY = pose.getY();
        inputs.positionOmega = pose.getRotation().getRadians();
        inputs.pose = new double[]{inputs.positionX, inputs.positionY, inputs.positionOmega};
    }

    @Override
    public String getSubsystemName() {
        return "SwerveDrive";
    }
}
