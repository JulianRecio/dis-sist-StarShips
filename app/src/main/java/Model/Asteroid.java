package Model;

import Model.Enums.EntityType;

public class Asteroid implements Entity{

    private Long id;

    EntityType type;

    private int integrity;

    public Asteroid(int integrity) {
        this.type = EntityType.ASTEROID;
        this.integrity = integrity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return type;
    }

    public int getIntegrity() {
        return integrity;
    }

    public void setIntegrity(int integrity) {
        this.integrity = integrity;
    }
}
