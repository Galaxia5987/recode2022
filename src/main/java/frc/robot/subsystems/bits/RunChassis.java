package frc.robot.subsystems.bits;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class RunChassis extends CommandBase {
    private final SwerveDrive swerve = SwerveDrive.getFieldOrientedInstance();
    private final Timer timer = new Timer();
    private final double runTime;

    public RunChassis(double runTime) {
        this.runTime = runTime;
    }

    @Override
    public void execute() {
        swerve.defaultHolonomicDrive(1, 0, 1);
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= runTime;
    }
}
