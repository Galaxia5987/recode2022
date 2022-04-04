package frc.robot.subsystems.vision;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.net.PortForwarder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.PeriodicSubsystem;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.SimPhotonCamera;
import org.photonvision.SimVisionSystem;
import org.photonvision.targeting.PhotonPipelineResult;
import webapp.FireLog;

import java.util.Optional;
import java.util.OptionalDouble;

import static frc.robot.Constants.Vision.*;

public class PhotonVisionModule implements PeriodicSubsystem {
    private final PhotonCamera camera;
    private final SimPhotonCamera simCamera;
    private final SimVisionSystem simVisionSystem;
    private final LinearFilter filter = LinearFilter.movingAverage(5);
    private static PhotonVisionModule INSTANCE = null;

    private PhotonVisionModule() {
        if (Robot.isSimulation()) {
            camera = null;
            simCamera = new SimPhotonCamera("photonvision");
            simVisionSystem = new SimVisionSystem("photonvision", DIAG_FOV, CAMERA_PITCH, CAMERA_TO_ROBOT, CAMERA_HEIGHT, LED_RANGE, CAM_RESOLUTION_WIDTH, CAM_RESOLUTION_HEIGHT, MIN_TARGET_AREA);
            simVisionSystem.addSimVisionTarget(SIM_TARGET_HUB);
        } else {
            camera = new PhotonCamera("photonvision");
            simCamera = null;
            simVisionSystem = null;

            PortForwarder.add(5800, "photonvision.local", 5800);
            PortForwarder.add(1182, "photonvision.local", 1182);
            PortForwarder.add(1181, "photonvision.local", 1181);
        }
    }

    public static PhotonVisionModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhotonVisionModule();
        }
        return INSTANCE;
    }

    /**
     * Check whether the camera detected a target.
     *
     * @return whether we have a target.
     */
    public boolean hasTarget() {
        return camera.getLatestResult().hasTargets();
    }

    /**
     * Gets the distance of the robot from the target.
     *
     * @return the distance of the vision module from the target. [m]
     */
    public double getDistance(double cameraHeight, double targetHeight) {
        var results = camera.getLatestResult();
        if (results.hasTargets()) {
            double distance = PhotonUtils.calculateDistanceToTargetMeters(
                    cameraHeight,
                    targetHeight,
                    Math.toRadians(CAMERA_PITCH),
                    Math.toRadians(results.getBestTarget().getPitch())
            );

            return filter.calculate(distance) + TARGET_RADIUS;
        }
        return 0;
    }

    /**
     * Gets the distance of the robot from the target.
     *
     * @return the distance of the vision module from the target. [m]
     */
    public double getDistance() {
        return getDistance(CAMERA_HEIGHT, TARGET_HEIGHT_FROM_GROUND);
    }

    public OptionalDouble getYaw() {
        var results = camera.getLatestResult();
        if (results.hasTargets()) {
            return OptionalDouble.of(results.getBestTarget().getYaw());
        }
        return OptionalDouble.empty();
    }

    /**
     * Estimates the camera translation relative to the target.
     *
     * @return the translation relative to the target.
     */
    public Optional<Translation2d> estimateCameraTranslationToTarget() {
        PhotonPipelineResult results = Robot.isSimulation() ? simCamera.getLatestResult() : camera.getLatestResult();
        if (results.hasTargets()) {
            double distance = PhotonUtils.calculateDistanceToTargetMeters(
                    CAMERA_HEIGHT,
                    TARGET_HEIGHT_FROM_GROUND,
                    Math.toRadians(CAMERA_PITCH),
                    Math.toRadians(results.getBestTarget().getPitch())
            );
            return Optional.of(PhotonUtils.estimateCameraToTargetTranslation(distance, Rotation2d.fromDegrees(-results.getBestTarget().getYaw())));
        }
        return Optional.empty();
    }

    public VisionEstimationData estimatePose() {
        double navxAngle = Robot.getAngle().getDegrees();
        var results = camera.getLatestResult();
        if (results.hasTargets()) {
            double yawOffset = results.getBestTarget().getYaw();
            double d = PhotonUtils.calculateDistanceToTargetMeters(
                    CAMERA_HEIGHT,
                    TARGET_HEIGHT_FROM_GROUND,
                    Math.toRadians(CAMERA_PITCH),
                    Math.toRadians(results.getBestTarget().getPitch())
            );

            double relativeAngle = navxAngle - yawOffset + 180;
            relativeAngle = (relativeAngle < 0) ? 360 + relativeAngle : relativeAngle;
            double y = d * Math.sin(Math.toRadians(relativeAngle));
            double x = d * Math.cos(Math.toRadians(relativeAngle));

            double imageCaptureTime = Timer.getFPGATimestamp() - results.getLatencyMillis() / 1000.0;
            return new VisionEstimationData(true, HUB_POSE.plus(new Transform2d(
                    new Translation2d(x, y),
                    new Rotation2d(x, y)
            )), imageCaptureTime);
        }
        return new VisionEstimationData(false, null, 0);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("Has target", hasTarget());
        SmartDashboard.putNumber("Yaw", getYaw().orElse(0));
        SmartDashboard.putNumber("Distance", getDistance());
        FireLog.log("Yaw", getYaw().orElse(0));
        FireLog.log("Distance", getDistance());
    }
}