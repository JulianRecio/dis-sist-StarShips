package starShips;

import javafx.scene.input.KeyCode;
import starShips.configuration.GameConfig;
import starShips.configuration.GameConfigHandler;
import starShips.model.*;
import starShips.model.Entities.Asteroid;
import starShips.model.Entities.Entity;
import starShips.model.Entities.Ship;
import starShips.model.Enums.EntityType;

import java.util.*;

public class Game {
    private final GameConfig config;
    private GameState gameState;
    private final Map<String, Integer> points;
    private final List<String> eliminated;
    private boolean isPaused;
    private boolean endGame;
    private final Random random;

    public Game() {
        this.config = new GameConfig();
        this.eliminated = new ArrayList<>();
        this.isPaused = false;
        this.points = new HashMap<>();
        random = new Random();
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
        this.gameState = new GameState(ShipsGenerator.generate(players.size(), players, config), players);
    }

    private List<Player> generateIds(GameConfig configuration) {
        int amount = configuration.getNumberOfPlayers();
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            players.add(new Player("player-" + i, configuration.getDefaultLives(), "starship-" + i));
        }
        return players;
    }


    public void update() {
        if (!isPaused && gameState != null){
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
                if (shooterShip.canShoot()){
                    nextStateEntities.add(shooterShip.shoot());
                }
                else {
                    nextStateEntities.add(shooterShip.getNewEntity());
                }
            }
            else {
                nextStateEntities.add(entity.getNewEntity());
            }
        }
        if (shooterShip != null && shooterShip.canShoot()){
            nextStateEntities.add(ShotGenerator.shoot(shooterShip, random));
        }
        updateGameState(nextStateEntities, getNewPlayers());
    }

    private void updateGameState(List<Entity> nextStateEntities, List<Player> newPlayers) {
        this.gameState = new GameState(nextStateEntities, newPlayers);
    }

    public void addEliminated(String id) {
        eliminated.add(id);
    }

    private void handleAsteroidSpawn(List<Entity> nextEntities) {
        List<Asteroid> asteroids = new ArrayList<>();
        for (Entity entity : getEntities()) {
            if (entity.getType() == EntityType.ASTEROID){
                asteroids.add((Asteroid) entity);
            }
        }
        AsteroidGenerator.handleAsteroidSpawn(asteroids, nextEntities, random);
    }

    public void handleCollision(String id1, String id2){
        Entity entity1 = null;
        Entity entity2 = null;
        for (Entity entity : getEntities()){
            if (entity.getId().equals(id1)) entity1 = entity;
            if (entity.getId().equals(id2)) entity2 = entity;
        }
        if(entity1 != null && entity2 != null){
            this.gameState = CollisionManager.manageCollision(entity1, entity2, gameState, this);
        }else {
            updateGameState(getNewEntities(), getNewPlayers());
        }
    }
    public void addPoints(String id, int addPoints) {
        int addedPoints = points.get(id) + addPoints;
        points.put(id,addedPoints);
    }

    public void gameOver() {
        this.endGame = true;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public boolean isPaused() {
        return isPaused;
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

    public void saveGame() {
        GameConfigHandler.saveGame(gameState);
    }

    public void printLeaderboard() {
        if (getPlayers() != null){
            System.out.println("LEADERBOARD");
            points.forEach((key, value) -> System.out.println(key  + " = " + value + " points"));
        }
    }
}
