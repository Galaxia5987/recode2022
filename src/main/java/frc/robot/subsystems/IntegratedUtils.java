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

public class IntegratedUtils {
    private static final Shooter shooter = Shooter.getInstance();
    private static final Limelight limelight = Limelight.getInstance();
    private static final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Conveyor conveyor = Conveyor.getInstance();
    private static final Hood hood = Hood.getInstance();
    private static final Helicopter helicopter = Helicopter.getInstance();

    public static double distanceToTarget() {
        return limelight.estimatePose(Robot.getAngle())
                .orElse(swerveDrive.getPose())
                .minus(Constants.Vision.HUB_POSE)
                .getTranslation().getNorm();
    }
}
