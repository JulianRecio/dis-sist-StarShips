package starShips.model.Entities;

import starShips.model.Enums.ShotType;

public class Weapon {

    private final ShotType shotType;

    private final long previousShot;

    public Weapon(ShotType shotType, long previousShot) {
        this.shotType = shotType;
        this.previousShot = previousShot;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public boolean isLoaded() {
        if (shotType == ShotType.LASER) return System.currentTimeMillis() - previousShot > 600;
        else return System.currentTimeMillis() - previousShot > 300;
    }

    public long getPreviousShot() {
        return previousShot;
    }
}
