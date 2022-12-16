package starShips.model.Entities;

import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Position;

public class Ship extends Entity {

    private final Weapon weapon;
    private final String playerId;
    private final boolean isAccelerating;

    public Ship(String id, Position entityPosition, double rotation, double speed, double height, double width, double trajectory, Weapon weapon, String playerId, boolean isAccelerating) {
        super(id, EntityType.SHIP, entityPosition, rotation, speed, height, width, trajectory, HitBoxType.TRIANGULAR);
        this.weapon = weapon;
        this.playerId = playerId;
        this.isAccelerating = isAccelerating;
    }

    @Override
    public Ship update() {
        if (getSpeed() > 0){
            double nextX =  getEntityPosition().getX() - 3.5 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
            double nextY =  getEntityPosition().getY() + 3.5 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
            if (!isInsideBounds(nextX, nextY)){
                return new Ship(getId(), getEntityPosition(), getRotation(),0, getHeight(), getWidth(), getTrajectory(),  getWeapon(), playerId, isAccelerating());
            }else{
                return new Ship(getId(), new Position(nextX, nextY), getRotation(),getSpeed() - 5, getHeight(), getWidth(), getTrajectory(),  getWeapon(), playerId, isAccelerating());
            }
        }
        return getNewEntity();
    }

    @Override
    public Ship getNewEntity() {
        return new Ship(getId(), getEntityPosition(), getRotation(),getSpeed(), getHeight(), getWidth(), getTrajectory(),  getWeapon(), playerId, isAccelerating());
    }

    public Ship move(boolean moveForward){
        if (moveForward){
            return accelerate();
        }else return decelerate();
    }

    private Ship decelerate() {
        if (getSpeed() > 0) {
            return new Ship(getId(), getEntityPosition(), getRotation(), getSpeed() - 170, getHeight(), getWidth(), getTrajectory(),  getWeapon(), playerId, isAccelerating());
        }else return getNewEntity();
    }

    private Ship accelerate() {
        if (getSpeed() < 1000) {
            double currentSpeed = getSpeed() + 70;
            return new Ship(getId(), getNextPosition(getEntityPosition(),currentSpeed) , getRotation(), currentSpeed, getHeight(), getWidth(), getTrajectory(),  getWeapon(), playerId, isAccelerating());
        }else return getNewEntity();
    }

    private Position getNextPosition(Position entityPosition, double currentSpeed) {
        double newX = 0;
        double newY = 0;
        double distance = currentSpeed * (0.01);
        if (getRotation() >= 90 && getRotation() < 180 ){
            newX = entityPosition.getX() - (distance);
            newY = entityPosition.getY() - (distance);
        }
        if (getRotation() >= 0 && getRotation() < 90 ){
            newX = entityPosition.getX() - (distance);
            newY = entityPosition.getY() + (distance);
        }
        if (getRotation() >= 270 && getRotation() < 360 ){
            newX = entityPosition.getX() + (distance);
            newY = entityPosition.getY() + (distance);
        }
        if (getRotation() >= 180 && getRotation() < 270 ){
            newX = entityPosition.getX() + (distance);
            newY = entityPosition.getY() - (distance);
        }

        return new Position(newX,newY);
    }

    public Ship turn(double turnDegrees){
        return new Ship(getId(), getEntityPosition(), getRotation() + turnDegrees, getSpeed(), getHeight(), getWidth(), getTrajectory(),  getWeapon(), playerId, isAccelerating());
    }

    public Ship useWeapon(){
        return new Ship(getId(), getEntityPosition(), getRotation(), getSpeed(), getHeight(), getWidth(), getTrajectory(),  new Weapon(getWeapon().getShotType(), System.currentTimeMillis()), playerId, isAccelerating());
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
