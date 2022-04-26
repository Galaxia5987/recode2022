package frc.robot.subsystems.shooter.commands;

import frc.robot.Constants;
import frc.robot.Infrastructure;
import frc.robot.subsystems.shooter.ShootAndDriveUtil;
import frc.robot.subsystems.shooter.Shooter;

public class ShootToVirtualTarget extends Shoot {

    public ShootToVirtualTarget(Shooter shooter) {
        super(shooter);
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().chassisGetRightTrigger() && !isWarmupActive()) {
            double setpoint = distanceToVelocity(ShootAndDriveUtil.getVirtualTarget().getNorm());
            shooter.setVelocity(setpoint);
        } else if (isWarmupActive()) {
            shooter.setVelocity(Constants.Shooter.WARMUP_VELOCITY);
        } else {
            shooter.setPower(0);
        }

        yWasPressed = yIsPressed;
        yIsPressed = Infrastructure.getInstance().chassisGetY();
    }
}
