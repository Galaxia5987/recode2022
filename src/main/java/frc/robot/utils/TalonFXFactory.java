package frc.robot.utils;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;

public class TalonFXFactory {
    private static TalonFXFactory INSTANCE = null;
    private static FileWriter errorLogWriter;

    private TalonFXFactory() {
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

    public static TalonFXFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TalonFXFactory();
        }
        return INSTANCE;
    }

    public void handleConfig(ErrorCode errorCode, int id) {
        try {
            if (errorCode.equals(ErrorCode.OK)) {
                return;
            }
            errorLogWriter.write(errorCode.name() + " - port : " + id + " - timestamp : " + Timer.getFPGATimestamp());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public WPI_TalonFX createSimpleTalon(int id, TalonFXInvertType inversion) {
        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.setInverted(inversion);
        return talon;
    }

    public WPI_TalonFX createDefaultPIDTalon(int id, int timeout, PIDConstants pidConstants, TalonFXInvertType inversion) {
        if (Utils.deadband(id, 63) != 0) {
            try {
                errorLogWriter.write("Invalid id - port : " + id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        WPI_TalonFX talon = new WPI_TalonFX(id);
        handleConfig(talon.configFactoryDefault(), id);
        handleConfig(talon.config_kP(0, pidConstants.kP, timeout), id);
        handleConfig(talon.config_kP(0, pidConstants.kI, timeout), id);
        handleConfig(talon.config_kP(0, pidConstants.kD, timeout), id);
        handleConfig(talon.config_kP(0, pidConstants.kF, timeout), id);

        if (pidConstants.kIZone.isPresent() && pidConstants.maxIntegralAccumulator.isPresent()) {
            handleConfig(talon.config_IntegralZone(
                    0, pidConstants.kIZone.getAsDouble(), timeout), id);
            handleConfig(talon.configMaxIntegralAccumulator(
                    0, pidConstants.maxIntegralAccumulator.getAsDouble(), timeout), id);
        }

        talon.setInverted(inversion);
        return talon;
    }

    public WPI_TalonFX createDefaultSlaveTalon(TalonFX master, int id, boolean opposingMaster) {
        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.follow(master);
        if (opposingMaster) {
            talon.setInverted(TalonFXInvertType.OpposeMaster);
        } else {
            talon.setInverted(TalonFXInvertType.FollowMaster);
        }
        return talon;
    }
}
