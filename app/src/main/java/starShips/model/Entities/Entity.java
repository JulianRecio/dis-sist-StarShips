package starShips.model.Entities;

import starShips.model.Enums.Color;
import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;

public abstract class Entity {

    private  final String id;

    private  final EntityType type;

    private final double x;

    private final double y;

    private final double rotation;

    private final double height;

    private final double width;

    private final double trajectory;

    private final HitBoxType hitBoxType;

    private final Color color;

    public Entity(String id, EntityType type,HitBoxType hitBoxType,double x, double y, double rotation, double height, double width, double trajectory,  Color color) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.height = height;
        this.width = width;
        this.trajectory = trajectory;
        this.hitBoxType = hitBoxType;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
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
        return getX() > 0 && getX() < 800 && getY() > 0 && getY() < 800;
    }

    public boolean isInsideBounds(double x,double y){
        return x > 0 && x < 725 && y > 0 && y < 700;
    }

    public Color getColor() {
        return color;
    }

    public abstract Entity update();

    public abstract Entity getNewEntity();
}
