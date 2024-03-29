package frc.robot.subsystems.shooter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;

import java.util.function.DoubleSupplier;

public class Shoot extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Limelight vision = Limelight.getInstance();

    private final DoubleSupplier velocitySupplier;

    public Shoot(DoubleSupplier velocitySupplier) {
        this.velocitySupplier = velocitySupplier;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.setVelocity(velocitySupplier.getAsDouble());

    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
