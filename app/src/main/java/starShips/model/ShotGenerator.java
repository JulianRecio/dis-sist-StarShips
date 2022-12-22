package starShips.model;

import starShips.model.Entities.Ship;
import starShips.model.Entities.Shot;
import starShips.model.Enums.ShotType;

import java.util.Random;

public class ShotGenerator {
    private static int count = 0;

    public static Shot shoot(Ship ship){
        String id = "shot-" + ++count;
        Random r = new Random();
        double n =  2 + (5-2) * r.nextDouble();
        double[] values = getSpecificValues(ship, n);
        return new Shot(id,ship.getX()+16, ship.getY(), values[0], values[1], values[2],
                ship.getRotation(), ship.getColor(), ship.getPlayerId(),ship.getShotType(),
                (int) (n*13));
    }
    private static double[] getSpecificValues(Ship ship, double n){
        ShotType type = ship.getShotType();
        if (ship.getShotType() == null){type = ShotType.LASER;}
        return switch (type){
            case LASER-> new double[]{ship.getRotation(), n*5, n*2};
            case LIGHTNING -> new double[]{ship.getRotation()-20, n*12, n*4};
            case PLASMA -> new double[]{ship.getRotation(), n*7, n*7};
        };
    }
}
