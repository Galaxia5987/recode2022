package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
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
            Constants.Autonomous.THETA_Kp, 0, 0);

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
            rotation = sign * Math.min(Math.abs(rotation), 0.8);

            swerveDrive.holonomicDrive(
                    xFilter.calculate(forward),
                    yFilter.calculate(strafe),
                    rotation
            );
        } else {
            super.execute();
        }
    }
}
