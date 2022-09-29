package frc.robot.subsystems.hood.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.IntegratedUtils;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.vision.Limelight;

import java.util.function.DoubleSupplier;

public class HoodDefaultCommand extends CommandBase {
    private final Hood hood = Hood.getInstance();


    public HoodDefaultCommand() {
        addRequirements(hood);
    }


    @Override
    public void execute() {
        hood.setAngle(Constants.interpolateMeasurements(IntegratedUtils.distanceToTarget()).hoodAngle);
    }
}
