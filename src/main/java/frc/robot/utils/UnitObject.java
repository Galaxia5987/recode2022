package frc.robot.utils;

import java.util.HashMap;
import java.util.Map;

public class UnitObject {
    public final Units.Types unitType;
    public final double value;
    public final double ticksPerRotation;
    public double metersPerRotation;

    public UnitObject(Units.Types unitType, double value, double ticksPerRotation) {
        this.unitType = unitType;
        this.value = value;
        this.ticksPerRotation = ticksPerRotation;
    }

    public void setMetersPerRotation(double metersPerRotation) {
        this.metersPerRotation = metersPerRotation;
    }

    public int compareTo(UnitObject other) {
        return (int) Math.signum(this.getRps() - other.getRps());
    }

    public UnitObject getLarger(UnitObject other) {
        int larger = compareTo(other);
        if (larger < 0) {
            return other;
        }
        return this;
    }

    public Map<Units.Types, Double> getAllUnitValues(double metersPerRotation) {
        Map<Units.Types, Double> map = getAllUnitValues();
        map.put(Units.Types.METERS_PER_SECOND, getMetersPerSecond(metersPerRotation));
        return map;
    }

    public Map<Units.Types, Double> getAllUnitValues() {
        Map<Units.Types, Double> map = new HashMap<>();
        map.put(Units.Types.TICKS_PER_100MS, getTicksPer100ms());
        map.put(Units.Types.RPS, getRps());
        map.put(Units.Types.RPM, getRpm());
        return map;
    }

    public double getMetersPerSecond(double metersPerRotation) {
        this.metersPerRotation = metersPerRotation;
        switch (unitType) {
            case METERS_PER_SECOND:
                return value;
            case TICKS_PER_100MS:
                return value / ticksPerRotation * metersPerRotation * 10;
            case RPS:
                return value * metersPerRotation;
            case RPM:
                return Units.rpsToRpm(value * metersPerRotation);
        }
        return 0;
    }

    public double getTicksPer100ms() {
        switch (unitType) {
            case TICKS_PER_100MS:
                return value;
            case RPS:
                return (value / ticksPerRotation) * 10;
            case RPM:
                return Units.rpsToRpm((value / ticksPerRotation) * 10);
        }
        return Double.POSITIVE_INFINITY;
    }

    public double getTicksPer100ms(double metersPerRotation) {
        double ticksPer100ms = getTicksPer100ms();
        if (ticksPer100ms == Double.POSITIVE_INFINITY) {
            return (value / metersPerRotation) * ticksPerRotation / 10;
        }
        return ticksPer100ms;
    }

    public double getRps() {
        switch (unitType) {
            case TICKS_PER_100MS:
                return value / ticksPerRotation * 10;
            case RPS:
                return value;
            case RPM:
                return Units.rpmToRps(value);
        }
        return Double.POSITIVE_INFINITY;
    }

    public double getRps(double metersPerRotation) {
        double rps = getRps();
        if (rps == Double.POSITIVE_INFINITY) {
            return value / metersPerRotation;
        }
        return rps;
    }

    public double getRpm() {
        switch (unitType) {
            case TICKS_PER_100MS:
                return Units.rpsToRpm(value / ticksPerRotation * 10);
            case RPS:
                return Units.rpsToRpm(value);
            case RPM:
                return value;
        }
        return Double.POSITIVE_INFINITY;
    }

    public double getRpm(double metersPerRotation) {
        double rpm = getRpm();
        if (rpm == Double.POSITIVE_INFINITY) {
            return Units.rpsToRpm(value / metersPerRotation);
        }
        return rpm;
    }

    public double getDirection() {
        return Math.signum(getRps());
    }
}
