package frc.robot.subsystems.helicopter;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.utils.PIDConstants;

public class ArmController {
    private ProfiledPIDController baseController;
    private final double feedForward;

    public ArmController(double kP, double kI, double kD, double feedForward, double maxVelocity, double maxAcceleration) {
        this.baseController = new ProfiledPIDController(kP, kI, kD, new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration));
        this.feedForward = feedForward;
    }

    public ArmController(PIDConstants pidConstants, double maxVelocity, double maxAcceleration) {
        this.baseController = new ProfiledPIDController(pidConstants.kP, pidConstants.kI, pidConstants.kD, new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration));
        this.feedForward = pidConstants.kF;
    }

    public double calculate(double currentAngle, double requiredAngle) {
        double downwardForce = -feedForward * Math.cos(requiredAngle - currentAngle);
        double baseCalculation = baseController.calculate(currentAngle, requiredAngle);
        return baseCalculation - downwardForce;
    }

    public void updateConstants(double kP, double kI, double kD, double maxVelocity, double maxAcceleration) {
        baseController = new ProfiledPIDController(kP, kI, kD, new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration));
    }
}
