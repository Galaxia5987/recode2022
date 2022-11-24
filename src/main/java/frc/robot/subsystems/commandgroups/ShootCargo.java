package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;
import frc.robot.valuetuner.WebConstant;

public class ShootCargo extends ParallelCommandGroup {
    private final WebConstant hoodAngle = WebConstant.of("Hood", "Hood Angle", 10);
    private final WebConstant shooterVelocity = WebConstant.of("Shooter", "Shooter Velocity", 3900);
    private final WebConstant tuneMeasurements = WebConstant.of("Shooter", "Tune", 0);

    public ShootCargo() {
        addCommands(
//                new ConditionalCommand(
//                        new ParallelCommandGroup(
//                                new AdjustAngle(hoodAngle::get),
//                                new Shoot(shooterVelocity::get),
//                                new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) &&
//                                        Hood.getInstance().atSetpoint(Constants.Hood.ALLOWABLE_ERROR))
//                        ),
                        new ParallelCommandGroup(
                                new AdjustAngle(() -> Constants.interpolateMap(Constants.Hood.HOOD_MEASUREMENTS, IntegratedUtils.distanceToTarget())),
                                new Shoot(() -> Constants.interpolateMap(Constants.Shooter.SHOOT_MEASUREMENTS, IntegratedUtils.distanceToTarget())),
                                new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) &&
                                       Hood.getInstance().atSetpoint(Constants.Hood.ALLOWABLE_ERROR))
                        )
 //                               , () -> tuneMeasurements.get() != 0);

        );
    }
}
