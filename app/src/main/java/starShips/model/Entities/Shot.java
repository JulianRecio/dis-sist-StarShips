package starShips.model.Entities;

import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Enums.ShotType;
import starShips.model.Position;

public class Shot extends Entity {

    private String shooterId;
    private ShotType shotType;
    private int damageOutput;

    public Shot(String id, Position entityPosition, double rotation, double speed, double height, double width, double trajectory, String shooterId, ShotType shotType, int damageOutput) {
        super(id, EntityType.SHOT, entityPosition, rotation, speed, height, width, trajectory, HitBoxType.ELLIPTICAL);
        this.shooterId = shooterId;
        this.shotType = shotType;
        this.damageOutput = damageOutput;
    }

    public String getShooterId() {
        return shooterId;
    }

    public int getDamageOutput() {
        return damageOutput;
    }

    public ShotType getShotType() {
        return shotType;
    }

    @Override
    public Entity update() {
        if (isInsideBounds()){
            return continueOverTrajectory();
        }else return null;
    }

    private Entity continueOverTrajectory() {
        double nextX =  getEntityPosition().getX() - 4 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
        double nextY =  getEntityPosition().getY() + 4 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
        return new Shot(getId(), new Position(nextX, nextY), getRotation(), getSpeed(), getHeight(),getWidth(),getTrajectory(),getShooterId(),getShotType(),getDamageOutput());
    }

    @Override
    public Entity getNewEntity() {
        return new Shot(getId(), getEntityPosition(), getRotation(), getSpeed(), getHeight(),getWidth(),getTrajectory(),getShooterId(),getShotType(),getDamageOutput());

    }
}
