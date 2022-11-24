package frc.robot.subsystems.bits;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class RunChassis extends CommandBase {
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();

    private final Timer timer = new Timer();

    public RunChassis() {
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
    }

    @Override
    public void execute() {
        swerveDrive.holonomicDrive(Math.cos(timer.get() / 10), Math.sin(timer.get() / 10), 0);
    }

    @Override
    public boolean isFinished() {
        return timer.get() > 10;
    }
}
