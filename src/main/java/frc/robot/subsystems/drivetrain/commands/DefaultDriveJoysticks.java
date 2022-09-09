package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class DefaultDriveJoysticks extends CommandBase {
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    private final Joystick rightJoystick;
    private final Joystick leftJoystick;

    private final LinearFilter xFilter = LinearFilter.movingAverage(15);
    private final LinearFilter yFilter = LinearFilter.movingAverage(15);
    private final LinearFilter omegaFilter = LinearFilter.movingAverage(10);


    public DefaultDriveJoysticks(Joystick rightJoystick, Joystick leftJoystick) {
        this.rightJoystick = rightJoystick;
        this.leftJoystick = leftJoystick;
        addRequirements(swerveDrive);
    }

    @Override
    public void execute() {
        double forward = Utils.deadband(-rightJoystick.getY(), 0.05);
        double strafe = Utils.deadband(-rightJoystick.getX(), 0.05);
        double rotation = Utils.deadband(-leftJoystick.getX(), 0.05);

        double multiplier = leftJoystick.getThrottle() * 0.4 + 0.6;

        swerveDrive.holonomicDrive(
                multiplier * xFilter.calculate(forward),
                multiplier * yFilter.calculate(strafe),
                multiplier * omegaFilter.calculate(rotation)
        );
    }
}
