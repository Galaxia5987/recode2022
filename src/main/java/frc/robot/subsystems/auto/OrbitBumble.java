package frc.robot.subsystems.auto;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.commandgroups.ShootCargo;

public class OrbitBumble extends AutoFunctions {

    public OrbitBumble() {
        super("Joe", "OrbitBumblePath");
        addCommands(new WaitCommand(5));
        addCommands(followPath("OrbitBumblePath", Constants.Autonomous.MAX_VELOCITY, Constants.Autonomous.MAX_ACCEL));
        addCommands(new ShootCargo().withTimeout(10));
    }
}
