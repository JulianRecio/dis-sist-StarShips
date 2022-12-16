package starShips.model;

import starShips.model.Entities.Asteroid;
import starShips.model.Entities.Entity;
import starShips.model.Entities.Ship;
import starShips.model.Enums.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidGenerator {
    private static int asteroidCount = 0;
    static Random random = new Random();

    public static void handleAsteroidSpawn(List<Asteroid> asteroids, List<Entity> entities) {
        if (asteroids.size() < 5) {
            createNewAsteroid(entities);
        }
    }

    private static void createNewAsteroid(List<Entity> entities) {
        List<Ship> ships = getShipsOnStage(entities);
        double x ;
        double y;
        Ship target = getRandomShip(ships);
        if (target == null) return;
        int side = random.nextInt(4);
        double position = 800 * random.nextDouble();
        switch (side){
            case 0 -> {
                x = position;
                y = 0;
            }
            case 1 -> {
                x = 0;
                y = position;
            }
            case 2 -> {
                x = position;
                y = 794;
            }
            default -> {
                x = 794;
                y = position;
            }
        }
        double trajectory = getDirection(x,y, target);
        String id = "asteroid-" + ++asteroidCount;
        double height = 50;
        double width = 50;
        int integrity = getRandomIntegrity();
        int speed = random.nextInt() * 7;
        entities.add(new Asteroid(id,new Position(x,y), 180,speed, height, width, integrity, random.nextBoolean(), trajectory));
    }

    private static int getRandomIntegrity() {
        return random.nextInt(6);
    }

    private static Ship getRandomShip(List<Ship> ships) {
        return ships.isEmpty() ? null : ships.get(random.nextInt(ships.size()));
    }

    private static double getDirection(double x, double y, Ship target) {
        return Math.toDegrees(Math.atan2(target.getEntityPosition().getX() - x, target.getEntityPosition().getY()- y)) + 7 * random.nextDouble();
    }

    private static List<Ship> getShipsOnStage(List<Entity> entities) {

        List<Ship> shipList = new ArrayList<>();

        for (Entity entity :
                entities) {
            if (entity.getType().equals(EntityType.SHIP)) shipList.add((Ship) entity);

        }

        return shipList;
    }
}
