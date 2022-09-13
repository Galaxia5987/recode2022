package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.vision.Limelight;

import java.util.function.DoubleSupplier;

public class HoodDefaultCommands extends CommandBase {
    private final Hood hood = Hood.getInstance();
    private final Limelight limelight = Limelight.getInstance();
    private DoubleSupplier angle;


    public HoodDefaultCommands(DoubleSupplier angle) {
        this.angle = angle;
    }


    @Override
    public void execute() {
        if (limelight.hasTargets()){

        }
    }
}
