package frc.robot.autonomous.test;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.autonomous.builder.PathFollower;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class TestOdometry extends CommandBase {
    private final SwerveDrive swerve = SwerveDrive.getInstance();
    private final Command pathFollower;
    private final PathPlannerTrajectory trajectory = PathPlanner.loadPath("TestOdometry", 4, 5);
    private final Timer timer = new Timer();
    private double numberOfLoops = 0;
    private double errorSum = 0;

    public TestOdometry() {
        pathFollower = PathFollower.getInstance().defaultFollowPath(
                "TestOdometry", 4, 5, false, false);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        pathFollower.initialize();
    }

    @Override
    public void execute() {
        pathFollower.execute();
        Pose2d requiredPose = trajectory.sample(timer.get()).poseMeters;
        Pose2d currentPose = swerve.getPose();
        double error = requiredPose.minus(currentPose).getTranslation().getNorm();
        numberOfLoops++;
        errorSum += error;
    }

    @Override
    public void end(boolean interrupted) {
        SmartDashboard.putNumber("Average error", errorSum / numberOfLoops);
    }

    @Override
    public boolean isFinished() {
        return pathFollower.isFinished();
    }
}
