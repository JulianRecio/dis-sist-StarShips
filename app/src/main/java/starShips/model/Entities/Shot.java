package starShips.model.Entities;

import starShips.model.Enums.Color;
import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Enums.ShotType;

public class Shot extends Entity {

    private final String shooterId;
    private final ShotType shotType;
    private final int damageOutput;

    public Shot(String id, double x, double y, double rotation, double height, double width, double trajectory, Color color, String shooterId, ShotType shotType, int damageOutput) {
        super(id, EntityType.SHOT, HitBoxType.RECTANGULAR, x, y, rotation, height, width, trajectory, color);
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

    private Shot continueOverTrajectory() {
        double nextX =  getX() - 4 * Math.sin(Math.PI * 2 * getTrajectory() / 360);
        double nextY =  getY() + 4 * Math.cos(Math.PI * 2 * getTrajectory() / 360);
        return new Shot(getId(), nextX, nextY, getRotation(), getHeight(),getWidth(),getTrajectory(), getColor(),getShooterId(),getShotType(),getDamageOutput());
    }

    @Override
    public Shot update() {
        if (isInsideBounds()){
            return continueOverTrajectory();
        }
        else return null;
    }
    @Override
    public Entity getNewEntity() {
        return new Shot(getId(), getX(), getY(), getRotation(), getHeight(),getWidth(),
                getTrajectory(), getColor(),getShooterId(),getShotType(),getDamageOutput());
    }
}
