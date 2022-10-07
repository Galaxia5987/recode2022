// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.LoggedSubsystem;
import frc.robot.valuetuner.NetworkTableConstant;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedNetworkTables;
import org.littletonrobotics.junction.io.ByteLogReceiver;
import org.littletonrobotics.junction.io.LogSocketServer;
import webapp.Webserver;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LoggedRobot {
    public static final AHRS navx = new AHRS(SPI.Port.kMXP);
    private static final Rotation2d zeroAngle = new Rotation2d();
    public static boolean debug = true;
    private Command autonomousCommand;
    private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);


    public Robot() {
    }

    /**
     * Resets the angle of the navx to the current angle.
     */
    public static void resetAngle() {
        resetAngle(new Rotation2d());
    }

    /**
     * Resets the angle of the navx to the current angle.
     *
     * @param angle the angle in -180 to 180 degrees coordinate system.
     */
    public static void resetAngle(Rotation2d angle) {
        zeroAngle = getRawAngle().minus(angle);
    }

    /**
     * Gets the current angle of the robot in respect to the start angle.
     *
     * @return the current angle of the robot in respect to the start angle.
     */
    public static Rotation2d getAngle() {
        return getRawAngle().minus(zeroAngle);
    }

    /**
     * Gets the raw angle from the navx.
     *
     * @return the angle of the robot in respect to the angle of the robot initiation time.
     */
    public static Rotation2d getRawAngle() {
        return Robot.navx.getRotation2d();
    }

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        compressor.disable();
        RobotContainer robotContainer = RobotContainer.getInstance();
        autonomousCommand = robotContainer.getAutonomousCommand();

        try {
            new Webserver();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        setUseTiming(isReal()); // Run as fast as possible during replay
        LoggedNetworkTables.getInstance().addTable("/SmartDashboard"); // Log & replay "SmartDashboard" values (no tables are logged by default).
        Logger.getInstance().recordMetadata("ProjectName", "Recode2022"); // Set a metadata value

//        if (isReal()) {
        Logger.getInstance().addDataReceiver(new ByteLogReceiver("/media/sda1/")); // Log to USB stick (name will be selected automatically)
        Logger.getInstance().addDataReceiver(new LogSocketServer(5804)); // Provide log data over the network, viewable in Advantage Scope.
//        } else {
//            String path = ByteLogReplay.promptForPath(); // Prompt the user for a file path on the command line
//            Logger.getInstance().setReplaySource(new ByteLogReplay(path)); // Read log file for replay
//            Logger.getInstance().addDataReceiver(new ByteLogReceiver(ByteLogReceiver.addPathSuffix(path, "_sim"))); // Save replay results to a new log with the "_sim" suffix
//        }

        Logger.getInstance().start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.
        NetworkTableConstant.initializeAllConstants();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        updateLogger();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
     * uncomment the getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to the switch structure
     * below with additional strings. If using the SendableChooser make sure to add them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomousCommand = Autonomous.get();

        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    /**
     * This function is called once when teleop is enabled.
     */
    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    }

    /**
     * This function is called once when the robot is disabled.
     */
    @Override
    public void disabledInit() {
    }

    /**
     * This function is called periodically when disabled.
     */
    @Override
    public void disabledPeriodic() {
    }

    /**
     * This function is called once when test mode is enabled.
     */
    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    private void updateLogger() {
        var subsystems = LoggedSubsystem.getSubsystems();
        for (var subsystem : subsystems) {
            subsystem.updateInputs();
            Logger.getInstance().processInputs(subsystem.getSubsystemName(), subsystem.getLoggerInputs());
        }
    }
}
