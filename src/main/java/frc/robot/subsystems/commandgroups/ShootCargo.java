package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;

import java.util.function.Supplier;

public class ShootCargo extends SequentialCommandGroup {

    public ShootCargo() {
        final Supplier<Constants.ShootData> shootDataSupplier =
                () -> Constants.interpolateMeasurements(IntegratedUtils.distanceToTarget());

        addCommands(
                new AdjustAngle(() -> shootDataSupplier.get().hoodAngle)
                        .alongWith(new Shoot(() -> shootDataSupplier.get().shooterVelocity))
                        .withInterrupt(() -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND)),
                new ConveyAll(Constants.Conveyor.DEFAULT_POWER)
        );
    }
}
