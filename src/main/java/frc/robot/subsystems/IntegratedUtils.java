package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.helicopter.Helicopter;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;

/**
 * This class constitutes all the information provided by multiple subsystem.
 * <p>
 * Here one can implement useful functions without implementing suppliers
 * in the code, and ensure the same information for all commands.
 */
public class IntegratedUtils {
    private static final Shooter shooter = Shooter.getInstance();
    private static final Limelight limelight = Limelight.getInstance();
    private static final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Conveyor conveyor = Conveyor.getInstance();
    private static final Hood hood = Hood.getInstance();
    private static final Helicopter helicopter = Helicopter.getInstance();

    /**
     * This function gets the distance to the target in two ways:
     * vision - if the vision has targets
     * odometry - if the vision doesn't have targets
     *
     * @return the distance to the target. [m]
     */
    public static double distanceToTarget() {
        return limelight.getDistance()
                .orElse(swerveDrive.getPose()
                        .minus(Constants.Vision.HUB_POSE)
                        .getTranslation().getNorm());
    }

    public static double angleToTarget() {
        return limelight.getYaw().orElseGet(() -> {
            var toTarget = SwerveDrive.getFieldOrientedInstance().getPose().minus(Constants.Vision.HUB_POSE);
            return Math.IEEEremainder(Robot.getAngle().getDegrees() + Math.toDegrees(Math.atan2(toTarget.getY(), toTarget.getX())), 360.0);
        });
    }
}
