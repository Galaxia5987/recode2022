package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.Utils;

public class ZeroModules extends CommandBase {
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();

    public ZeroModules() {
    }

    @Override
    public void execute() {
        for (int i = 0; i < 4; i++) {
            swerveDrive.getModule(i).setAngle(Rotation2d.fromDegrees(0));
        }
    }

    @Override
    public boolean isFinished() {
        boolean res = true;
        for (int i = 0; i < 4; i++) {
            res &= Utils.deadband(swerveDrive
                    .getModule(i)
                    .getAngle()
                    .getDegrees(), 2) == 0;
        }
        return res;
    }
}
