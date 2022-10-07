package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class AdjustOnCommand extends CommandBase {
    private final SwerveDrive swerveDrive;
    private final DoubleSupplier yawSupplier;
    private final PIDController adjustController = new PIDController(Constants.SwerveDrive.ADJUST_CONTROLLER_KP.get(), 0, 0) {{
        enableContinuousInput(-Math.PI, Math.PI);
        setTolerance(Constants.SwerveDrive.ADJUST_CONTROLLER_TOLERANCE);
    }};

    public AdjustOnCommand(SwerveDrive swerveDrive, DoubleSupplier yawSupplier) {
        this.swerveDrive = swerveDrive;
        this.yawSupplier = yawSupplier;
        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double rotation = adjustController.calculate(yawSupplier.getAsDouble(), 0);
        swerveDrive.holonomicDrive(0, 0, rotation);
    }


    @Override
    public void end(boolean interrupted) {
        swerveDrive.terminate();
    }

    @Override
    public boolean isFinished() {
        return Utils.deadband(yawSupplier.getAsDouble(), Constants.SwerveDrive.ALLOWABLE_HEADING_ERROR) == 0;
    }
}

