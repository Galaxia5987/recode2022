package frc.robot.subsystems.CommandGroups.Intake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.commands.Shoot;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class ShootCargo extends ParallelCommandGroup {

    public ShootCargo(final DoubleSupplier distanceSupplier) {
        final Supplier<Constants.ShootData> shootDataSupplier =
                () -> Constants.interpolateMeasurements(distanceSupplier.getAsDouble());
        
        addCommands(
                new AdjustAngle(() -> shootDataSupplier.get().hoodAngle),
                new Shoot(() -> shootDataSupplier.get().shooterVelocity)
        );
    }
}
