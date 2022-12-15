package Model;

import Model.Enums.EntityType;
import Model.Enums.HitBoxType;
import Movement.Position;

public class Ship extends Entity{

    private Weapon weapon;

    private boolean isAccelerating;

    public Ship(String id, Position entityPosition, double rotation, double speed, Weapon weapon, boolean isAccelerating, HitBoxType hitBoxType) {
        super(id, EntityType.SHIP, entityPosition, rotation, speed, hitBoxType);
        this.weapon = weapon;
        this.isAccelerating = isAccelerating;
    }

    public Shot useWeapon(){
        return weapon.shoot(super.getId());
    }

    public void setAccelerating(boolean accelerating) {
        isAccelerating = accelerating;
    }

    public boolean isAccelerating() {
        return isAccelerating;
    }

    public Ship accelerate(){
        if (!isAccelerating()){
            this.setAccelerating(true);
            this.setSpeed(0.5);
        }else if (this.getSpeed() < 10) {
            this.setSpeed(this.getSpeed() * 1.5);
        } else {
            this.setSpeed(10);
        }
        return new Ship(super.getId(),super.getEntityPosition(), super.getRotation(),super.getSpeed(),this.weapon,true, super.getHitBoxType());
    }

    public Ship rotateClockwise(){
        super.setRotation(super.getRotation() + 1);
        return new Ship(super.getId(),super.getEntityPosition(), super.getRotation(),super.getSpeed(),this.weapon,isAccelerating(), super.getHitBoxType());
    }

    public Ship rotateCounterClockwise(){
        super.setRotation(super.getRotation() - 1);
        return new Ship(super.getId(),super.getEntityPosition(), super.getRotation(),super.getSpeed(),this.weapon,isAccelerating(), super.getHitBoxType());
    }

    public Ship speedBreak(){
        if (super.getSpeed() < 0.1) {
            super.setSpeed(0);
            this.setAccelerating(false);
        }else{
            super.setSpeed(super.getSpeed() * 0.5);
        }
        return new Ship(super.getId(),super.getEntityPosition(), super.getRotation(),super.getSpeed(),this.weapon,isAccelerating(), super.getHitBoxType());
    }

    public Ship decelerate(){
        if (super.getSpeed() < 0.1) {
            super.setSpeed(0);
            this.setAccelerating(false);
        }else{
            super.setSpeed(super.getSpeed() * 0.75);
        }
        return new Ship(super.getId(),super.getEntityPosition(), super.getRotation(),super.getSpeed(),this.weapon,isAccelerating(), super.getHitBoxType());
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
