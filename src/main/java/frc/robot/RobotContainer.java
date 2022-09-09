package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.bits.RunAllBits;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.HolonomicDriveJoysticks;
import frc.robot.subsystems.drivetrain.commands.ZeroModules;

public class RobotContainer {
    private static RobotContainer INSTANCE = null;
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();

    private final XboxController xboxController = new XboxController(0);
    private final JoystickButton rb = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
    private final JoystickButton a = new JoystickButton(xboxController, XboxController.Button.kA.value);
    private final Joystick rightJoystick = new Joystick(2);
    private final Joystick leftJoystick = new Joystick(1);

    private RobotContainer() {
    }

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        a.whenPressed(new ZeroModules());
        rb.whenPressed(new InstantCommand(Robot.navx::reset));
        swerveDrive.setDefaultCommand(new HolonomicDriveJoysticks(rightJoystick, leftJoystick));
    }

    public Command getAutonomousCommand() {
        return new RunAllBits();
    }
}
