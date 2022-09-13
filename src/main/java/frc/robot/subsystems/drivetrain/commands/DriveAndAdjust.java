package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.utils.Utils;

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
    public void execute() {
        if (adjustToTarget.getAsBoolean()) {
            double forward = Utils.deadband(-xboxController.getLeftY(), Constants.UIControl.DEFAULT_DEADBAND);
            double strafe = Utils.deadband(-xboxController.getLeftX(), Constants.UIControl.DEFAULT_DEADBAND);
            double rotation = adjustController.calculate(Robot.getAngle().getDegrees(), vision.angleToTarget());

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
