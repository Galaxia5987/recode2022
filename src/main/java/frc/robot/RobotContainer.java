package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.FiveBallAuto;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.commands.SimpleConvey;
import frc.robot.subsystems.drivetrain.commands.HolonomicDrive;
import frc.robot.subsystems.flap.commands.FlapDefaultCommand;
import frc.robot.subsystems.hood.commands.HoodDefaultCommand;
import frc.robot.subsystems.intake.commands.IntakeCargo;
import frc.robot.subsystems.shooter.commands.Shoot;

import java.util.HashMap;

public class RobotContainer extends Superstructure implements ChassisController, ClimberController {
    private static RobotContainer INSTANCE = null;
    private final ShuffleboardTab autonomousSelector = Shuffleboard.getTab("Auto Selector");
    private final SuppliedValueWidget<String> selectorButton = autonomousSelector
            .addString("Choose Auto", () -> "Five Cargo")
            .withSize(8, 8);
    private final HashMap<String, Command> autonomousRoutines = new HashMap<>() {{
        put("Five Cargo", new FiveBallAuto());
    }};

    private RobotContainer() {
        super();
    }

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        swerve.setDefaultCommand(new HolonomicDrive(swerve, false));
        shooter.setDefaultCommand(new Shoot(shooter));
        conveyor.setDefaultCommand(new SimpleConvey(conveyor));
        intake.setDefaultCommand(new IntakeCargo(intake));
        hood.setDefaultCommand(new HoodDefaultCommand(hood));
        flap.setDefaultCommand(new FlapDefaultCommand(flap));
    }

    public Command getAutonomousCommand() {
        return autonomousRoutines.get(SmartDashboard.getString("Choose Auto", "Five Cargo"));
    }
}
