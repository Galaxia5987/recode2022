package frc.robot.utils;

public class Units {
    /**
     * Converts the value from m/s to rps.
     *
     * @param value  the speed in m/s.
     * @param radius the radius of the wheel. [m]
     * @return the speed in rps.
     */
    public static double metersPerSecondToRps(double value, double radius) {
        return value / (2 * Math.PI * radius);
    }

    public static double rpsToRpm(double valueRps) {
        return valueRps * 60;
    }

    public static double rpmToRps(double valueRpm) {
        return valueRpm / 60.0;
    }

    public enum Types {
        METERS_PER_SECOND("m/s"),
        TICKS_PER_100MS("tick/100ms"),
        RPM("rpm"),
        RPS("rps"),
        NONE("None");

        public final String name;

        Types(String name) {
            this.name = name;
        }
    }
}
