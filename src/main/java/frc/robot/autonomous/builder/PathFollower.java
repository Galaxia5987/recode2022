package frc.robot.autonomous.builder;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.ShootAndDriveUtil;
import frc.robot.utils.Utils;

import java.util.HashSet;
import java.util.Set;

public class PathFollower implements BuilderUtil {
    private static PathFollower INSTANCE = null;
    private final SwerveDrive swerve = SwerveDrive.getInstance();

    private PathFollower() {
    }

    public static PathFollower getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PathFollower();
        }
        return INSTANCE;
    }

    public Command defaultFollowPath(String pathName, double maxVelocity, double maxAcceleration, boolean isAShootingPath, boolean isVirtualShot) {
        final PathPlannerTrajectory trajectory = PathPlanner.loadPath(pathName, maxVelocity, maxAcceleration);

        return new Command() {
            Timer timer;
            PIDController xVelocityController;
            PIDController yVelocityController;
            HolonomicDriveController holonomicController;
            PIDController thetaController;

            @Override
            public void initialize() {
                timer = new Timer();
                xVelocityController = new PIDController(
                        Constants.Autonomous.VELOCITY_Kp,
                        Constants.Autonomous.VELOCITY_Ki,
                        Constants.Autonomous.VELOCITY_Kd
                );
                yVelocityController = new PIDController(
                        Constants.Autonomous.VELOCITY_Kp,
                        Constants.Autonomous.VELOCITY_Ki,
                        Constants.Autonomous.VELOCITY_Kd
                );
                holonomicController = new HolonomicDriveController(
                        xVelocityController,
                        yVelocityController,
                        new ProfiledPIDController(0, 0, 0, new TrapezoidProfile.Constraints(0, 0))
                );
                thetaController = new PIDController(Constants.Autonomous.THETA_Kp, 0, 0) {{
                    enableContinuousInput(-Math.PI, Math.PI);
                }};
                timer.start();
                timer.reset();
                swerve.resetOdometry(trajectory.getInitialPose(), trajectory.getInitialPose().getRotation());
            }

            @Override
            public void execute() {
                var desiredState = trajectory.sample(timer.get());
                var currentPose = swerve.getPose();
                var desiredPose = desiredState.poseMeters;
                var desiredSpeeds = holonomicController.calculate(currentPose, desiredPose, desiredState.velocityMetersPerSecond, Rotation2d.fromDegrees(0));
                double desiredAngle;
                if (isAShootingPath) {
                    if (isVirtualShot) {
                        desiredAngle = Robot.getAngle().getDegrees() + ShootAndDriveUtil.getVirtualYaw();
                    } else {
                        desiredAngle = Robot.getAngle().getDegrees() + Superstructure.getInstance().getYawFromTarget();
                    }
                } else {
                    desiredAngle = desiredState.poseMeters.getRotation().getDegrees();
                }
                double rotation = thetaController.calculate(Robot.getAngle().getDegrees(), desiredAngle);
                swerve.defaultHolonomicDrive(desiredSpeeds.vxMetersPerSecond, desiredSpeeds.vyMetersPerSecond, rotation);
            }

            @Override
            public boolean isFinished() {
                return Utils.poseEquals(trajectory.getState(trajectory.getStates().size() - 1).poseMeters, swerve.getPose(), 0.1);
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    public Command adjustToTarget() {
        return new Command() {
            final PIDController adjustController = new PIDController(5, 0, 0);

            @Override
            public void execute() {
                double currentAngle = Robot.getAngle().getDegrees();
                double desiredAngle = Robot.getAngle().getDegrees() + Superstructure.getInstance().getYawFromTarget();
                swerve.defaultHolonomicDrive(0, 0, adjustController.calculate(currentAngle, desiredAngle));
            }

            @Override
            public boolean isFinished() {
                return Superstructure.getInstance().robotAtAllowableYawError();
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    @Override
    public Set<Subsystem> defaultRequirements() {
        return new HashSet<>() {{
            add(swerve);
        }};
    }
}
