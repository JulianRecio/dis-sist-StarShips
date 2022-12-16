package starShips.configuration;

import starShips.GameState;
import starShips.model.Entities.*;
import starShips.model.Enums.ShotType;
import starShips.model.Player;
import starShips.model.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameConfigHandler {

    public static void saveGame(GameState gameState){
        writeSaveFile(gameState.getEntities(), gameState.getPlayers());
    }

    private static void writeSaveFile(List<Entity> entities, List<Player> players) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("app\\src\\main\\java\\starShips.Configuration\\StShSave"))){
            saveEntities(entities, writer);
            savePlayers(players, writer);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static void saveEntities(List<Entity> entities, BufferedWriter writer) throws IOException {
        for (Entity entity: entities) {
            String toWrite = getStringToWrite(entity);
            writer.write(toWrite + "\n");
        }
        writer.write("\n");
    }

    private static void savePlayers(List<Player> players, BufferedWriter writer) throws IOException {
        for (Player player : players) {
            String toWrite = getStringToWrite(player);
            writer.write(toWrite + "\n");
        }
        writer.write("\n");
    }

    private static String getStringToWrite(Entity entity) {
        String str = "id:" + entity.getId() + ",type:" + entity.getType().toString() + ",x:" + entity.getEntityPosition().getX() + ",y:" + entity.getEntityPosition().getY() +
        ",rotation:"+ entity.getRotation() + ",speed:" + entity.getSpeed()+ ",height:" + entity.getHeight() + ",width:" + entity.getWidth() + ",trajectory:" + entity.getTrajectory() + ",hitbox:" + entity.getHitBoxType().toString();
        return str + extendedParams(entity);
    }

    private static String extendedParams(Entity entity) {
        switch (entity.getType()){
            case SHIP -> {
                Ship ship = (Ship) entity;
                return ",shotType:" + ship.getWeapon().getShotType().toString() + ",previousShot:" + ship.getWeapon().getPreviousShot() + ",playerId:" + ship.getPlayerId() + ",isAccelerating:" + ship.isAccelerating();
            }
            case ASTEROID -> {
                Asteroid asteroid = (Asteroid) entity;
                return ",integrity:"+asteroid.getIntegrity() + ",rotatesClockwise:"+asteroid.isRotatesClockwise();
            }
            case SHOT -> {
                Shot shot = (Shot) entity;
                return ",shooterId:"+shot.getShooterId()+",shotType:"+shot.getShotType().toString()+",damageOutput:"+shot.getDamageOutput();
            }
        };
        return "";
    }

    private static String getStringToWrite(Player player) {
        return "id:"+ player.getId() + ",points:" + player.getPoints() + ",shipId:"+ player.getPlayerShip();
    }

    public static GameState getSavedGame() {

        List<String> configLines = getLinesFromFile();
        List<String> entities = null;
        List<String> players = null;
        for (int i = 0; i < configLines.size(); i++) {
            String line = configLines.get(i);
            if (line.equals("%")){
                entities = configLines.subList(0, i);
                players = configLines.subList(i+1, configLines.size());
                break;
            }
        }
        return null;
    }

    public static GameState loadSaveState(){
        List<String> configLines = getLinesFromFile();
        List<String> entities = null;
        List<String> players = null;
        for (int i = 0; i < configLines.size(); i++) {
            String line = configLines.get(i);
            if (line.equals("%")){
                entities = configLines.subList(0, i);
                players = configLines.subList(i+1, configLines.size());
                break;
            }
        }
        List<Entity> savedEntities = getSavedEntities(entities);
        List<Player> savedPlayers = getSavedPlayers(players);
        return new GameState(savedEntities, savedPlayers);
    }



    private static List<Entity> getSavedEntities(List<String> lines) {
        List<Entity> entities = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(",");
            String id = (String) transform(parts[0]);
            String type = (String) transform(parts[1]);
            double x = (double) transform(parts[2]);
            double y = (double) transform(parts[3]);
            double rotation = (double) transform(parts[4]);
            double speed = (double) transform(parts[5]);
            double height = (double) transform(parts[6]);
            double width = (double) transform(parts[7]);
            String trajectory = (String) transform(parts[8]);
            entities.add(createEntity(parts, id, type, x, y, rotation, speed, height, width, trajectory));
        }
        return entities;

    }

    private static Entity createEntity(String[] parts, String id, String type, double x, double y, double rotation, double speed, double height, double width, String trajectory) {
        return null;
        //TODO
    }

    private static ShotType getShotType(Object string) {
        return null;
        //TODO
    }

    private static List<Player> getSavedPlayers(List<String> lines) {
        List<Player> players = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(",");
            String id = (String) transform(parts[0]);
            int points = (int) transform(parts[1]);
            int lives = (int) transform(parts[2]);
            String shipId = (String) transform(parts[3]);
            Player player = new Player(id, points, lives, shipId);
            players.add(player);
        }
        return players;
    }

    private static Object transform(String line){
        String[] str = line.split(":");
        String variable = str[0];
        String value = str[1];
        return switch (variable){
            case "id", "playerId", "type", "color", "hitbox", "shipId", "shotType" -> value;
            case "points", "lives", "integrity", "damage" -> Integer.parseInt(value);
            case "clockwise" -> Boolean.parseBoolean(value);
            case "x", "y", "trajectory", "direction", "height", "width", "speed" -> Double.parseDouble(value);
            case "previousShot" -> Long.parseLong(value);
            default -> "";
        };
    }


    private static List<String> getLinesFromFile(){
        List<String> lines =new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("app\\src\\main\\java\\starShips.Configuration\\StShSave"));
            String line;
            while ((line = br.readLine()) != null){
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
