package frc.robot.subsystems.auto;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.auto.FollowPath;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;

public class AutoFunctions extends SequentialCommandGroup {
        protected final SwerveDrive swerveDrive;
        protected final Shooter shooter;
        protected final Conveyor conveyor;
        protected final Intake intake;
        protected final Hood hood;
        protected final Limelight visionModule;

        public AutoFunctions(SwerveDrive swerveDrive, Shooter shooter, Conveyor conveyor, Intake intake, Hood hood, Limelight visionModule, String initialPathPath) {
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
}
