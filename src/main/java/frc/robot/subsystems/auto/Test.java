package frc.robot.subsystems.auto;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class Test extends AutoFunctions {
    public Test() {
        super("test", "New Path");
        addCommands(followPath("New Path", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
    }

}

