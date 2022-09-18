package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;
import frc.robot.valuetuner.WebConstant;

public class RobotContainer {
    private static RobotContainer INSTANCE = null;
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    private final Hood hood = Hood.getInstance();

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

    private final WebConstant shooterVelocity = WebConstant.of("Shooter", "velocity", 3000);
    private final WebConstant hoodAngle = WebConstant.of("Hood", "angle", 10);

    private RobotContainer() {
    }

    public static RobotContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotContainer();
        }
        return INSTANCE;
    }

    public void configureDefaultCommands() {
//        swerveDrive.setDefaultCommand(new HolonomicDrive(xboxController));
//        swerveDrive.setDefaultCommand(new HolonomicDriveJoysticks(rightJoystick, leftJoystick));
//        hood.setDefaultCommand(new JoystickHood(xboxController));
    }

    public void configureButtonBindings() {
//        lb.whenPressed(Robot.navx::reset);
        rt.whileActiveContinuous(new Shoot(shooterVelocity::get));
        lt.whileActiveContinuous(() -> Shooter.getInstance().setPower(0.75));
//        lt.whenPressed(new FeedAndConvey());

        a.whileHeld(new AdjustAngle(hoodAngle::get));
//        b.whileHeld(new Outtake());
//        x.whenPressed(new ToggleIntake());
//        y.toggleWhenActive(new Warmup());
    }

    public Command getAutonomousCommand() {
        return null;
    }
}
