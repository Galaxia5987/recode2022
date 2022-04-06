package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.hood.Hood;

public class HoodDefaultCommand extends CommandBase {
    private final Hood hood;

    public HoodDefaultCommand(Hood hood) {
        this.hood = hood;
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().getRightTrigger()) {
            hood.setMode(Superstructure.getInstance().getDistanceFromTarget() >= Constants.Hood.DISTANCE_FROM_TARGET_THRESHOLD);
        }
    }
}
