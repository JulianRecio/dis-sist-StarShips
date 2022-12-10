package Model;

import Model.Enums.EntityType;

import java.util.List;
import java.util.Map;

public class GameState {

    private double height;
    private double width;
    List<Ship> ships;
    List<Entity> entities;
    Map<Long, Integer> playerLives;
    boolean isPaused;
    Map<Long, Integer> points;

    public GameState(double height, double width, List<Ship> ships, Map<Long, Integer> playerLives, boolean isPaused, Map<Long, Integer> points, List<Entity> entities) {
        this.height = height;
        this.width = width;
        this.ships = ships;
        this.playerLives = playerLives;
        this.isPaused = isPaused;
        this.points = points;
        this.entities = entities;
    }

    public GameState(double height, double width, List<Ship> ships, Map<Long, Integer> playerLives, Map<Long, Integer> points, List<Entity> entities) {
        this(height,width,ships,playerLives,false,points,entities);
    }

    private GameState collideEntities(Entity entity1, Entity entity2){

        if (entity1.getType() == EntityType.SHOT && entity2.getType() == EntityType.ASTEROID){

            Shot shot = (Shot) entity1;

            Asteroid asteroid = (Asteroid) entity2;

            switch (shot.getShotType()){
                case SHOT, SPREAD -> asteroid.setIntegrity(asteroid.getIntegrity() - 1);
                case LASER -> asteroid.setIntegrity(asteroid.getIntegrity() - 3);
            }

            if (asteroid.getIntegrity() < 0){
                entities.remove(entity2);

            }else {
                entities.remove(entity2);
                entities.add(asteroid);
            }

        } else if ((entity1.getType() == EntityType.SHOT || entity1.getType() == EntityType.ASTEROID) && entity2.getType() == EntityType.SHIP){

            int lives = playerLives.get(entity2.getId()) - 1;

            playerLives.put(entity2.getId(), lives);

        } else if (entity1.getType() == EntityType.SHIP && entity2.getType() == EntityType.SHIP){

            int livesP1 = playerLives.get(entity1.getId()) - 1;
            int livesP2 = playerLives.get(entity2.getId()) - 1;

            playerLives.put(entity1.getId(), livesP1);
            playerLives.put(entity2.getId(), livesP2);

        }
        return new GameState(height,width,ships,playerLives,false,points,entities);
    }

}
