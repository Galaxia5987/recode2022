package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.flap.Flap;

public class OscillateFlap extends CommandBase {
    private final Flap flap = Flap.getInstance();
    private final Timer timer = new Timer();
    private final double runTime;
    private double startTime;

    public OscillateFlap(double runTime) {
        this.runTime = runTime;

        addRequirements(flap);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (timer.hasElapsed(0.4)) {
            flap.toggle();
            timer.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime >= runTime;
    }
}
