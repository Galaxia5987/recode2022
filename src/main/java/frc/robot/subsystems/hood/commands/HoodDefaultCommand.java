package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.vision.Limelight;

import java.util.function.DoubleSupplier;

public class HoodDefaultCommand extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final DoubleSupplier angle;


    public HoodDefaultCommand(DoubleSupplier angle) {
        this.angle = angle;
        addRequirements(hood);
    }


    @Override
    public void execute() {
        hood.setAngle(angle.getAsDouble());
    }
}
