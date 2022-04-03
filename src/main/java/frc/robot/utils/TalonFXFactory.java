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

    public TalonFX createSimpleTalon(int id, TalonFXInvertType inversion) {
        TalonFX talon = new TalonFX(id);
        talon.setInverted(inversion);
        return talon;
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
        public final double kP;
        public final double kI;
        public final double kD;
        public final double kF;

        public final OptionalDouble kIZone;
        public final OptionalDouble maxIntegralAccumulator;

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
