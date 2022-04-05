package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.PhotonVisionModule;
import frc.robot.utils.UnitObject;
import frc.robot.utils.Utils;

import java.util.ArrayList;

public class Superstructure implements PeriodicSubsystem {
    protected static final SwerveDrive swerve = SwerveDrive.getInstance();
    protected static final Shooter shooter = Shooter.getInstance();
    protected static final PhotonVisionModule visionModule = PhotonVisionModule.getInstance();
    protected static final Conveyor conveyor = Conveyor.getInstance();
    protected static final ArrayList<PeriodicSubsystem> subsystems = new ArrayList<>() {{
        add(swerve);
        add(shooter);
        add(visionModule);
        add(conveyor);
    }};

    private static Superstructure INSTANCE = null;

    public static Superstructure getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Superstructure();
        }
        return INSTANCE;
    }

    public double getRobotVelocity() {
        return Math.hypot(swerve.getChassisSpeeds().vxMetersPerSecond, swerve.getChassisSpeeds().vyMetersPerSecond);
    }

    public boolean isFlywheelAtSetpoint(UnitObject setpoint) {
        if (setpoint.getDirection() < 0) {
            return false;
        }
        return Utils.deadband(shooter.getVelocity() - setpoint.getRps(), Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) == 0;
    }

    public boolean cargoHasEntered() {
        return conveyor.getPower() > 0 && conveyor.newObjectSensed();
    }

    public boolean cargoHasExited() {
        return conveyor.getPower() < 0 && conveyor.oldObjectExited();
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
}
