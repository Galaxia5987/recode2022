package frc.robot.utils;

import java.util.OptionalDouble;

public class PIDConstants {
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