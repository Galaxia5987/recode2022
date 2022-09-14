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
        if (timer.get() > 5) {
            swerveDrive.setPower(1);
        } else {
            for (int i = 0; i < 4; i++) {
                swerveDrive.getModule(i).setAngle(Rotation2d.fromDegrees(720 * timer.get()));
            }
        }
    }

    @Override
    public boolean isFinished() {
        return timer.get() > 15;
    }
}
