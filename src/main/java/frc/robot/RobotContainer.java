package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.Outtake;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.commandgroups.WarmUp;
import frc.robot.subsystems.conveyor.commands.ConveyToShooter;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.DriveAndAdjust;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.vision.Limelight;

public class RobotContainer {
    private static RobotContainer INSTANCE = null;
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    private final Hood hood = Hood.getInstance();
    private final Limelight limelight = Limelight.getInstance();

    private final XboxController xboxController = new XboxController(0);

    private final Joystick leftJoystick = new Joystick(1);
    private final Joystick rightJoystick = new Joystick(2);

    private final JoystickButton rb = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
    private final JoystickButton lb = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
    private final Trigger rt = new Trigger(() -> xboxController.getRightTriggerAxis() > 0.7);
    private final Trigger lt = new Trigger(() -> xboxController.getLeftTriggerAxis() > 0.7);

    private final JoystickButton a = new JoystickButton(xboxController, XboxController.Button.kA.value);
    private final JoystickButton b = new JoystickButton(xboxController, XboxController.Button.kB.value);
    private final JoystickButton x = new JoystickButton(xboxController, XboxController.Button.kX.value);
    private final JoystickButton y = new JoystickButton(xboxController, XboxController.Button.kY.value);


    private RobotContainer() {
        configureDefaultCommands();
        configureButtonBindings();
    }

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
        swerveDrive.setDefaultCommand(new DriveAndAdjust(xboxController, xboxController::getRightBumper));
//       swerveDrive.setDefaultCommand(new DriveAndAdjustJoysticks(rightJoystick, leftJoystick, xboxController::getRightBumper));
//        Shooter.getInstance().setDefaultCommand(new ShootCargo().perpetually());
    }

    public void configureButtonBindings() {
        // TODO: Return hard code back to normal
        lb.whenPressed(Robot.navx::reset);
        rt.whileActiveContinuous(new ShootCargo(false));
        b.whileHeld(new Outtake());
        lt.whileActiveContinuous(new FeedAndConvey());
        y.toggleWhenPressed(new WarmUp());
        x.whileActiveContinuous(new ConveyToShooter(Constants.Conveyor.DEFAULT_POWER / 5.0));
    }

    public Command getAutonomousCommand() {
        return null;
    }
}
