package frc.robot.subsystems.auto;

import frc.robot.Constants;
import frc.robot.subsystems.commandgroups.ShootCargo;

public class BottomFour extends AutoFunctions {
    public BottomFour() {
        super("BottomFour", "Bottom4.2");
        {
            addCommands(new ShootCargo().withTimeout(3));
            addCommands(followPathAndPickUp("Bottom4.2", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
            addCommands(new ShootCargo().withTimeout(3));
        }
    }
}