package darkchoco.bycomparison.favor_enum.solution;

import java.util.Objects;

class CruiseControl {

    private double targetSpeedKmh;

    void setPreset(SpeedPreset speedPreset) {
        Objects.requireNonNull(speedPreset);

        setTargetSpeedKmh(speedPreset.speedKmh);
    }

    void setTargetSpeedKmh(double speedKmh) {
        targetSpeedKmh = speedKmh;
    }
}

enum SpeedPreset {

    STOP(0), PLANETARY_SPEED(7667), CRUISE_SPEED(16944);

    final double speedKmh;

    SpeedPreset(double speedKmh) {
        this.speedKmh = speedKmh;
    }
}

class Main {

    static void usage() {
        CruiseControl cruiseControl = null;
        cruiseControl.setPreset(SpeedPreset.PLANETARY_SPEED);
        cruiseControl.setPreset(SpeedPreset.CRUISE_SPEED);
    }
}
