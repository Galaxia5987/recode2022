package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class TalonFXFactory extends TalonFactory {
    private static TalonFXFactory INSTANCE = null;

    public TalonFXFactory() {
        super();
    }

    public static TalonFXFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TalonFXFactory();
        }
        return INSTANCE;
    }

    public WPI_TalonFX createSimpleTalonFX(int id, TalonFXInvertType inversion, NeutralMode neutralMode) {
        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.setInverted(inversion);
        talon.setNeutralMode(neutralMode);
        return talon;
    }

    public WPI_TalonFX createSimpleTalonFX(int id, boolean inversion, NeutralMode neutralMode) {
        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.setInverted(inversion);
        talon.setNeutralMode(neutralMode);
        return talon;
    }

    public WPI_TalonFX createDefaultPIDTalonFX(int id, int timeout, PIDConstants pidConstants, TalonFXInvertType inversion, NeutralMode neutralMode) {
        if (Utils.deadband(id, 63) != 0) {
            try {
                errorLogWriter.write("Invalid id - port : " + id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        WPI_TalonFX talon = new WPI_TalonFX(id);
        handleConfig(id,
                talon.configFactoryDefault(),
                talon.config_kP(0, pidConstants.kP, timeout),
                talon.config_kI(0, pidConstants.kP, timeout),
                talon.config_kD(0, pidConstants.kP, timeout),
                talon.config_kF(0, pidConstants.kP, timeout)
        );
        talon.setNeutralMode(neutralMode);

        if (pidConstants.kIZone.isPresent() && pidConstants.maxIntegralAccumulator.isPresent()) {
            handleConfig(id,
                    talon.config_IntegralZone(0, pidConstants.kIZone.getAsDouble(), timeout),
                    talon.configMaxIntegralAccumulator(0, pidConstants.maxIntegralAccumulator.getAsDouble(), timeout)
            );
        }

        talon.setInverted(inversion);
        return talon;
    }

    public WPI_TalonFX createDefaultSlaveTalonFX(TalonFX master, int id, boolean opposingMaster, NeutralMode neutralMode) {
        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.follow(master);
        if (opposingMaster) {
            talon.setInverted(TalonFXInvertType.OpposeMaster);
        } else {
            talon.setInverted(TalonFXInvertType.FollowMaster);
        }
        talon.setNeutralMode(neutralMode);
        return talon;
    }
}
