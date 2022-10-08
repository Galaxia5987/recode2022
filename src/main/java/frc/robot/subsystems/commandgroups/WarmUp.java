package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;

public class WarmUp extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Hood hood = Hood.getInstance();

    public WarmUp() {
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        double distance = IntegratedUtils.distanceToTarget();
        double velocity = Constants.interpolateMap(Constants.Shooter.SHOOT_MEASUREMENTS, distance);
        double angle = Constants.interpolateMap(Constants.Hood.HOOD_MEASUREMENTS, distance);

        shooter.setVelocity(velocity - 100);
        hood.setAngle(angle);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        hood.stop();
    }
}
