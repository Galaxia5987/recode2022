package frc.robot.subsystems.auto;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Autonomous;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.commandgroups.WarmUp;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.AdjustOnCommand;
import frc.robot.subsystems.drivetrain.commands.auto.FollowPath;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;

public class AutoFunctions extends Autonomous.AutoCommand {
    protected final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    protected final Shooter shooter = Shooter.getInstance();
    protected final Conveyor conveyor = Conveyor.getInstance();
    protected final Intake intake = Intake.getInstance();
    protected final Hood hood = Hood.getInstance();
    protected final Limelight vision = Limelight.getInstance();

    public AutoFunctions(String name, String initialPathPath) {
        super(name);

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
                swerveDrive
        );
    }

    protected CommandBase followPathAndPickUp(String path) {
        return new ParallelRaceGroup(
                followPath(path),
                pickup(10),
                new WarmUp()
        );
    }

    protected CommandBase pickup(double timeout) {
        return new FeedAndConvey().withTimeout(timeout);
    }

    protected CommandBase adjustAndShoot(double timeout) {

        return new SequentialCommandGroup(
                new AdjustOnCommand(swerveDrive, IntegratedUtils::angleToTarget)
                        .alongWith(new WarmUp()),
                new ShootCargo().withTimeout(timeout)
        );
    }
}
