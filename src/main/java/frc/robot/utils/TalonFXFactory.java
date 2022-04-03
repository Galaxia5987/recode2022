package frc.robot.utils;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;
import java.util.OptionalDouble;

public class TalonFXFactory {
    private static TalonFXFactory INSTANCE = null;
    private static FileWriter errorLogWriter;
    private static FileWriter exceptionLogWriter;

    private TalonFXFactory() {
        try {
            File errorLog = new File("C:/Users/eitan/Galaxia/talonErrorLog.txt");
            File exceptionLog = new File("C:/Users/eitan/Galaxia/talonExceptionLog.txt");
            if (errorLog.createNewFile() || exceptionLog.createNewFile()) {
                System.out.println("File already exists.");
            }
            errorLogWriter = new FileWriter(errorLog.getName());
            exceptionLogWriter = new FileWriter(exceptionLog.getName());
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

    private static void handleConfig(ErrorCode errorCode, int id) {
        try {
            if (errorCode.equals(ErrorCode.OK)) {
                return;
            }
            errorLogWriter.write(errorCode.name() + " - port : " + id + " - timestamp : " + Timer.getFPGATimestamp());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public TalonFX createDefaultPIDTalon(int id, int timeout, PIDConstants pidConstants, TalonFXInvertType inversion) {
        if (Utils.deadband(id, 63) != 0) {
            try {
                errorLogWriter.write("Invalid id - port : " + id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        TalonFX talon = new TalonFX(id);
        handleConfig(talon.configFactoryDefault(), id);
        handleConfig(talon.config_kP(0, pidConstants.kP), id);
        handleConfig(talon.config_kP(0, pidConstants.kI), id);
        handleConfig(talon.config_kP(0, pidConstants.kD), id);
        handleConfig(talon.config_kP(0, pidConstants.kF), id);

        if (pidConstants.kIZone.isPresent() && pidConstants.maxIntegralAccumulator.isPresent()) {
            handleConfig(talon.config_IntegralZone(
                    0, pidConstants.kIZone.getAsDouble(), timeout), id);
            handleConfig(talon.configMaxIntegralAccumulator(
                    0, pidConstants.maxIntegralAccumulator.getAsDouble(), timeout), id);
        }
        talon.setInverted(inversion);
        return talon;
    }

    public TalonFX createDefaultSlaveTalon(TalonFX master, int id, boolean opposingMaster) {
        TalonFX talon = new TalonFX(id);
        talon.follow(master);
        if (opposingMaster) {
            talon.setInverted(TalonFXInvertType.OpposeMaster);
        } else {
            talon.setInverted(TalonFXInvertType.FollowMaster);
        }
        return talon;
    }

    public static class PIDConstants {
        public double kP;
        public double kI;
        public double kD;
        public double kF;

        public OptionalDouble kIZone;
        public OptionalDouble maxIntegralAccumulator;

        public PIDConstants(double kP, double kI, double kD, double kF) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.kF = kF;

            this.kIZone = OptionalDouble.empty();
            this.maxIntegralAccumulator = OptionalDouble.empty();
        }

        public PIDConstants(double kP, double kI, double kD, double kF, double kIZone, double maxIntegralAccumulator) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.kF = kF;

            this.kIZone = OptionalDouble.of(kIZone);
            this.maxIntegralAccumulator = OptionalDouble.of(maxIntegralAccumulator);
        }
    }
}
