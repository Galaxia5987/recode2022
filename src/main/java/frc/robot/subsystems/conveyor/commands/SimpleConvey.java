package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.Conveyor;

public class SimpleConvey extends CommandBase {
    protected final Conveyor conveyor;

    public SimpleConvey(Conveyor conveyor) {
        this.conveyor = conveyor;

        addRequirements(conveyor);
    }

    protected boolean shoot() {
        return RobotContainer.getInstance().chassisGetRightTrigger() &&
                Superstructure.getInstance().isFlywheelAtSetpoint() &&
                Superstructure.getInstance().robotAtAllowableYawError();
    }

    @Override
    public void execute() {
        if (RobotContainer.getInstance().chassisGetLeftTrigger() || shoot()) {
            conveyor.setPower(Constants.Conveyor.DEFAULT_POWER.get());
        } else if (RobotContainer.getInstance().chassisGetLeftBumper() || RobotContainer.getInstance().chassisGetRightBumper()) {
            conveyor.setPower(-Constants.Conveyor.DEFAULT_POWER.get());
        } else {
            conveyor.setPower(0);
        }
    }
}
