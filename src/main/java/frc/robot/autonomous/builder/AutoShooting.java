package frc.robot.autonomous.builder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.flap.Flap;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.shooter.ShootAndDriveUtil;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;

import java.util.HashSet;
import java.util.Set;

public class AutoShooting implements BuilderUtil {
    private static AutoShooting INSTANCE = null;
    private final Shooter shooter = Shooter.getDefaultInstance();
    private final Conveyor conveyor = Conveyor.getInstance();
    private final Flap flap = Flap.getInstance();
    private final Hood hood = Hood.getInstance();

    private AutoShooting() {
    }

    public static AutoShooting getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoShooting();
        }
        return INSTANCE;
    }

    public Command getSimpleShoot() {
        return new Command() {
            @Override
            public void initialize() {
                flap.setMode(false);
            }

            @Override
            public void execute() {
                double distance = Superstructure.getInstance().getDistanceFromTarget();
                shooter.setVelocity(Shoot.distanceToVelocity(distance));
                hood.setMode(distance > Constants.Hood.DISTANCE_FROM_TARGET_THRESHOLD);

                if (Superstructure.getInstance().isFlywheelAtSetpoint() && Superstructure.getInstance().robotAtAllowableYawError()) {
                    conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
                } else {
                    conveyor.setPower(0);
                }
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    public Command getVirtualShoot() {
        return new Command() {
            @Override
            public void initialize() {
                flap.setMode(false);
            }

            @Override
            public void execute() {
                double distance = ShootAndDriveUtil.getVirtualTarget().getNorm();
                shooter.setVelocity(Shoot.distanceToVelocity(distance));
                hood.setMode(distance > Constants.Hood.DISTANCE_FROM_TARGET_THRESHOLD);

                if (Superstructure.getInstance().isFlywheelAtSetpoint() && Superstructure.getInstance().robotAtAllowableYawError()) {
                    conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
                } else {
                    conveyor.setPower(0);
                }
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    public Command getSimpleWarmup() {
        return new Command() {
            @Override
            public void execute() {
                shooter.setVelocity(Constants.Shooter.WARMUP_VELOCITY);
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    public Command getOdometryWarmup() {
        return new Command() {
            @Override
            public void execute() {
                double distance = Superstructure.getInstance().getDistanceFromTarget();
                shooter.setVelocity(Shoot.distanceToVelocity(distance));
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    public Set<Subsystem> defaultRequirements() {
        return new HashSet<>() {{
            add(shooter);
            add(conveyor);
            add(flap);
            add(hood);
        }};
    }
}
