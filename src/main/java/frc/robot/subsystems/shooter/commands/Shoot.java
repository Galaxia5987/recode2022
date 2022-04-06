package frc.robot.subsystems.shooter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.shooter.Shooter;

import java.util.HashMap;

public class Shoot extends CommandBase {
    protected final Shooter shooter;
    protected boolean yWasPressed;
    protected boolean yIsPressed;

    public Shoot(Shooter shooter) {
        this.shooter = shooter;
        this.yWasPressed = false;
        this.yIsPressed = false;
    }

    protected boolean isWarmupActive() {
        boolean toggled = yIsPressed && !yWasPressed;
        return toggled ^ yIsPressed;
    }

    protected double distanceToVelocity(double distance) {
        HashMap<Double, Double> measurements;
        if (distance < Constants.Hood.DISTANCE_FROM_TARGET_THRESHOLD) {
            measurements = Constants.Shooter.SHORT_MEASUREMENTS;
        } else {
            measurements = Constants.Shooter.LONG_MEASUREMENTS;
        }
        double prevMeasuredDistance = 0, nextMeasuredDistance = 0;
        double minPrevDifference = Double.POSITIVE_INFINITY, minNextDifference = Double.POSITIVE_INFINITY;

        for (var measuredDistance : measurements.keySet()) {
            double difference = measuredDistance - distance;
            if (difference < 0) {
                if (Math.abs(difference) < Math.abs(minPrevDifference)) {
                    minPrevDifference = difference;
                    prevMeasuredDistance = measuredDistance;
                }
            } else {
                if (Math.abs(difference) < Math.abs(minNextDifference)) {
                    minNextDifference = difference;
                    nextMeasuredDistance = measuredDistance;
                }
            }
        }
        double y1 = measurements.get(prevMeasuredDistance);
        double y2 = measurements.get(nextMeasuredDistance);
        double t = (distance - prevMeasuredDistance) / (nextMeasuredDistance - prevMeasuredDistance);
        return (1 - t) * y1 + t * y2;
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().getRightTrigger() && !isWarmupActive()) {
            double setpoint = distanceToVelocity(Superstructure.getInstance().getDistanceFromTarget());
            shooter.setVelocity(setpoint);
        } else if (isWarmupActive()) {
            shooter.setVelocity(Constants.Shooter.WARMUP_VELOCITY);
        } else {
            shooter.setPower(0);
        }

        yWasPressed = yIsPressed;
        yIsPressed = Infrastructure.getInstance().getY();
    }
}
