package Model;

import Model.Enums.EntityType;
import Model.Enums.HitBoxType;
import Movement.Position;

public abstract class Entity {

    private String id;

    private EntityType type;

    private Position entityPosition;

    private double rotation;

    private double speed;

    private HitBoxType hitBoxType;

    public Entity(String id, EntityType type, Position entityPosition, double rotation, double speed, HitBoxType hitBoxType) {
        this.id = id;
        this.type = type;
        this.entityPosition = entityPosition;
        this.rotation = rotation;
        this.speed = speed;
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

    public void setEntityPosition(Position entityPosition) {
        this.entityPosition = entityPosition;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public HitBoxType getHitBoxType() {
        return hitBoxType;
    }
}
