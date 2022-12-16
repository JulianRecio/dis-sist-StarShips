package starShips.model.Entities;

import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Position;

public abstract class Entity {

    private String id;

    private EntityType type;

    private Position entityPosition;

    private double rotation;

    private double speed;

    private double height;

    private double width;

    private double trajectory;

    private HitBoxType hitBoxType;

    public Entity(String id, EntityType type, Position entityPosition, double rotation, double speed,double height, double width, double trajectory, HitBoxType hitBoxType) {
        this.id = id;
        this.type = type;
        this.entityPosition = entityPosition;
        this.rotation = rotation;
        this.speed = speed;
        this.height = height;
        this.width = width;
        this.trajectory = trajectory;
        this.hitBoxType = hitBoxType;
    }

    public Entity(String id, EntityType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public Position getEntityPosition() {
        return entityPosition;
    }

    public double getRotation() {
        return rotation;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getTrajectory() {
        return trajectory;
    }

    public HitBoxType getHitBoxType() {
        return hitBoxType;
    }

    public boolean isInsideBounds(){
        return entityPosition.getX() > 0 && entityPosition.getX() < 800 && entityPosition.getY() > 0 && entityPosition.getY() < 800;
    }

    public boolean isInsideBounds(double x,double y){
        return x > 0 && x < 725 && y > 0 && y < 700;
    }

    public abstract Entity update();

    public abstract Entity getNewEntity();
}
