package frc.robot.subsystems.helicopter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.helicopter.Helicopter;
import frc.robot.utils.Utils;

public class AngleHelicopter extends CommandBase {
    private final Helicopter helicopter = Helicopter.getInstance();

    private final double angle;

    public AngleHelicopter(double angle) {
        this.angle = angle;
    }

    @Override
    public void execute() {
        helicopter.setAngle(angle);
    }

    @Override
    public boolean isFinished() {
        return Utils.deadband(helicopter.getAngle() - angle,
                Constants.Helicopter.ALLOWABLE_ERROR) == 0;
    }
}
