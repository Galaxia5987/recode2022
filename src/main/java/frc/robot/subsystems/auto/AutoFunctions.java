package frc.robot.subsystems.auto;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Autonomous;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.conveyor.commands.ConveyToShooter;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.AdjustOnCommand;
import frc.robot.subsystems.drivetrain.commands.auto.FollowPath;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class AutoFunctions extends Autonomous.AutoCommand {
    protected final SwerveDrive swerveDrive;
    protected final Shooter shooter;
    protected final Conveyor conveyor;
    protected final Intake intake;
    protected final Hood hood;
    protected final Limelight visionModule;

    public AutoFunctions(String name, SwerveDrive swerveDrive, Shooter shooter, Conveyor conveyor, Intake intake, Hood hood, Limelight visionModule, String initialPathPath) {
        super(name);
        this.swerveDrive = swerveDrive;
        this.shooter = shooter;
        this.conveyor = conveyor;
        this.intake = intake;
        this.hood = hood;
        this.visionModule = visionModule;

        var initialState = PathPlanner.loadPath(initialPathPath, Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL).getInitialState();
        addCommands(new InstantCommand(() -> {
            swerveDrive.resetOdometry(initialState.poseMeters, initialState.holonomicRotation);
            Robot.resetAngle(initialState.holonomicRotation);
        }));
    }

    protected FollowPath followPath(String path) {
        return new FollowPath(
                PathPlanner.loadPath(path, Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL),
                swerveDrive::getPose,
                swerveDrive.getKinematics(),
                new PIDController(Constants.Autonomous.KP_X_CONTROLLER, 0, 0),
                new PIDController(Constants.Autonomous.KP_Y_CONTROLLER, 0, 0),
                swerveDrive::setStates,
                swerveDrive);
    }

    protected CommandBase followPathAndPickUp(String path) {
        return new ParallelRaceGroup(
                followPath(path),
                pickup(10),
                new RunCommand(() -> shooter.setVelocity(3400), shooter)
        );
    }

    protected CommandBase pickup(double timeout) {
        return new FeedAndConvey().withTimeout(timeout);
    }

    protected CommandBase adjustAndShoot(double timeout) {
        Supplier<Pose2d> swervePose = swerveDrive::getPose;
        Supplier<Transform2d> poseRelativeToTarget = () -> Constants.Vision.HUB_POSE.minus(swervePose.get());
        DoubleSupplier yaw = () -> visionModule.getYaw().orElse(Robot.getAngle().minus(new Rotation2d(
                        Math.atan2(
                                poseRelativeToTarget.get().getY(),
                                poseRelativeToTarget.get().getX()
                        )
                )
        ).getDegrees());
        return new SequentialCommandGroup(
                new AdjustOnCommand(swerveDrive, yaw, visionModule::hasTargets).withTimeout(0.3),
                new ParallelRaceGroup(
                        new ShootCargo().withTimeout(timeout),
                        new ConveyToShooter(Constants.Conveyor.DEFAULT_POWER)
                        // new AdjustOnCommand(swerveDrive, yaw, visionModule::hasTargets)
                ));
    }
}
