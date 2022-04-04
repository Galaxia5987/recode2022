package frc.robot.utils;

import com.ctre.phoenix.ErrorCode;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;

public class TalonFactory {
    protected FileWriter errorLogWriter;
    private static TalonFactory INSTANCE = null;

    public TalonFactory() {
        try {
            File errorLog = new File("talonErrorLog.txt");
            if (errorLog.createNewFile()) {
                System.out.println("File already exists.");
            }
            errorLogWriter = new FileWriter(errorLog.getName());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static TalonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TalonFactory();
        }
        return INSTANCE;
    }

    public void handleConfig(int id, ErrorCode... errorCodes) {
        try {
            for (ErrorCode error : errorCodes) {
                errorLogWriter.write(error.name() + " - port : " + id + " - timestamp : " + Timer.getFPGATimestamp());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
