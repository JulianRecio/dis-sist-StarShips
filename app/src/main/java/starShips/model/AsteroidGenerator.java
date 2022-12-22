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

    public static void handleAsteroidSpawn(List<Asteroid> asteroids, List<Entity> entities, Random random) {
        if (asteroids.size() < 5) {
            createNewAsteroid(entities, random);
        }
    }

    private static void createNewAsteroid(List<Entity> entities, Random random) {
        List<Ship> ships = getShipsOnStage(entities);
        double x ;
        double y;
        Ship target = getRandomShip(ships, random);
        if (target == null) return;
        int side = random.nextInt(5);
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
        double trajectory = getDirection(x,y, target, random);
        String id = "asteroid-" + ++asteroidCount;
        double height = 50 + random.nextDouble()*10;
        double width = 50 + random.nextDouble()*10;
        int integrity = getRandomIntegrity(random);
        entities.add(new Asteroid(id,x,y, 180, height, width, trajectory, integrity, random.nextBoolean()));
    }

    private static int getRandomIntegrity(Random random) {
        return random.nextInt(6);
    }

    private static Ship getRandomShip(List<Ship> ships, Random random) {
        return ships.isEmpty() ? null : ships.get(random.nextInt(ships.size()));
    }

    private static double getDirection(double x, double y, Ship target, Random random) {
        return Math.toDegrees(Math.atan2(target.getX() - x, target.getY()- y)) + 20 * random.nextDouble();
    }

    private static List<Ship> getShipsOnStage(List<Entity> entities) {

        List<Ship> shipList = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity.getType().equals(EntityType.SHIP)) shipList.add((Ship) entity);

        }
        return shipList;
    }
}
