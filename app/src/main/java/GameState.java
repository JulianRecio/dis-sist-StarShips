import Configuration.GameConfig;
import Model.*;
import Model.Enums.ButtonKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {

    private GameConfig config;
    private List<Entity> entities;
    private List<String> playerIds;
    private Map<String, Integer> playerLives;
    private Map<String, Integer> points;
    private boolean isPaused;


    public GameState() {
        config = new GameConfig();
        entities = new ArrayList<>();
        isPaused = false;
        points = new HashMap<>();
    }

    public GameState(GameConfig config, List<Entity> entities, List<String> playerIds, Map<String, Integer> playerLives, Map<String, Integer> points, boolean isPaused) {
        this.config = config;
        this.entities = entities;
        this.playerIds = playerIds;
        this.playerLives = playerLives;
        this.points = points;
        this.isPaused = isPaused;
    }

    public GameState(List<Entity> nextStateEntities, List<String> playerIds, Map<String, Integer> playerLives, Map<String, Integer> points) {
        new GameState(config, nextStateEntities, playerIds, playerLives, points, isPaused);
    }

    public GameState controlShip(String id, ButtonKey key){
        if (isPaused) return this;

        List<Entity> newEntities = new ArrayList<>();
        Ship ship = null;
        for (Entity entity: entities) {
            if (entity.getId().equals(id)) ship = (Ship) entity;
        }

        switch (key){
            case UP -> {
                newEntities.add(ship.accelerate());
            }
            case RIGHT -> {
                newEntities.add(ship.rotateClockwise());
            }
            case LEFT -> {
                newEntities.add(ship.rotateCounterClockwise());
            }
            case DOWN -> {
                newEntities.add(ship.speedBreak());
            }
            case NO_KEY -> {
                newEntities.add(ship.decelerate());
            }
            case SHOOT -> {
                newEntities.add(ship.useWeapon());
            }
        }

        return new GameState(config, newEntities, playerIds, playerLives, points, isPaused);
    }

    public GameState collideEntities(String id1, String id2){
        Entity entity1 = null;
        Entity entity2 = null;

        for (Entity entity: entities) {
            if (entity.getId().equals(id1)) entity1 = entity;
            if (entity.getId().equals(id2)) entity2 = entity;
        }

        if (entity1 != null && entity2 != null){
            return CollisionManager.manageCollision(entity1, entity2, this);
        }else{
            return this;
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public Map<String, Integer> getPlayerLives() {
        return playerLives;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public GameConfig getConfig() {
        return config;
    }

    public void start() {
        startNewGame();
        this.isPaused = false;
        loadPoints();
    }

    private void loadPoints() {
        for (int i = 1; i <= config.getNumberOfPlayers(); i++){
            points.put(playerIds.get(i), 0);
        }
    }

    private void startNewGame() {
        playerIds = generateIds(config);
    }

    private List<String> generateIds(GameConfig config) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= config.getNumberOfPlayers(); i++){
            String id = "ship" + Integer.toString(i);
            ids.add(id);
        }
        return ids;
    }


}
