package starShips.model.Entities;

import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Position;

public class Ship extends Entity {

    private final Weapon weapon;
    private final String playerId;
    private final boolean isAccelerating;

    public Ship(String id, EntityType type, Position entityPosition, double rotation, double speed, double height, double width, double trajectory, HitBoxType hitBoxType, Weapon weapon, String playerId, boolean isAccelerating) {
        super(id, type, entityPosition, rotation, speed, height, width, trajectory, hitBoxType);
        this.weapon = weapon;
        this.playerId = playerId;
        this.isAccelerating = isAccelerating;
    }

    @Override
    public Ship update() {
        if (isAccelerating){
            double nextX =  getEntityPosition().getX() + 0.7 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
            double nextY =  getEntityPosition().getY() + 0.7 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
            if (!isInsideBounds(nextX, nextY)){
                return new Ship(getId(), getType(), getEntityPosition(), getRotation(),0, getHeight(), getWidth(), getTrajectory(), getHitBoxType(), getWeapon(), playerId, isAccelerating());
            }else{
                return new Ship(getId(), getType(), new Position(nextX, nextY), getRotation(),getSpeed() - 5, getHeight(), getWidth(), getTrajectory(), getHitBoxType(), getWeapon(), playerId, isAccelerating());
            }
        }
        return getNewEntity();
    }

    @Override
    public Ship getNewEntity() {
        return new Ship(getId(), getType(), getEntityPosition(), getRotation(),getSpeed(), getHeight(), getWidth(), getTrajectory(), getHitBoxType(), getWeapon(), playerId, isAccelerating());
    }

    public Ship move(boolean moveForward){
        if (moveForward){
            return accelerate();
        }else return decelerate();
    }

    private Ship decelerate() {
        if (getSpeed() > 0) {
            return new Ship(getId(), getType(), getEntityPosition(), getRotation(), getSpeed() - 7, getHeight(), getWidth(), getTrajectory(), getHitBoxType(), getWeapon(), playerId, isAccelerating());
        }else return getNewEntity();
    }

    private Ship accelerate() {
        if (getSpeed() < 100) {
            return new Ship(getId(), getType(), getEntityPosition(), getRotation(), getSpeed() + 7, getHeight(), getWidth(), getTrajectory(), getHitBoxType(), getWeapon(), playerId, isAccelerating());
        }else return getNewEntity();
    }

    public Ship turn(double turnDegrees){
        return new Ship(getId(), getType(), getEntityPosition(), getRotation() + turnDegrees, getSpeed(), getHeight(), getWidth(), getTrajectory(), getHitBoxType(), getWeapon(), playerId, isAccelerating());
    }

    public Ship useWeapon(){
        return new Ship(getId(), getType(), getEntityPosition(), getRotation(), getSpeed(), getHeight(), getWidth(), getTrajectory(), getHitBoxType(), new Weapon(getWeapon().getShotType(), System.currentTimeMillis()), playerId, isAccelerating());
    }

    public boolean isAccelerating() {
        return isAccelerating;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getPlayerId() {
        return playerId;
    }
}
