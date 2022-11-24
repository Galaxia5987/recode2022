package frc.robot.subsystems.auto;

import frc.robot.Constants;

public class MiddleDownThree extends AutoFunctions {
    public MiddleDownThree() {
        super("MiddleDownThree", "MiddleDown3.2");
        {
            //addCommands(followPath("MiddleDown3.1"));
            addCommands(adjustAndShoot(3));
            addCommands(followPathAndPickUp("MiddleDown3.2", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
            addCommands(adjustAndShoot(3));
        }
    }
}
