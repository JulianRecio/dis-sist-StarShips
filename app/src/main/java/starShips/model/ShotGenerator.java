package starShips.model;

import starShips.model.Entities.Ship;
import starShips.model.Entities.Shot;

import java.util.Random;

import static starShips.model.Enums.ShotType.*;

public class ShotGenerator {
    private static int count = 0;

    public static Shot shoot(Ship ship){
        String id = "shot-" + ++count;
        Random r = new Random();
        double n =  2 + (5-2) * r.nextDouble();
        double[] values = getSpecificValues(ship, n);
        return new Shot(id,new Position(ship.getEntityPosition().getX()+16, ship.getEntityPosition().getY()), values[0], 0 ,values[1], values[2], ship.getRotation(), ship.getPlayerId(),ship.getWeapon().getShotType(), (int) (n*13));
    }
    private static double[] getSpecificValues(Ship ship, double n){

        return switch (ship.getWeapon().getShotType()){
            case SHOT -> new double[]{ship.getRotation()-20, n*12, n*4};
            case SPREAD -> new double[]{ship.getRotation(), n*7, n*7};
            case LASER -> new double[]{ship.getRotation(), n*5, n*2};
        };
    }
}
