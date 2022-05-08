package frc.robot.autonomous.builder;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Set;

public interface BuilderUtil {

    static String getJsonFromPathName(String pathName) {
        return "deploy/pathplanner/generatedJSON/" + pathName + ".wpilib.json";
    }

    Set<Subsystem> defaultRequirements();
}
