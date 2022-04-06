package frc.robot.subsystems.shooter;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Superstructure;

public class ShootAndDriveUtil {

    public static Translation2d getVirtualTarget() {
        return Constants.Vision.HUB_POSE.plus(new Transform2d(getVirtualOffset(), Rotation2d.fromDegrees(0))).getTranslation();
    }

    public static Translation2d getVirtualOffset() {
        double timeOfFlight = Constants.Shooter.distanceToTimeOfFlight(Superstructure.getInstance().getDistanceFromTarget());
        ChassisSpeeds speeds = Superstructure.getInstance().getRobotSpeeds();

        return new Translation2d(speeds.vyMetersPerSecond * timeOfFlight, speeds.vxMetersPerSecond * timeOfFlight);
    }

    public static double getVirtualYaw() {
        Translation2d targetPosition = getVirtualTarget();
        return Rotation2d.fromDegrees(Math.atan2(targetPosition.getY(), targetPosition.getX())).minus(Robot.getAngle()).getDegrees();
    }
}
