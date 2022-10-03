package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;
import frc.robot.valuetuner.WebConstant;

public class ShootCargo extends ParallelCommandGroup {
    private final WebConstant hoodAngle = WebConstant.of("Hood", "Hood Angle", 10);
    private final WebConstant shooterVelocity = WebConstant.of("Shooter", "Shooter Velocity", 3900);

    public ShootCargo(boolean tuneMeasurements) {
        if (tuneMeasurements) {
            addCommands(
                    new AdjustAngle(hoodAngle::get),
                    new Shoot(shooterVelocity::get),
                    new WaitUntilCommand(() -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND)).andThen(new WaitCommand(0.2)).andThen(
                            new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> true))
            );
        } else {
            addCommands(
                    new AdjustAngle(() -> Constants.interpolateMap(Constants.Hood.HOOD_MEASUREMENTS, IntegratedUtils.distanceToTarget())),
                    new Shoot(() -> Constants.interpolateMap(Constants.Shooter.SHOOT_MEASUREMENTS, IntegratedUtils.distanceToTarget())),
                    new WaitUntilCommand(() -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND)).andThen(new WaitCommand(0.2)).andThen(
                            new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> true))
            );
        }
    }
}
