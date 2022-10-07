package frc.robot.subsystems.auto;

import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Limelight;

public class BottomFour extends AutoFunctions {
    public BottomFour(Shooter shooter, SwerveDrive swerveDrive, Conveyor conveyor, Intake intake, Hood hood, Limelight visionModule) {
        super(swerveDrive, shooter, conveyor, intake, hood, visionModule, "Bottom4.1");{
            addCommands(followPath("Bottom4.1"));
            addCommands(adjustAndShoot(3));
            addCommands(followPathAndPickUp("Bottom4.2"));
            addCommands(adjustAndShoot(3));
        }
    }
}