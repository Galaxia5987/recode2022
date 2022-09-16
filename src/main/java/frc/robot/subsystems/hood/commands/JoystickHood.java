package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

public class JoystickHood extends CommandBase {
    private final Hood hood = Hood.getInstance();

    private final XboxController xboxController;

    public JoystickHood(XboxController xboxController) {
        this.xboxController = xboxController;
        addRequirements(hood);
    }

    @Override
    public void execute() {
        hood.setPower(0.3 * xboxController.getLeftY());
    }
}
