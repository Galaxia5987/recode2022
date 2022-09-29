package frc.robot.subsystems.shooter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;

public class WarmUp extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();

    public WarmUp() {
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        double distance = IntegratedUtils.distanceToTarget();
        Constants.ShootData shootData = Constants.interpolateMeasurements(distance);
        shooter.setVelocity(Math.min(shootData.shooterVelocity, Constants.Shooter.MAX_WARMUP_VELOCITY));
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
