package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.Units;
import frc.robot.utils.Utils;

import java.util.ArrayList;

public class Superstructure implements PeriodicSubsystem {

    private static final SwerveDrive driveBase = SwerveDrive.getFieldOrientedInstance();
    private static final Shooter shooter = Shooter.getInstance();

    private static final ArrayList<PeriodicSubsystem> subsystems = new ArrayList<>() {{
        add(driveBase);
        add(shooter);
    }};

    public static double getRobotVelocity() {
        return Math.hypot(
                driveBase.getChassisSpeeds().vxMetersPerSecond,
                driveBase.getChassisSpeeds().vyMetersPerSecond);
    }

    public static boolean isFlywheelAtSetpoint(double setpoint) {
        if (setpoint < 0) {
            return false;
        }

        return Utils.deadband(shooter.getVelocity() - setpoint, Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) == 0;
    }

    @Override
    public void outputTelemetry() {
        for (PeriodicSubsystem subsystem : subsystems) {
            subsystem.outputTelemetry();
        }
    }

    @Override
    public void periodic() {
        for (PeriodicSubsystem subsystem : subsystems) {
            subsystem.periodic();
        }
    }

    @Override
    public Units.Types getUnitType() {
        return Units.Types.NONE;
    }
}
