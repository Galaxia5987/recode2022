package frc.robot.subsystems.bits;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;

public class RunPipeline extends CommandBase {
    private final Conveyor conveyor = Conveyor.getInstance();
    private final Intake intake = Intake.getInstance();
    private final Shooter shooter = Shooter.getInstance();
    private final double runTime;
    private final Timer timer = new Timer();

    public RunPipeline(double runTime) {
        this.runTime = runTime;

        addRequirements(conveyor, intake, shooter);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
    }

    @Override
    public void execute() {
        conveyor.setPower(1);
        shooter.setPower(0.75);
        intake.setPower(0.5);
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.setPower(0);
        shooter.setPower(0);
        intake.setPower(0);
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= runTime;
    }
}
