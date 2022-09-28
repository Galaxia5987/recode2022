package frc.robot.subsystems.shooter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
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
        shooter.setVelocity(shooter.velocityForDistance(vision.getDistance().orElse(0)));

    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
