package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform2d;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.Conveyor;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.flap.Flap;
import frc.robot.subsystems.hood.Hood;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.PhotonVisionModule;
import frc.robot.utils.Utils;

import java.util.ArrayList;

public class Superstructure implements PeriodicSubsystem {
    private static Superstructure INSTANCE = null;
    protected final SwerveDrive swerve = SwerveDrive.getInstance();
    protected final Shooter shooter = Shooter.getInstance();
    protected final PhotonVisionModule visionModule = PhotonVisionModule.getInstance();
    protected final Conveyor conveyor = Conveyor.getInstance();
    protected final Intake intake = Intake.getInstance();
    protected final Hood hood = Hood.getInstance();
    protected final Flap flap = Flap.getInstance();
    protected final ArrayList<PeriodicSubsystem> subsystems;

    protected Superstructure() {
        subsystems = new ArrayList<>();
        subsystems.add(swerve);
        subsystems.add(shooter);
        subsystems.add(visionModule);
        subsystems.add(conveyor);
        subsystems.add(intake);
        subsystems.add(hood);
        subsystems.add(flap);
    }

    public static Superstructure getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Superstructure();
        }
        return INSTANCE;
    }

    public double getRobotVelocity() {
        return Math.hypot(swerve.getChassisSpeeds().vxMetersPerSecond, swerve.getChassisSpeeds().vyMetersPerSecond);
    }

    public boolean isFlywheelAtSetpoint() {
        return Utils.deadband(shooter.getVelocity() - shooter.getSetpoint(), Constants.Shooter.SHOOTER_VELOCITY_DEADBAND) == 0;
    }

    public boolean cargoHasEntered() {
        return conveyor.getPower() > 0 && conveyor.newObjectSensed();
    }

    public boolean cargoHasExited() {
        return conveyor.getPower() < 0 && conveyor.oldObjectExited();
    }

    public double getDistanceFromTarget() {
        if (visionModule.hasTarget()) {
            return visionModule.getDistance();
        }
        Transform2d robotToTarget = Constants.Vision.HUB_POSE.minus(getOdometry());
        return Math.hypot(robotToTarget.getX(), robotToTarget.getY());
    }

    public double getYawFromTarget() { // TODO: Check units
        return visionModule.getYaw().orElse(180);
    }

    public Pose2d getOdometry() {
        return swerve.getPose();
    }

    public boolean targetVisible() {
        return visionModule.hasTarget();
    }

    public boolean robotAtAllowableYawError() {
        return Utils.deadband(getYawFromTarget(), Constants.SwerveDrive.ALLOWABLE_HEADING_ERROR) == 0;
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
