package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.valuetuner.WebConstant;

import java.util.function.DoubleSupplier;

public class AdjustAngle extends CommandBase {
    private final Limelight vision = Limelight.getInstance();
    private final Hood hood = Hood.getInstance();
    private final Timer timer = new Timer();
    private final DoubleSupplier angle;

    public AdjustAngle(DoubleSupplier angle) {
        this.angle = angle;
        addRequirements(hood);
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
    }

    @Override
    public void execute() {
        hood.setAngle(angle.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        hood.stop();
    }

    @Override
    public boolean isFinished() {
        return hood.atSetpoint(Constants.Hood.ALLOWABLE_ERROR);
    }
}
