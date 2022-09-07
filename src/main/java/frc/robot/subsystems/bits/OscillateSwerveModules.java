package frc.robot.subsystems.bits;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class OscillateSwerveModules extends CommandBase {
    private final SwerveDrive swerve = SwerveDrive.getFieldOrientedInstance();
    private final Timer timer = new Timer();
    private final double runTime;
    private double startTime;

    public OscillateSwerveModules(double runTime) {
        this.runTime = runTime;
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (timer.hasElapsed(0.1)) {
            swerve.setStates(new SwerveModuleState[]{
                    new SwerveModuleState(0, Rotation2d.fromDegrees(Math.random() * 360)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(Math.random() * 360)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(Math.random() * 360)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(Math.random() * 360))
            });
            timer.reset();
        }
    }

    @Override
    public void end(boolean interrupted) {
        swerve.setStates(new SwerveModuleState[]{
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0))
        });
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime > runTime;
    }
}
