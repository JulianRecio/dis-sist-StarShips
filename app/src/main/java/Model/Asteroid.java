package Model;

import Model.Enums.EntityType;

public class Asteroid extends Entity{

    private int integrity;

    public Asteroid(String id, int integrity) {
        super(id, EntityType.ASTEROID);
        this.integrity = integrity;
    }

    public int getIntegrity() {
        return integrity;
    }

    public void setIntegrity(int integrity) {
        this.integrity = integrity;
    }
}
