package frc.robot.subsystems.aprilTags;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.util.net.PortForwarder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.SimPhotonCamera;
import org.photonvision.SimVisionSystem;

public class AprilTags extends SubsystemBase {
   private final PhotonCamera camera;
   private final SimPhotonCamera simCamera;
   private final SimVisionSystem simVisionSystem;
   private final LinearFilter filter = LinearFilter.movingAverage(5);

   public AprilTags(String cameraName){
        camera = new PhotonCamera(cameraName);
        simCamera = null;
        simVisionSystem = null;

       PortForwarder.add(5800, "photonvision.local", 5800);
   }








}
