package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDrive extends CommandBase {
    protected final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    protected final XboxController xboxController;

    protected final SlewRateLimiter xFilter = new SlewRateLimiter(Constants.UIControl.SLEW_RATE_LIMIT);
    protected final SlewRateLimiter yFilter = new SlewRateLimiter(Constants.UIControl.SLEW_RATE_LIMIT);
    protected final SlewRateLimiter omegaFilter = new SlewRateLimiter(Constants.UIControl.SLEW_RATE_LIMIT);

    public HolonomicDrive(XboxController xboxController) {
        this.xboxController = xboxController;
        addRequirements(swerveDrive);
    }

    @Override
    public void execute() {
        double forward = Utils.deadband(-xboxController.getLeftY(), 0.1);
        double strafe = Utils.deadband(-xboxController.getLeftX(), 0.1);
        double rotation = Utils.deadband(-xboxController.getRightX(), 0.1);

        swerveDrive.holonomicDrive(
                xFilter.calculate(forward),
                yFilter.calculate(strafe),
                omegaFilter.calculate(rotation)
        );
    }
}
