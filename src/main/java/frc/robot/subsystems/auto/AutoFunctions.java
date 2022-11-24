package frc.robot.subsystems.auto;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Autonomous;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.LowerBalls;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.commandgroups.WarmUp;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.conveyor.commands.ConveyToShooter;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.AdjustOnCommand;
import frc.robot.subsystems.drivetrain.commands.TurnToAngle;
import frc.robot.subsystems.drivetrain.commands.auto.FollowPath;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.valuetuner.WebConstant;

import java.util.function.DoubleSupplier;

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

    protected FollowPath followPath(String path, double maxVel, double maxAccel) {
        return new FollowPath(
                PathPlanner.loadPath(path, maxVel, maxAccel)
        );
    }

    protected CommandBase followPathAndPickUp(String path, double maxVel, double maxAccel) {
        return new ParallelRaceGroup(
                followPath(path, maxVel, maxAccel),
                pickup(15)
//                new WarmUp()
        );
    }

    protected CommandBase pickup(double timeout) {
        return new FeedAndConvey()
                .alongWith(new ConveyToShooter(0.2)
                        .withInterrupt(conveyor::preFlapBeamSeesObject)
                        .andThen(new ConveyToShooter(0.3).withTimeout(0.15)))
                .withTimeout(timeout);
    }

    protected CommandBase adjustAndShoot(double timeout) {
        return new SequentialCommandGroup(
                new AdjustOnCommand(swerveDrive, IntegratedUtils::angleToTarget).raceWith(new WarmUp()),
                new ShootCargo().withTimeout(timeout));

    }

    protected CommandBase turnToAngle(DoubleSupplier angle, double timeout) {
        return new TurnToAngle(swerveDrive, () -> Rotation2d.fromDegrees(angle.getAsDouble())).withTimeout(timeout);
    }
}
