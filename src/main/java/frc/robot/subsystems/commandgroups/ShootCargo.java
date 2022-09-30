package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;
import frc.robot.valuetuner.WebConstant;

import java.util.function.Supplier;

public class ShootCargo extends ParallelCommandGroup {
    private final WebConstant hoodAngle = WebConstant.of("Hood", "Hood Angle", 10);
    private final WebConstant shooterVelocity = WebConstant.of("Shooter", "Shooter Velocity", 3900);

    public ShootCargo() {
        final Supplier<Constants.ShootData> shootDataSupplier =
                () -> Constants.interpolateMeasurements(IntegratedUtils.distanceToTarget());

        addCommands(
                new AdjustAngle(() ->  Shoot.getSetpointVelocity(Constants.Hood.HOOD_MEASUREMENTS, IntegratedUtils.distanceToTarget()))
                        .withInterrupt(() -> Hood.getInstance().atSetpoint(Constants.Hood.ALLOWABLE_ERROR)),
                new Shoot(() -> Shoot.getSetpointVelocity(Constants.Shooter.SHOOT_MEASUREMENTS, IntegratedUtils.distanceToTarget())),
//                new RunCommand(() -> Shooter.getInstance().setPower(0.7)),
                new WaitUntilCommand(() -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND)).andThen(new WaitCommand(1)).andThen(
                        new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> true))
/*
        addCommands(
                new AdjustAngle(hoodAngle::get)
                        .withInterrupt(() -> Hood.getInstance().atSetpoint(Constants.Hood.ALLOWABLE_ERROR)),
                new Shoot(shooterVelocity::get),
//                new RunCommand(() -> Shooter.getInstance().setPower(0.7)),
                new WaitUntilCommand(() -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND)).andThen(new WaitCommand(1)).andThen(
                        new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> true))*/
        );
    }
}
