package starShips.model.Entities;

import starShips.model.Enums.Color;
import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Enums.ShotType;

public class Ship extends Entity {

    private final String playerId;
    private final ShotType shotType;
    private final double speed;
    private final long previousShot;

    public Ship(String id, double x, double y, double rotation, double height, double width,
                double trajectory, Color color, String playerId, ShotType shotType, double speed,
                long previousShot) {
        super(id, EntityType.SHIP,HitBoxType.TRIANGULAR, x, y, rotation, height, width, trajectory,
                 color);
        this.playerId = playerId;
        this.shotType = shotType;
        this.speed = speed;
        this.previousShot = previousShot;
    }

    public Ship move(boolean forward){
        if (forward){
            return accelerate();
        }
        else return decelerate();
    }

    private Ship accelerate() {
        if (speed < 1000){
            return new Ship(getId(), getX(), getY(), getRotation(), getHeight(), getWidth(), getTrajectory(), getColor(), getPlayerId(), getShotType(), speed + 70, getPreviousShot());
        }
        return getNewEntity();
    }

    private Ship decelerate() {
        if (speed > 0){
            return new Ship(getId(), getX(), getY(), getRotation(), getHeight(), getWidth(), getTrajectory(), getColor(), getPlayerId(), getShotType(), speed - 170, getPreviousShot());
        }
        return getNewEntity();
    }

    public Ship turn(double rotation) {
        return new Ship(getId(), getX(), getY(), getRotation() + rotation, getHeight(), getWidth(), getTrajectory() + rotation, getColor(), getPlayerId(), getShotType(), speed, getPreviousShot());
    }

    public Ship shoot(){
        return new Ship(getId(), getX(), getY(), getRotation(), getHeight(), getWidth(), getTrajectory(), getColor(), getPlayerId(), getShotType(), getSpeed(), System.currentTimeMillis());
    }

    public boolean canShoot(){
        return System.currentTimeMillis() - previousShot > 250;
    }

    @Override
    public Ship update() {
        if (speed > 0){
            double nextX =  getX() - 3.5 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
            double nextY =  getY() + 3.5 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
            if (!isInsideBounds(nextX, nextY)){
                return new Ship(getId(), getX(), getY(), getRotation(), getHeight(), getWidth(), getTrajectory(),  getColor(), getPlayerId(), getShotType(),0, getPreviousShot());
            }else{
                return new Ship(getId(), nextX, nextY, getRotation(), getHeight(), getWidth(), getTrajectory(),  getColor(), getPlayerId(), getShotType(),speed - 5, getPreviousShot());
            }
        }
        return (Ship) getNewEntity();
    }

    @Override
    public Ship getNewEntity() {
        return new Ship(getId(), getX(), getY(), getRotation(), getHeight(), getWidth(), getTrajectory(),  getColor(), getPlayerId(), getShotType(),speed, getPreviousShot());
    }

    public String getPlayerId() {
        return playerId;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public double getSpeed() {
        return speed;
    }

    public long getPreviousShot() {
        return previousShot;
    }
}
