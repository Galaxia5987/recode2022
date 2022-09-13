package frc.robot.subsystems.helicopter.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.helicopter.Helicopter;

public class JoystickHelicopter extends CommandBase {
    private final Helicopter helicopter = Helicopter.getInstance();

    private final XboxController xboxController;

    public JoystickHelicopter(XboxController xboxController) {
        this.xboxController = xboxController;
    }

    @Override
    public void execute() {
        helicopter.setPower(-xboxController.getLeftY());
    }
}
