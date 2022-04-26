package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.conveyor.commands.SimpleConvey;
import frc.robot.subsystems.drivetrain.commands.HolonomicDrive;
import frc.robot.subsystems.flap.commands.FlapDefaultCommand;
import frc.robot.subsystems.hood.commands.HoodDefaultCommand;
import frc.robot.subsystems.intake.commands.IntakeCargo;
import frc.robot.subsystems.shooter.commands.Shoot;

public class Infrastructure extends Superstructure implements ChassisController, ClimberController {
    private static Infrastructure INSTANCE = null;

    private Infrastructure() {
        super();
    }

    public static Infrastructure getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Infrastructure();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        swerve.setDefaultCommand(new HolonomicDrive(swerve));
        shooter.setDefaultCommand(new Shoot(shooter));
        conveyor.setDefaultCommand(new SimpleConvey(conveyor));
        intake.setDefaultCommand(new IntakeCargo(intake));
        hood.setDefaultCommand(new HoodDefaultCommand(hood));
        flap.setDefaultCommand(new FlapDefaultCommand(flap));
    }

    public Command getAutonomousCommand() {
        return null;
    }

}
