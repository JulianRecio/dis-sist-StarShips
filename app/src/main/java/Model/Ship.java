package Model;

import Model.Enums.EntityType;

public class Ship implements Entity{

    private Long id;

    EntityType type;

    Weapon weapon;

    public Ship(Weapon weapon) {
        this.type = EntityType.SHIP;
        this.weapon = weapon;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return type;
    }

    public Shot useWeapon(){
        return weapon.shoot();
    }
}
