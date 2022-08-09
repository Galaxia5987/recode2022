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
    private double startTime;

    public RunPipeline(double runTime) {
        this.runTime = runTime;

        addRequirements(conveyor, intake, shooter);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        conveyor.setPower(1);
        shooter.setPower(0.75);
        intake.setPower(0.5);
    }

    @Override
    public boolean isFinished() {
        return runTime - (Timer.getFPGATimestamp() - startTime) <= 0;
    }
}
