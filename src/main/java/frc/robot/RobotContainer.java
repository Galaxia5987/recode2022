package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.Outtake;
import frc.robot.subsystems.conveyor.commands.ConveyAll;
import frc.robot.subsystems.conveyor.commands.ConveyToShooter;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.DriveAndAdjust;
import frc.robot.subsystems.drivetrain.commands.DriveAndAdjustJoysticks;
import frc.robot.subsystems.helicopter.commands.JoystickHelicopter;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.hood.commands.AdjustAngle;
import frc.robot.subsystems.intake.commands.ToggleIntake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.commands.Shoot;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.valuetuner.WebConstant;

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

//        swerveDrive.setDefaultCommand(new HolonomicDriveJoysticks(rightJoystick, leftJoystick));


    }

    public void configureButtonBindings() {
        // TODO: Return hard code back to normal
        new JoystickHelicopter(xboxController);
        lb.whenPressed(Robot.navx::reset);
        x.whileHeld(new AdjustAngle());
        rt.whileActiveContinuous(new ParallelCommandGroup(new Shoot(() -> 0),
                new ConveyAll(Constants.Conveyor.MAX_POWER, () -> Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND))
        ));

//        b.whileHeld(new Outtake());
//        lt.whileActiveContinuous(new ParallelCommandGroup(new FeedAndConvey(), new ConveyToShooter(Constants.Conveyor.DEFAULT_UPPER)));
        //y.whenPressed(new ToggleIntake());

//        lt.whileActiveContinuous(() -> Shooter.getInstance().setPower(0.75));
//        rt.whileActiveContinuous(new ShootCargo());
        y.whenPressed(() -> {
            var blah = limelight.estimatePose(Robot.getAngle());
            blah.ifPresent(pose2d -> swerveDrive.resetOdometry(pose2d, Robot.getAngle()));
        });
    }

    public Command getAutonomousCommand() {
        return null;
    }
}
