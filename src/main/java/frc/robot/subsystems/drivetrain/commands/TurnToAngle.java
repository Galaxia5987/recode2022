package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.SwerveDrive;

import java.util.function.Supplier;

public class TurnToAngle extends CommandBase {
    private final SwerveDrive swerveDrive;
    private final Supplier<Rotation2d> targetAngleSupplier;
    private Rotation2d targetAngle;
    private final PIDController adjustController = new PIDController(Constants.SwerveDrive.ADJUST_CONTROLLER_KP.get(), Constants.SwerveDrive.ADJUST_CONTROLLER_KI.get(), Constants.SwerveDrive.ADJUST_CONTROLLER_KD.get()) {{
        enableContinuousInput(-Math.PI, Math.PI);
        setTolerance(Constants.SwerveDrive.ADJUST_CONTROLLER_TOLERANCE);
    }};


    /**
     * Initialize rotate to angle command.
     *
     * @param swerveDrive the SwerveDrive subsystem
     * @param targetAngle the target angle. [rad]
     */
    public TurnToAngle(SwerveDrive swerveDrive, Supplier<Rotation2d> targetAngle) {
        this.swerveDrive = swerveDrive;
        this.targetAngleSupplier = targetAngle;
        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {
        targetAngle = targetAngleSupplier.get();
    }

    @Override
    public void execute() {
        swerveDrive.holonomicDrive(0, 0, adjustController.calculate(Robot.getAngle().getRadians(), targetAngle.getRadians()));
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Robot.getAngle().minus(targetAngle).getDegrees()) <= 5;
    }

    @Override
    public void end(boolean interrupted) {
        swerveDrive.terminate();
    }
}

