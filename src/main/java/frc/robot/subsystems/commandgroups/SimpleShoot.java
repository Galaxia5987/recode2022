package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.Shooter;

import java.util.function.DoubleSupplier;

public class SimpleShoot extends CommandBase {
    private final Shooter shooter = Shooter.getInstance();
    private final Hood hood = Hood.getInstance();

    private final DoubleSupplier velocity;
    private final DoubleSupplier angle;

    public SimpleShoot(DoubleSupplier velocity, DoubleSupplier angle) {
        this.velocity = velocity;
        this.angle = angle;
        addRequirements(shooter, hood);
    }

    @Override
    public void execute() {
        shooter.setVelocity(velocity.getAsDouble());
        hood.setAngle(angle.getAsDouble());

        System.out.println("Velocity: " + velocity.getAsDouble());
        System.out.println("Angle: " + angle.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        hood.stop();
    }
}
