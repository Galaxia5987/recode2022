package frc.robot.subsystems.helicopter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Infrastructure;
import frc.robot.subsystems.helicopter.Helicopter;
import frc.robot.utils.Utils;

public class SimpleJoystickClimb extends CommandBase {
    private final Helicopter helicopter;

    public SimpleJoystickClimb(Helicopter helicopter) {
        this.helicopter = helicopter;
    }

    @Override
    public void execute() {
        double output = Infrastructure.getInstance().climberGetLeftY() * Utils.boolToInt(Constants.ClimberUIControl.IS_LEFT_Y_INVERTED);
        output = Utils.deadband(output, Constants.Helicopter.NEUTRAL_DEADBAND);
        helicopter.setPower(output);
    }
}
