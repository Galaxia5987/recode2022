package frc.robot.utils;

import com.ctre.phoenix.ErrorCode;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;

public class TalonFactory {
    private static TalonFactory INSTANCE = null;
    protected FileWriter errorLogWriter;
    protected File errorLog;
    protected String errorLogOutput;

    protected TalonFactory() {
        errorLog = new File("talonErrorLog.txt");
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
                writeToFile(error.name() + " - timestamp : " + Timer.getFPGATimestamp() + " : port - " + id);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void writeToFile(String output) {
        errorLogOutput += output + "\n";

        try {
            errorLogWriter = new FileWriter(errorLog);
            errorLog.createNewFile();
            errorLogWriter.write(errorLogOutput);
            errorLogWriter.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
