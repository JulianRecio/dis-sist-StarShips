package starShips.model;

import starShips.Game;
import starShips.GameState;
import starShips.model.Entities.Asteroid;
import starShips.model.Entities.Entity;
import starShips.model.Entities.Ship;
import starShips.model.Entities.Shot;
import starShips.model.Enums.EntityType;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    public static GameState manageCollision(Entity entity1, Entity entity2, GameState gameState, Game game) {

        List<Entity> entities = gameState.getEntities();
        List<Player> players = gameState.getPlayers();

        GameState nextGameState = null;
        if (entity1.getType() == EntityType.SHOT  && entity2.getType() == EntityType.ASTEROID || entity1.getType() == EntityType.ASTEROID && entity2.getType() == EntityType.SHOT){
            nextGameState = manageShotAndAsteroid(entity1, entity2, entities, players, game);
        }
        else if (entity1.getType() == EntityType.SHOT  && entity2.getType() == EntityType.SHIP || entity1.getType() == EntityType.SHIP && entity2.getType() == EntityType.SHOT){
            nextGameState = manageShipAndShot(entity1, entity2, entities, players, game);
        }
        else if (entity1.getType() == EntityType.ASTEROID  && entity2.getType() == EntityType.SHIP || entity1.getType() == EntityType.SHIP && entity2.getType() == EntityType.ASTEROID){
            nextGameState = manageShipAndAsteroid(entity1, entity2, entities, players, game);
        }
        if (nextGameState == null) return new GameState(game.getNewEntities(), game.getNewPlayers());
        return nextGameState;
    }

    private static GameState manageShipAndAsteroid(Entity entity1, Entity entity2, List<Entity> entities, List<Player> players, Game game) {
        Asteroid asteroid;
        Ship ship;

        if (entity1.getType() == EntityType.ASTEROID){
            asteroid = (Asteroid) entity1;
            ship = (Ship) entity2;
        }else{
            asteroid = (Asteroid) entity2;
            ship = (Ship) entity1;
        }

        if (ship.getId().equals(asteroid.getId())) return null;

        List<Entity> nextStateEntities = new ArrayList<>();
        List<Player> nextStatePlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(ship.getPlayerId(), players);
        Player nextStatePlayer = null;

        for (Entity entity : entities) {
            if (entity.getId().equals(ship.getId())){
                nextStatePlayer = new Player(shipPlayer.getId(), shipPlayer.getPoints(), shipPlayer.getLives() - 1, shipPlayer.getPlayerShip());
                if (nextStatePlayer.getLives() > 0){
                    nextStateEntities.add(new Ship(ship.getId(), new Position(300,300 ), 180, 0, ship.getHeight(), ship.getWidth(), ship.getTrajectory(),  ship.getWeapon(), ship.getPlayerId(), ship.isAccelerating()));
                } else{
                    game.addEliminated(entity.getId());
                    nextStatePlayer = null;
                }
            }
            else if (entity.getId().equals(asteroid.getId())){
                game.addEliminated(entity.getId());
            }else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }

        for (Player player : players) {
            if (player.getId().equals(shipPlayer.getId())){
                if (nextStatePlayer == null) continue;
                nextStatePlayers.add(nextStatePlayer);
            }else {
                nextStatePlayers.add(player.getNewPlayer());
            }
        }
        if (nextStatePlayers.isEmpty()) game.gameOver();
        return new GameState(nextStateEntities, nextStatePlayers);
    }

    private static GameState manageShipAndShot(Entity entity1, Entity entity2, List<Entity> entities, List<Player> players, Game game) {
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
        List<Player> nextStatePlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(shot.getShooterId(), players);
        Player nextStatePlayer = null;

        for (Entity entity : entities) {
            if (shot.getId().equals(entity.getId())){
                game.addEliminated(entity.getId());
            }
            if (entity.getId().equals(ship.getId())){
                nextStatePlayer = new Player(shipPlayer.getId(), shipPlayer.getPoints(), shipPlayer.getLives() - 1, shipPlayer.getPlayerShip());
                if (nextStatePlayer.getLives() > 0){
                    nextStateEntities.add(new Ship(ship.getId(), new Position(300,300 ), 180, 0, ship.getHeight(), ship.getWidth(), ship.getTrajectory(),  ship.getWeapon(), ship.getPlayerId(), ship.isAccelerating()));
                } else{
                    game.addEliminated(entity.getId());
                    nextStatePlayer = null;
                }
            }
            else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }

        for (Player player : players) {
            if (player.getId().equals(shipPlayer.getId())){
                if (nextStatePlayer == null) continue;
                nextStatePlayers.add(nextStatePlayer);
            }else {
                nextStatePlayers.add(player.getNewPlayer());
            }
        }
        if (nextStatePlayers.isEmpty()) game.gameOver();
        return new GameState(nextStateEntities, nextStatePlayers);
    }

    private static GameState manageShotAndAsteroid(Entity entity1, Entity entity2, List<Entity> entities, List<Player> players, Game game) {
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
        List<Player> nextStatePlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(shot.getShooterId(), players, entities);
        Player nextStatePlayer = null;
        for (Entity entity : entities) {
            if (shot.getId().equals(entity.getId())){
                game.addEliminated(entity.getId());
            }
            if (entity.getId().equals(asteroid.getId())){
                Asteroid newAsteroid = new Asteroid(asteroid.getId(), asteroid.getEntityPosition(), asteroid.getRotation(),asteroid.getSpeed(),asteroid.getHeight(),asteroid.getWidth(),asteroid.getIntegrity() - shot.getDamageOutput(), asteroid.isRotatesClockwise(), asteroid.getTrajectory());
                if (newAsteroid.getIntegrity() <= 0 ){
                    nextStatePlayer = new Player(shipPlayer.getId(), shipPlayer.getPoints(), shipPlayer.getLives(), shipPlayer.getPlayerShip());
                    game.addPoints(nextStatePlayer.getId(), newAsteroid.getPoints());
                    game.addEliminated(entity.getId());
                }else{
                    nextStateEntities.add(newAsteroid);
                    nextStatePlayer = new Player(shipPlayer.getId(), shipPlayer.getPoints(), shipPlayer.getLives(), shipPlayer.getPlayerShip());
                }
            }
            else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }

        for (Player player :
                players) {
            if (player.getId().equals(shipPlayer.getId())){
                if (nextStatePlayer == null) continue;
                nextStatePlayers.add(player.getNewPlayer());
            }

        }

        return new GameState(nextStateEntities, nextStatePlayers);
    }

    private static Player getPlayer(String shooterId, List<Player> players, List<Entity> entities) {
        String playerId = "";
        for (Entity entity :
                entities) {
            if (entity.getType() == EntityType.SHIP && entity.getId().equals(shooterId)) {
                Ship ship = (Ship) entity;
                playerId = ship.getPlayerId();
            }
        }
        return getPlayer(playerId, players);
    }

    private static Player getPlayer(String playerId, List<Player> players) {
        for (Player player : players) {
            if (playerId.equals(player.getId())) {
                return player;
            }
        }
        return null;
    }
}
