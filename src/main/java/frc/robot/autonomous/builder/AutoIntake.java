package frc.robot.autonomous.builder;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.flap.Flap;
import frc.robot.subsystems.intake.Intake;

import java.util.HashSet;
import java.util.Set;

public class AutoIntake implements BuilderUtil {
    private static AutoIntake INSTANCE = null;
    private final Conveyor conveyor = Conveyor.getInstance();
    private final Intake intake = Intake.getInstance();
    private final Flap flap = Flap.getInstance();
    private final Color enemyColor = conveyor.allianceToColor(DriverStation.getAlliance()) // TODO: Hardcode for testing
            .equals(Constants.Conveyor.RED) ? Constants.Conveyor.BLUE : Constants.Conveyor.RED;

    private AutoIntake() {
    }

    public static AutoIntake getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoIntake();
        }
        return INSTANCE;
    }

    public Command simpleIntakeCargo() {
        return new Command() {
            @Override
            public void initialize() {
                flap.setMode(true);
            }

            @Override
            public void execute() {
                conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
                intake.setPower(Constants.Intake.DEFAULT_POWER);
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    public Command smartIndexingIntakeCargo() {
        return new Command() {
            boolean outtake;

            @Override
            public void initialize() {
                flap.setMode(true);
                outtake = false;
            }

            @Override
            public void execute() {
                if (enemyCargoExited()) {
                    outtake = false;
                }
                if (enemyCargoEntered()) {
                    outtake = true;
                    conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
                } else if (!outtake) {
                    conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
                }
            }

            @Override
            public Set<Subsystem> getRequirements() {
                return defaultRequirements();
            }
        };
    }

    private boolean enemyCargoEntered() {
        return Superstructure.getInstance().cargoHasEntered() && conveyor.getColor().equals(enemyColor);
    }

    private boolean enemyCargoExited() {
        return Superstructure.getInstance().cargoHasExited() && conveyor.getLastColor().equals(enemyColor);
    }

    @Override
    public Set<Subsystem> defaultRequirements() {
        return new HashSet<>() {{
            add(conveyor);
            add(intake);
            add(flap);
        }};
    }
}
