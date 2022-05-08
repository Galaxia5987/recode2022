package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.Conveyor;

public class SmartIndexing extends SimpleConvey {
    private final Color enemyColor;
    private boolean outtake;

    public SmartIndexing(Conveyor conveyor) {
        super(conveyor);
        this.enemyColor = conveyor.allianceToColor(DriverStation.getAlliance()) // TODO: Hardcode for testing
                .equals(Constants.Conveyor.RED) ?
                Constants.Conveyor.BLUE :
                Constants.Conveyor.RED;
        this.outtake = false;

        addRequirements(conveyor);
    }

    private boolean enemyCargoEntered() {
        return Superstructure.getInstance().cargoHasEntered() && conveyor.getColor().equals(enemyColor);
    }

    private boolean enemyCargoExited() {
        return Superstructure.getInstance().cargoHasExited() && conveyor.getLastColor().equals(enemyColor);
    }

    @Override
    public void execute() {
        if (enemyCargoExited()) {
            outtake = false;
        }

        if (RobotContainer.getInstance().chassisGetLeftTrigger() || shoot()) {
            if (enemyCargoEntered()) {
                outtake = true;
                conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
            } else if (!outtake) {
                conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
            }
        } else if (RobotContainer.getInstance().chassisGetLeftBumper() || RobotContainer.getInstance().chassisGetRightBumper()) {
            conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
        }
    }
}
