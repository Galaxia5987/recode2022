package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class HolonomicDriveJoysticks extends CommandBase {
    protected final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    protected final Joystick rightJoystick;
    protected final Joystick leftJoystick;

    protected final SlewRateLimiter xFilter = new SlewRateLimiter(Constants.UIControl.SLEW_RATE_LIMIT);
    protected final SlewRateLimiter yFilter = new SlewRateLimiter(Constants.UIControl.SLEW_RATE_LIMIT);
    protected final SlewRateLimiter omegaFilter = new SlewRateLimiter(Constants.UIControl.SLEW_RATE_LIMIT_ROTATION);

    public HolonomicDriveJoysticks(Joystick rightJoystick, Joystick leftJoystick) {
        this.rightJoystick = rightJoystick;
        this.leftJoystick = leftJoystick;
        addRequirements(swerveDrive);
    }

    @Override
    public void execute() {
        double forward = Utils.deadband(-leftJoystick.getY(), 0.05);
        double strafe = Utils.deadband(-leftJoystick.getX(), 0.05);
        double rotation = Utils.deadband(-rightJoystick.getX(), 0.05);

        swerveDrive.holonomicDrive(
                xFilter.calculate(forward),
                yFilter.calculate(strafe),
                omegaFilter.calculate(rotation)
        );
    }
}
