package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;

public class OscillateHood extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Timer timer = new Timer();
    private final double runTime;
    private double startTime;

    public OscillateHood(double runTime) {
        this.runTime = runTime;

        addRequirements(hood);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (timer.hasElapsed(0.2)) {
            hood.toggle();
            timer.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime >= runTime;
    }
}
