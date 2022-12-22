package starShips.model.Entities;

import starShips.model.Enums.Color;
import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;

public class Asteroid extends Entity {

    private final int integrity;
    private final boolean rotatesClockwise;

    public Asteroid(String id, double x, double y, double rotation, double height, double width, double trajectory, int integrity, boolean rotatesClockwise) {
        super(id, EntityType.ASTEROID, HitBoxType.ELLIPTICAL, x, y, rotation, height, width, trajectory, Color.RED);
        this.integrity = integrity;
        this.rotatesClockwise = rotatesClockwise;
    }

    @Override
    public Asteroid update() {
        if (outOfBound(3,3)){
            return null;
        }else{
            return continueOverTrajectory();
        }
    }

    @Override
    public Asteroid getNewEntity() {
        return new Asteroid(getId(), getX(), getY(),getRotation(),getHeight(),getWidth(),
                getTrajectory(), getIntegrity(),rotatesClockwise);

    }

    private Asteroid continueOverTrajectory() {
        double nextX =  getX() + 0.7 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
        double nextY =  getY() + 0.7 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
        double newRotation;
        if (rotatesClockwise) {
            newRotation = getRotation() + 2;
        } else {
            newRotation = getRotation() - 2;
        }
        return new Asteroid(getId(), nextX, nextY, newRotation,getHeight(),getWidth(),
                getTrajectory(), getIntegrity(),rotatesClockwise);
    }

    private boolean outOfBound(double x, double y) {
        return !isInsideBounds(getX() + x,getY() + y);
    }

    public int getPoints() {
        return (int) ((int) getHeight() * getWidth());
    }

    public int getIntegrity() {
        return integrity;
    }

    public boolean isRotatesClockwise() {
        return rotatesClockwise;
    }
}
