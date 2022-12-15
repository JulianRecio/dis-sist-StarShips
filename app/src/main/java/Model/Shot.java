package Model;

import Model.Enums.EntityType;
import Model.Enums.ShotType;

public class Shot extends Entity{

    private ShotType shotType;

    public Shot(String id, ShotType shotType) {
        super(id, EntityType.SHOT);
        this.shotType = shotType;
    }

    public ShotType getShotType() {
        return shotType;
    }
}
