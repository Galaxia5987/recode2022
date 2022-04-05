package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.Conveyor;

public class SmartIndexing extends CommandBase {
    private final Conveyor conveyor;
    private final Color enemyColor;
    private boolean outtake;

    public SmartIndexing(Conveyor conveyor) {
        this.conveyor = conveyor;
        this.enemyColor = conveyor.allianceToColor(DriverStation.getAlliance())
                == Constants.Conveyor.RED ?
                Constants.Conveyor.BLUE :
                Constants.Conveyor.RED;
        this.outtake = false;
    }

    private boolean enemyCargoEntered() {
        return Superstructure.getInstance().cargoHasEntered() && conveyor.getColor().equals(enemyColor);
    }

    private boolean enemyCargoExited() {
        return Superstructure.getInstance().cargoHasExited() && conveyor.getLastColor().equals(enemyColor);
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().getLeftTrigger()) {
            if (enemyCargoEntered()) {
                outtake = true;
                conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
            } else if (!outtake) {
                conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
            }
        } else if (Infrastructure.getInstance().getLeftBumper()) {
            conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
        }

        if (enemyCargoExited()) {
            outtake = false;
        }
    }
}
