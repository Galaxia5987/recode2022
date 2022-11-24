package frc.robot.subsystems.drivetrain.commands.auto;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.valuetuner.WebConstant;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FollowPath extends CommandBase {
    private final Timer m_timer = new Timer();
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();

    private final PathPlannerTrajectory trajectory;
    private final PIDController xController;
    private final PIDController yController;
    private HolonomicDriveController holonomicDriveController;
    private final PIDController thetaController = new PIDController(Constants.Autonomous.THETA_Kp, 0, 0) {{
        enableContinuousInput(-Math.PI, Math.PI);
    }};

    protected final WebConstant webKp_x = WebConstant.of("Autonomous", "kP_x", Constants.Autonomous.KP_X_CONTROLLER);
    protected final WebConstant webKi_x = WebConstant.of("Autonomous", "kI_x", Constants.Autonomous.KI_X_CONTROLLER);
    protected final WebConstant webKd_x = WebConstant.of("Autonomous", "kD_x", Constants.Autonomous.KD_X_CONTROLLER);
    protected final WebConstant webKf_x = WebConstant.of("Autonomous", "kF_x", Constants.Autonomous.KF_X_CONTROLLER);
    protected final WebConstant webKp_y = WebConstant.of("Autonomous", "kP_y", Constants.Autonomous.KP_Y_CONTROLLER);
    protected final WebConstant webKi_y = WebConstant.of("Autonomous", "kI_y", Constants.Autonomous.KI_Y_CONTROLLER);
    protected final WebConstant webKd_y = WebConstant.of("Autonomous", "kD_y", Constants.Autonomous.KD_Y_CONTROLLER);
    protected final WebConstant webKf_y = WebConstant.of("Autonomous", "kF_y", Constants.Autonomous.KF_Y_CONTROLLER);

    @SuppressWarnings("ParameterName")
    public FollowPath(PathPlannerTrajectory trajectory) {
        this.trajectory = trajectory;
        xController = new PIDController(webKp_x.get(), webKi_x.get(), webKd_x.get());
        yController = new PIDController(webKp_y.get(), webKi_y.get(), webKd_y.get());

        holonomicDriveController = new HolonomicDriveController(
                xController,
                yController,
                new ProfiledPIDController(0, 0, 0, new TrapezoidProfile.Constraints(0., 0))
        );

        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {
        m_timer.reset();
        m_timer.start();
        xController.setP(webKp_x.get());
        xController.setI(webKi_x.get());
        xController.setD(webKd_x.get());
        yController.setP(webKp_y.get());
        yController.setI(webKi_y.get());
        yController.setD(webKd_y.get());
        holonomicDriveController = new HolonomicDriveController(
                xController, yController,
                new ProfiledPIDController(0, 0, 0, new TrapezoidProfile.Constraints(0, 0)
        ));
    }

    @Override
    @SuppressWarnings("LocalVariableName")
    public void execute() {
        double curTime = m_timer.get();

        var desiredState = (PathPlannerTrajectory.PathPlannerState) trajectory.sample(curTime);

        var desiredSpeeds = holonomicDriveController.calculate(
                swerveDrive.getPose(),
                desiredState,
                desiredState.holonomicRotation
        );

         double rotation = thetaController.calculate(
                Robot.getAngle().getRadians(), desiredState.holonomicRotation.getRadians());

        double omega = rotation * Math.hypot(desiredSpeeds.vxMetersPerSecond, desiredSpeeds.vyMetersPerSecond);
        var states = swerveDrive.getKinematics().toSwerveModuleStates(new ChassisSpeeds(desiredSpeeds.vxMetersPerSecond, desiredSpeeds.vyMetersPerSecond, omega + rotation));
        swerveDrive.setStates(states, true);

    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();
        swerveDrive.terminate();
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasElapsed(trajectory.getTotalTimeSeconds());
    }
}
