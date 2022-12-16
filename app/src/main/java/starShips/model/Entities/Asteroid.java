package starShips.model.Entities;

import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Position;

public class Asteroid extends Entity {

    private int integrity;

    private boolean rotatesClockwise;

    public Asteroid(String id, Position entityPosition, double rotation, double speed, double height, double width, int integrity, boolean rotatesClockwise, double trajectory) {
        super(id, EntityType.ASTEROID, entityPosition, rotation, speed, height, width, trajectory, HitBoxType.ELLIPTICAL);
        this.integrity = integrity;
        this.rotatesClockwise = rotatesClockwise;
    }


    public int getIntegrity() {
        return integrity;
    }

    public boolean isRotatesClockwise() {
        return rotatesClockwise;
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
        return new Asteroid(getId(), getEntityPosition(),getRotation(),getSpeed(),getHeight(),getWidth(),getIntegrity(),rotatesClockwise,getTrajectory());

    }

    private Asteroid continueOverTrajectory() {
        double nextX =  getEntityPosition().getX() + 0.7 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
        double nextY =  getEntityPosition().getY() + 0.7 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
        double newRotation;
        if (rotatesClockwise) {
            newRotation = getRotation() + 2;
        } else {
            newRotation = getRotation() - 2;
        }
        return new Asteroid(getId(), new Position(nextX, nextY),newRotation,getSpeed(),getHeight(),getWidth(),getIntegrity(),rotatesClockwise,getTrajectory());
    }

    private boolean outOfBound(double x, double y) {
        return !isInsideBounds(getEntityPosition().getX() + x,getEntityPosition().getY() + y);
    }

    public int getPoints() {
        return (int) ((int) getHeight() * getWidth());
    }
}
