package frc.robot.subsystems.commandgroups;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;
import frc.robot.valuetuner.WebConstant;

import java.util.function.BooleanSupplier;

public class ShootCargoOverride extends ParallelCommandGroup {
    private final WebConstant hoodAngle = WebConstant.of("Hood", "Hood Angle", 0);
    private final WebConstant shooterVelocity = WebConstant.of("Shooter", "Shooter Velocity", 0);
    private final WebConstant tuneMeasurements = WebConstant.of("Shooter", "Tune", 0);

    public ShootCargoOverride(BooleanSupplier override, BooleanSupplier constAngle) {

        addCommands(
              /*  new ConditionalCommand(
                        new ParallelCommandGroup(
                                new AdjustAngle(hoodAngle::get),
                                new Shoot(shooterVelocity::get),
                                new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> (Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) &&
                                        Hood.getInstance().atSetpoint(Constants.Hood.ALLOWABLE_ERROR)) ||
                                        override.getAsBoolean())
                        ),*/
                        new ParallelCommandGroup(
                                new AdjustAngle(() -> constAngle.getAsBoolean() ? 15.5 : Constants.interpolateMap(Constants.Hood.HOOD_MEASUREMENTS, IntegratedUtils.distanceToTarget()) - SmartDashboard.getNumber("OffsetAngle", 0)),
                                new Shoot(() -> Constants.interpolateMap(Constants.Shooter.SHOOT_MEASUREMENTS, IntegratedUtils.distanceToTarget()) - SmartDashboard.getNumber("OffsetVelocity", 0)),
                                new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> (Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) &&
                                        Hood.getInstance().atSetpoint(Constants.Hood.ALLOWABLE_ERROR)) ||
                                        override.getAsBoolean())
                        )
//                        , () -> tuneMeasurements.get() != 0)

        );
    }
}
