package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

public class RunHood extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Timer timer = new Timer();

    public RunHood() {
    }

    @Override
    public void execute() {
        hood.setAngle(10 * Math.floor(timer.get()) + 10);
    }

    @Override
    public void end(boolean interrupted) {
        hood.setAngle(10);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3);
    }
}
