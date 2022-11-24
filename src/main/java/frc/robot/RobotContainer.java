package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.auto.*;
import frc.robot.subsystems.bits.RunAllBits;
import frc.robot.subsystems.commandgroups.FeedAndConvey;
import frc.robot.subsystems.commandgroups.Outtake;
import frc.robot.subsystems.commandgroups.ShootCargo;
import frc.robot.subsystems.commandgroups.WarmUp;
import frc.robot.subsystems.conveyor.commands.ConveyToShooter;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.DriveAndAdjustJoysticks;
import frc.robot.subsystems.helicopter.Helicopter;
import frc.robot.subsystems.helicopter.commands.JoystickHelicopter;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Limelight;

import java.util.Map;

public class RobotContainer {
    private static RobotContainer INSTANCE = null;
    private final SwerveDrive swerveDrive = SwerveDrive.getFieldOrientedInstance();
    private final Hood hood = Hood.getInstance();
    private final Limelight limelight = Limelight.getInstance();
    private final Helicopter helicopter = Helicopter.getInstance();
    private final SelectCommand autoCommand;

    private final XboxController xboxController = new XboxController(0);

    private final Joystick leftJoystick = new Joystick(1);
    private final Joystick rightJoystick = new Joystick(2);

    private final JoystickButton rb = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
    private final JoystickButton lb = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
    private final JoystickButton joystickRightTrigger = new JoystickButton(rightJoystick, 1);
    private final Trigger rt = new Trigger(() -> xboxController.getRightTriggerAxis() > 0.7);
    private final Trigger lt = new Trigger(() -> xboxController.getLeftTriggerAxis() > 0.7);

    private final JoystickButton a = new JoystickButton(xboxController, XboxController.Button.kA.value);
    private final JoystickButton b = new JoystickButton(xboxController, XboxController.Button.kB.value);
    private final JoystickButton x = new JoystickButton(xboxController, XboxController.Button.kX.value);
    private final JoystickButton y = new JoystickButton(xboxController, XboxController.Button.kY.value);
    private final JoystickButton start = new JoystickButton(xboxController, XboxController.Button.kStart.value);
    private final Trigger right = new Trigger(() -> xboxController.getPOV() == 90);
    private final Trigger left = new Trigger(() -> xboxController.getPOV() == 270);
    private final Trigger up = new Trigger(() -> xboxController.getPOV() == 0);
    private final Trigger down = new Trigger(() -> xboxController.getPOV() == 180);

    private RobotContainer() {
        SmartDashboard.putString("autoCommandName", "BottomFive");
        var commands = Map.<Object, Command>of(
                "BottomFive", new BottomFive(),
                "BottomFour", new BottomFour(),
                "TopThree", new TopThree(),
                "MiddleUpTwo", new MiddleUpTwo(),

                "MiddleDownThree", new MiddleDownThree(),
                "Test", new Test(),
                "RunAllBits", new RunAllBits());
        autoCommand = new SelectCommand(commands, () -> SmartDashboard.getString("autoCommandName", "BottomFive"));
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
//        swerveDrive.setDefaultCommand(new DriveAndAdjust(xboxController, xboxController::getRightBumper));
        swerveDrive.setDefaultCommand(new DriveAndAdjustJoysticks(rightJoystick, leftJoystick, xboxController::getRightBumper));
//        Shooter.getInstance().setDefaultCommand(new ShootCargo().perpetually());
        helicopter.setDefaultCommand(new JoystickHelicopter(xboxController));
    }

    public void configureButtonBindings() {
        SmartDashboard.putNumber("Velocity", 3900);
        SmartDashboard.putNumber("Angle", 10);
        // TODO: Return hard code back to normal
        joystickRightTrigger.whenPressed(Robot.navx::reset);

        rt.whileActiveContinuous(new ShootCargo());
//        rt.whileActiveContinuous(new ShootCargoOverride(left::get, down::get));
             b.whileHeld(new Outtake());
           lt.whileActiveContinuous(new FeedAndConvey());
       y.toggleWhenPressed(new WarmUp());
              a.whileActiveContinuous(new ConveyToShooter(Constants.Conveyor.DEFAULT_POWER / 2.5));
           x.whenPressed(new InstantCommand(() -> Intake.getInstance().toggleMechanism()));
//        right.whileActiveContinuous(new SimpleShoot(() -> 2000, () -> 5)
//                .raceWith(new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () -> true)));
//        rt.whileActiveContinuous(new SimpleShoot(
//                () -> SmartDashboard.getNumber("Velocity", 3900),
//                () -> SmartDashboard.getNumber("Angle", 10)
//        ).alongWith(new ConveyAll(Constants.Conveyor.DEFAULT_POWER,
//                () -> hood.atSetpoint(Constants.Hood.ALLOWABLE_ERROR) &&
//                        Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND))
//        ));
//        up.whileActiveContinuous(new SimpleShoot(
//                () -> Constants.interpolateMap(Constants.Shooter.SHOOT_MEASUREMENTS, 5.15),
//                () -> Constants.interpolateMap(Constants.Hood.HOOD_MEASUREMENTS, 5.15)
//        ).alongWith(new ConveyAll(Constants.Conveyor.DEFAULT_POWER,
//                () -> hood.atSetpoint(Constants.Hood.ALLOWABLE_ERROR) &&
//                        Shooter.getInstance().atSetpoint(Constants.Shooter.SHOOTER_VELOCITY_DEADBAND))
//        ));
//        start.whileHeld(new ResetHood());
        //  right.whileActiveContinuous(new ConveyAll(Constants.Conveyor.DEFAULT_POWER, () ->true));
        //  left.whileActiveContinuous(new ConveyAll(-Constants.Conveyor.DEFAULT_POWER, () ->true));
    }

    public Command getAutonomousCommand() {
//        return autoCommand;
//      return new MiddleDownThree();
        return new MiddleUpTwo();
//        return new TopThree();
    }
}
