package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.Intake;

public class OscillateIntake extends CommandBase {
    private final Intake intake = Intake.getInstance();
    private final Timer timer = new Timer();
    private final double runTime;
    private double startTime;

    public OscillateIntake(double runTime) {
        this.runTime = runTime;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (timer.hasElapsed(1)) {
            intake.toggleMechanism();
            timer.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime >= runTime;
    }
}
