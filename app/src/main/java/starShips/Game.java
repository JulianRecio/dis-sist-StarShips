package starShips;

import javafx.scene.input.KeyCode;
import starShips.configuration.GameConfig;
import starShips.configuration.GameConfigHandler;
import starShips.model.*;
import starShips.model.Entities.Asteroid;
import starShips.model.Entities.Entity;
import starShips.model.Entities.Ship;
import starShips.model.Enums.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private GameConfig config;
    private List<String> playerIds;
    private GameState gameState;
    private Map<String, Integer> points;
    private List<String> eliminated;
    private boolean isPaused;
    private boolean endGame;

    public Game() {
        this.config = new GameConfig();
        this.eliminated = new ArrayList<>();
        this.isPaused = false;
        this.points = new HashMap<>();
    }

    public void start(boolean startFromSaveFile) {
        if (startFromSaveFile) loadSaveGame();
        else startNewGame();
        this.isPaused = false;
        loadPoints();
    }

    private void loadSaveGame() {
        this.gameState = GameConfigHandler.getSavedGame();
    }

    private void loadPoints() {
        for (Player player: getPlayers()) {
            points.put(player.getId(), 0);
        }
    }

    private void startNewGame() {
        List<Player> players = generateIds(config);
        this.gameState = new GameState(EntitiesGenerator.generate(players.size(), players, config), players);
    }

    private List<Player> generateIds(GameConfig configuration) {
        int amount = configuration.getNumberOfPlayers();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            players.add(new Player("player-" + i, configuration.getDefaultLives(), "starship-" + i+1));
        }
        return players;
    }


    public void update() {
        if (!isPaused && playerIds != null){
            boolean hasShip = false;
            List<Entity> nextEntities = new ArrayList<>();
            boolean entered = false;
            for (Entity entity : getEntities()) {
                if (entity.getType() == EntityType.SHIP) hasShip = true;
                if (entity.getType() != EntityType.SHIP && !entered){
                    handleAsteroidSpawn(nextEntities);
                    entered = true;
                }
                Entity newEntity = entity.update();
                if (newEntity != null) nextEntities.add(newEntity);
                else {
                    addEliminated(entity.getId());
                }
            }
            if (!hasShip) gameOver();
            if (getEntities().size() == getPlayers().size()){
                handleAsteroidSpawn(nextEntities);
            }
            this.gameState = new GameState(nextEntities, getNewPlayers());
        }
    }

    public void accelerateShip(String shipId, boolean moveForward){
        List<Entity> nextStateEntities = new ArrayList<>();
        for (Entity entity: getEntities()){
            if (entity.getId().equals(shipId) && !isPaused){
                Ship ship = (Ship) entity;
                nextStateEntities.add(ship.move(moveForward));
            }
            else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }
        updateGameState(nextStateEntities, getNewPlayers());
    }

    public void turnShip(String shipId, double turn){
        List<Entity> nextStateEntities = new ArrayList<>();
        for (Entity entity: getEntities()){
            if (entity.getId().equals(shipId) && !isPaused){
                Ship ship = (Ship) entity;
                nextStateEntities.add(ship.turn(turn));
            }
            else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }
        updateGameState(nextStateEntities, getNewPlayers());
    }

    public void shoot(String shipId){
        List<Entity> nextStateEntities = new ArrayList<>();
        Ship shooterShip = null;
        for (Entity entity: getEntities()){
            if (entity.getId().equals(shipId)){
                shooterShip = (Ship) entity;
                if (shooterShip.getWeapon().isLoaded()){
                    nextStateEntities.add(shooterShip.useWeapon());
                }
                else {
                    nextStateEntities.add(shooterShip.getNewEntity());
                }
            }
            else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }
        if (shooterShip != null && shooterShip.getWeapon().isLoaded()){
            nextStateEntities.add(ShotGenerator.shoot(shooterShip));
        }
        updateGameState(nextStateEntities, getNewPlayers());
    }

    private void updateGameState(List<Entity> nextStateEntities, List<Player> newPlayers) {
    }

    public void addEliminated(String id) {
        eliminated.add(id);
    }

    private void handleAsteroidSpawn(List<Entity> nextEntities) {
        List<Asteroid> asteroids = new ArrayList<>();
        for (Entity entity :
                getEntities()) {
            asteroids.add((Asteroid) entity);
        }
        AsteroidGenerator.handleAsteroidSpawn(asteroids, nextEntities);
    }

    public void addPoints(String id, int addPoints) {
        int addedPoints = points.get(id) + addPoints;
        points.put(id,addedPoints);
    }

    public void gameOver() {
        this.endGame = true;
    }

    public List<String> getPlayerIds() {
        return playerIds;
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

    public GameState getGameState() {
        return gameState;
    }

    public List<String> getEliminated() {
        return eliminated;
    }

    public Map<String, KeyCode> getKeyBoardConfig(){
        return config.getKeyBoardConfig();
    }

    public List<Player> getNewPlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player :
                getPlayers()) {
            players.add(player.getNewPlayer());
        }
        return players;
    }

    public List<Entity> getNewEntities() {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity :
                getEntities()) {
            entities.add(entity.getNewEntity());
        }
        return entities;
    }

    public boolean isOver() {
        return endGame;
    }

    public List<Player> getPlayers() {
        return gameState ==null ? null : gameState.getPlayers();
    }

    public List<Entity> getEntities() {
        return gameState ==null ? null : gameState.getEntities();
    }

    public void pauseOrUnPause() {
        this.isPaused = !isPaused;
    }
}
