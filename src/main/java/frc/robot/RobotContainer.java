package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.bits.RunAllBits;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.Outtake;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.commandgroups.Warmup;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.DriveAndAdjust;
import frc.robot.subsystems.drivetrain.commands.DriveAndAdjustJoysticks;
import frc.robot.subsystems.drivetrain.commands.HolonomicDrive;
import frc.robot.subsystems.drivetrain.commands.HolonomicDriveJoysticks;
import frc.robot.subsystems.intake.commands.ToggleIntake;

public class RobotContainer {
    private static RobotContainer INSTANCE = null;
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();

    private final XboxController xboxController = new XboxController(0);

    private final Joystick leftJoystick = new Joystick(1);
    private final Joystick rightJoystick = new Joystick(2);

    private final JoystickButton rb = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
    private final JoystickButton lb = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
    private final JoystickButton rt = new JoystickButton(xboxController, XboxController.Axis.kRightTrigger.value);
    private final JoystickButton lt = new JoystickButton(xboxController, XboxController.Axis.kLeftTrigger.value);

    private final JoystickButton a = new JoystickButton(xboxController, XboxController.Button.kA.value);
    private final JoystickButton b = new JoystickButton(xboxController, XboxController.Button.kB.value);
    private final JoystickButton x = new JoystickButton(xboxController, XboxController.Button.kX.value);
    private final JoystickButton y = new JoystickButton(xboxController, XboxController.Button.kY.value);

    private RobotContainer() {
    }

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        swerveDrive.setDefaultCommand(new HolonomicDrive(xboxController));
//        swerveDrive.setDefaultCommand(new HolonomicDriveJoysticks(rightJoystick, leftJoystick));
    }

    public void configureButtonBindings() {
        lb.whenPressed(Robot.navx::reset);
        rt.whenHeld(new ShootCargo());
        lt.whenPressed(new FeedAndConvey());

        b.whileHeld(new Outtake());
        x.whenPressed(new ToggleIntake());
        y.toggleWhenActive(new Warmup());
    }

    public Command getAutonomousCommand() {
        return new RunAllBits();
    }
}
