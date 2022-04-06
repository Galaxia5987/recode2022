package frc.robot.utils;

import com.ctre.phoenix.ErrorCode;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;

public class TalonFactory {
    private static TalonFactory INSTANCE = null;
    protected FileWriter errorLogWriter;

    public TalonFactory() {
    }

    public static TalonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TalonFactory();
        }
        return INSTANCE;
    }

    public void handleConfig(int id, ErrorCode... errorCodes) {
        try {
            initErrorLog(id);
            for (ErrorCode error : errorCodes) {
                errorLogWriter.write(error.name() + " - timestamp : " + Timer.getFPGATimestamp());
            }
            errorLogWriter.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void initErrorLog(int id) {
        try {
            File errorLog = new File("talonErrorLog" + id + ".txt");
            if (!errorLog.createNewFile()) {
                System.out.println("File already exists.");
            }
            errorLogWriter = new FileWriter(errorLog.getName());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
