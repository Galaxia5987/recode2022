package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Infrastructure;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.hood.Hood;

public class HoodDefaultCommand extends CommandBase {
    private final Hood hood;

    public HoodDefaultCommand(Hood hood) {
        this.hood = hood;

        addRequirements(hood);
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().chassisGetRightTrigger()) {
            hood.setMode(Superstructure.getInstance().getDistanceFromTarget() >= Constants.Hood.DISTANCE_FROM_TARGET_THRESHOLD);
        }
    }
}
