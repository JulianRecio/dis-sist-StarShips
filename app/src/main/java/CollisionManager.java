import Model.Asteroid;
import Model.Entity;
import Model.Enums.EntityType;
import Model.Enums.ShotType;
import Model.Ship;
import Model.Shot;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    public static GameState manageCollision(Entity entity1, Entity entity2, GameState gameState) {

        GameState nextGameState = null;
        if (entity1.getType() == EntityType.SHOT  && entity2.getType() == EntityType.ASTEROID || entity1.getType() == EntityType.ASTEROID && entity2.getType() == EntityType.SHOT){
            nextGameState = manageShotAndAsteroid(entity1, entity2, gameState);
        }
        else if (entity1.getType() == EntityType.SHOT  && entity2.getType() == EntityType.SHIP || entity1.getType() == EntityType.SHIP && entity2.getType() == EntityType.SHOT){
            nextGameState = manageShipAndShot(entity1, entity2, gameState);
        }
        else if (entity1.getType() == EntityType.ASTEROID  && entity2.getType() == EntityType.SHIP || entity1.getType() == EntityType.SHIP && entity2.getType() == EntityType.ASTEROID){
            nextGameState = manageShipAndAsteroid(entity1, entity2, gameState);
        }
        if (nextGameState == null) return gameState;
        return nextGameState;
    }

    private static GameState manageShipAndAsteroid(Entity entity1, Entity entity2, GameState gameState) {
        Asteroid asteroid;
        Ship ship;

        if (entity1.getType() == EntityType.ASTEROID){
            asteroid = (Asteroid) entity1;
            ship = (Ship) entity2;
        }else{
            asteroid = (Asteroid) entity2;
            ship = (Ship) entity1;
        }

        List<Entity> nextStateEntities = new ArrayList<>();
        for (Entity entity : gameState.getEntities()) {
            if (entity.getId().equals(ship.getId())){
                int updatedLives = gameState.getPlayerLives().get(entity.getId()) - 1;
                gameState.getPlayerLives().put(ship.getId(), updatedLives);
                if (updatedLives > 0){
                    nextStateEntities.add(new Ship(entity.getId(), entity.getEntityPosition(), 180, 0, ship.getWeapon(), false, ship.getHitBoxType()));
                }
            }

        }
        //borrar asteroide
        return new GameState(nextStateEntities, gameState.getPlayerIds(), gameState.getPlayerLives(),gameState.getPoints());
    }

    private static GameState manageShipAndShot(Entity entity1, Entity entity2, GameState gameState) {
        Shot shot;
        Ship ship;

        if (entity1.getType() == EntityType.SHOT){
            shot = (Shot) entity1;
            ship = (Ship) entity2;
        }else{
            shot = (Shot) entity2;
            ship = (Ship) entity1;
        }

        if (ship.getId().equals(shot.getId())) return null;

        List<Entity> nextStateEntities = new ArrayList<>();
        for (Entity entity : gameState.getEntities()) {
            if (entity.getId().equals(ship.getId())){
                int updatedLives = gameState.getPlayerLives().get(entity.getId()) - 1;
                gameState.getPlayerLives().put(ship.getId(), updatedLives);
                if (updatedLives > 0){
                    nextStateEntities.add(new Ship(entity.getId(), entity.getEntityPosition(), 180, 0, ship.getWeapon(), false, ship.getHitBoxType()));
                }
            }

        }
        //borrar bala
        return new GameState(nextStateEntities, gameState.getPlayerIds(), gameState.getPlayerLives(),gameState.getPoints());

    }

    private static GameState manageShotAndAsteroid(Entity entity1, Entity entity2, GameState gameState) {
        Shot shot;
        Asteroid asteroid;

        if (entity1.getType() == EntityType.SHOT){
            shot = (Shot) entity1;
            asteroid = (Asteroid) entity2;
        }else{
            shot = (Shot) entity2;
            asteroid = (Asteroid) entity1;
        }

        List<Entity> nextStateEntities = new ArrayList<>();
        for (Entity entity : gameState.getEntities()) {
            if (entity.getId().equals(asteroid.getId())){
                Asteroid newAsteroid = new Asteroid(asteroid.getId(), asteroid.getIntegrity() - getDamage(shot.getShotType()));

                if (newAsteroid.getIntegrity() <= 0 ){
                    Integer points = null;
                    if (gameState.getPoints().get(shot.getId()) != null) {
                        points = gameState.getPoints().get(shot.getId()) + 100;
                    }else {
                        points = 100;
                    }
                    gameState.getPoints().put(shot.getId(), points);
                }else{
                    nextStateEntities.add(entity);
                }
            }

        }
        //borrar bala
        return new GameState(nextStateEntities, gameState.getPlayerIds(), gameState.getPlayerLives(),gameState.getPoints());
    }

    private static int getDamage(ShotType shotType){
        switch (shotType){
            case SHOT, SPREAD -> {
                return 1;
            }
            case LASER -> {
                return 2;
            }
        }
        return 1;
    }
}
