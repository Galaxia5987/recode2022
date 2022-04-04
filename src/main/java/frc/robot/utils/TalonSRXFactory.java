package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonSRXFactory extends TalonFactory {
    private static TalonSRXFactory INSTANCE = null;

    public TalonSRXFactory() {
        super();
    }

    public static TalonSRXFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TalonSRXFactory();
        }
        return INSTANCE;
    }

    public WPI_TalonSRX createSimpleTalonFX(int id, InvertType inversion) {
        WPI_TalonSRX talon = new WPI_TalonSRX(id);
        talon.setInverted(inversion);
        return talon;
    }

    public WPI_TalonSRX createDefaultPIDTalonFX(int id, int timeout, PIDConstants pidConstants, InvertType inversion) {
        if (Utils.deadband(id, 63) != 0) {
            try {
                errorLogWriter.write("Invalid id - port : " + id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        WPI_TalonSRX talon = new WPI_TalonSRX(id);
        handleConfig(id,
                talon.configFactoryDefault(),
                talon.config_kP(0, pidConstants.kP, timeout),
                talon.config_kI(0, pidConstants.kP, timeout),
                talon.config_kD(0, pidConstants.kP, timeout),
                talon.config_kF(0, pidConstants.kP, timeout)
        );

        if (pidConstants.kIZone.isPresent() && pidConstants.maxIntegralAccumulator.isPresent()) {
            handleConfig(id,
                    talon.config_IntegralZone(0, pidConstants.kIZone.getAsDouble(), timeout),
                    talon.configMaxIntegralAccumulator(0, pidConstants.maxIntegralAccumulator.getAsDouble(), timeout)
            );
        }

        talon.setInverted(inversion);
        return talon;
    }

    public WPI_TalonSRX createDefaultPIDTalonFX(int id, int timeout, PIDConstants pidConstants, boolean inversion) {
        if (Utils.deadband(id, 63) != 0) {
            try {
                errorLogWriter.write("Invalid id - port : " + id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        WPI_TalonSRX talon = new WPI_TalonSRX(id);
        handleConfig(id,
                talon.configFactoryDefault(),
                talon.config_kP(0, pidConstants.kP, timeout),
                talon.config_kI(0, pidConstants.kP, timeout),
                talon.config_kD(0, pidConstants.kP, timeout),
                talon.config_kF(0, pidConstants.kP, timeout)
        );

        if (pidConstants.kIZone.isPresent() && pidConstants.maxIntegralAccumulator.isPresent()) {
            handleConfig(id,
                    talon.config_IntegralZone(0, pidConstants.kIZone.getAsDouble(), timeout),
                    talon.configMaxIntegralAccumulator(0, pidConstants.maxIntegralAccumulator.getAsDouble(), timeout)
            );
        }

        talon.setInverted(inversion);
        return talon;
    }

    public WPI_TalonSRX createDefaultSlaveTalonFX(TalonFX master, int id, boolean opposingMaster) {
        WPI_TalonSRX talon = new WPI_TalonSRX(id);
        talon.follow(master);
        if (opposingMaster) {
            talon.setInverted(InvertType.OpposeMaster);
        } else {
            talon.setInverted(InvertType.FollowMaster);
        }
        return talon;
    }
}
