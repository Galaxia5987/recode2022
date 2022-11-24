package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.utils.Utils;
import frc.robot.valuetuner.WebConstant;

import java.util.function.BooleanSupplier;

public class DriveAndAdjust extends HolonomicDrive {
    private final Limelight vision = Limelight.getInstance();

    private final BooleanSupplier adjustToTarget;

    private final PIDController adjustController = new PIDController(
            Constants.SwerveDrive.THETA_Kp, 0, 0);
    private boolean odometryReset = false;
    private final WebConstant adjustKp = WebConstant.of("Drivetrain", "Adjust kP", Constants.SwerveDrive.THETA_Kp);
    private final WebConstant adjustKf = WebConstant.of("Drivetrain", "Adjust kF", Constants.SwerveDrive.THETA_Kf);


    public DriveAndAdjust(XboxController xboxController, BooleanSupplier adjustToTarget) {
        super(xboxController);
        this.adjustToTarget = adjustToTarget;
    }

    @Override
    public void initialize() {
        adjustController.setTolerance(0);
    }

    @Override
    public void execute() {
        if (adjustToTarget.getAsBoolean()) {
            double forward = Utils.deadband(-xboxController.getLeftY(), Constants.UIControl.DEFAULT_DEADBAND);
            double strafe = Utils.deadband(-xboxController.getLeftX(), Constants.UIControl.DEFAULT_DEADBAND);
            double rotation = adjustController.calculate(Math.toRadians(IntegratedUtils.angleToTarget()), 0);
            double sign = Math.signum(rotation);
            rotation = sign * (Math.min(Math.abs(rotation), 3));
            if (rotation != 0) {
                rotation += sign * adjustKf.get();
            }
            swerveDrive.holonomicDrive(
                    xFilter.calculate(forward),
                    yFilter.calculate(strafe),
                    rotation
            );

            var estimatedPose = vision.estimatePose(Robot.getAngle());
            if (Utils.deadband(IntegratedUtils.angleToTarget(), 5) == 0 &&
                    estimatedPose.isPresent() && !odometryReset) {
                swerveDrive.resetOdometry(estimatedPose.get(), Robot.getAngle());
                odometryReset = true;
            }
        } else {
            super.execute();
            odometryReset = false;
        }
        adjustController.setP(adjustKp.get());
    }
}
