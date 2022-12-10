package Model;

import Model.Enums.EntityType;
import Model.Enums.ShotType;

public class Shot implements Entity{

    private Long id;

    private EntityType type;

    private ShotType shotType;

    public Shot(ShotType shotType) {
        this.type = EntityType.SHOT;
        this.shotType = shotType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return type;
    }

    public ShotType getShotType() {
        return shotType;
    }
}
