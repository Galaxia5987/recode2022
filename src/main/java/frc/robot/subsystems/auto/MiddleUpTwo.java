package frc.robot.subsystems.auto;

import frc.robot.Constants;

public class MiddleUpTwo extends AutoFunctions {
    public MiddleUpTwo() {
        super("MiddleUpTwo", "MiddleUp2.1");

        addCommands(adjustAndShoot(5));
        addCommands(followPath("MiddleUp2.1", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
    }
}


