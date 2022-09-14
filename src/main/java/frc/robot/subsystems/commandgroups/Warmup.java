package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;

public class Warmup extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Hood hood = Hood.getInstance();

    public Warmup() {
        addRequirements(shooter, hood);
    }

    @Override
    public void execute() {
        double distance = IntegratedUtils.distanceToTarget();
        Constants.ShootData shootData = Constants.interpolateMeasurements(distance);

        shooter.setVelocity(Math.min(shootData.shooterVelocity, Constants.Shooter.MAX_WARMUP_VELOCITY));
        hood.setAngle(shootData.hoodAngle);
    }
}