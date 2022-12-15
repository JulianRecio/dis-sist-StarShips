package Model;

import Model.Enums.EntityType;
import Model.Enums.ShotType;

public class Weapon {

    ShotType shotType;

    public Weapon(ShotType shotType) {
        this.shotType = shotType;
    }

    public Shot shoot(String id){
        return new Shot(id,shotType);
    }

}
