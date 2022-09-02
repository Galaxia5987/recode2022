package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;

import java.util.ArrayList;

public class TestVision {

    public static void main(String[] args) {
        ArrayList<Double> angleTests = new ArrayList<>() {{
            add(45.0);
            add(-90.0);
            add(25.0);
            add(-87.0);
        }};
        ArrayList<Double> distanceTests = new ArrayList<>() {{
            add(3.0);
            add(1.5);
            add(1.0);
            add(2.45);
        }};
        ArrayList<Double> robotAngles = new ArrayList<>() {{
            add(0.0);
            add(60.0);
            add(-120.0);
            add(210.65);
        }};
        ArrayList<Translation2d> expectedResult = new ArrayList<>() {{
            add(new Translation2d(8.23 - 3.0 / Math.sqrt(2), 4.115 - 3.0 / Math.sqrt(2)));
            add(new Translation2d(8.23 - 1.5 * Math.cos(Math.toRadians(30)), 4.115 + 1.5 * Math.sin(Math.toRadians(30))));
            add(new Translation2d(8.23 - Math.cos(Math.toRadians(-95)), 4.115 - Math.sin(Math.toRadians(-95))));
            add(new Translation2d(8.23 - 2.45 * Math.cos(Math.toRadians(123.65)), 4.115 - 2.45 * Math.sin(Math.toRadians(123.65))));
        }};

        for (int i = 0; i < expectedResult.size(); i++) {
            double yaw = angleTests.get(i);
            double distance = distanceTests.get(i);

            double absoluteAngle = Math.toRadians(yaw + robotAngles.get(i));
            double dy = Math.sin(absoluteAngle) * distance;
            double dx = Math.cos(absoluteAngle) * distance;

            System.out.println(new Pose2d(dx, dy, Rotation2d.fromDegrees(Math.toDegrees(absoluteAngle))));

            Translation2d translation = Constants.Vision.HUB_POSE
                    .getTranslation().minus(new Translation2d(dx, dy));

            System.out.println("Output: " + translation.toString());
            System.out.println("Expected: " + expectedResult.get(i).toString() + "\n");
        }
    }
}
