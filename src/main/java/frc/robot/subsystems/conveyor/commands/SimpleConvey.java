package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Infrastructure;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.Conveyor;

public class SimpleConvey extends CommandBase {
    protected final Conveyor conveyor;

    public SimpleConvey(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    protected boolean shoot() {
        return Infrastructure.getInstance().getRightTrigger() &&
                Superstructure.getInstance().isFlywheelAtSetpoint() &&
                Superstructure.getInstance().robotAtAllowableYawError();
    }

    @Override
    public void execute() {
        if (Infrastructure.getInstance().getLeftTrigger() || shoot()) {
            conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
        } else if (Infrastructure.getInstance().getLeftBumper() || Infrastructure.getInstance().getRightBumper()) {
            conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
        } else {
            conveyor.setPower(0);
        }
    }
}
