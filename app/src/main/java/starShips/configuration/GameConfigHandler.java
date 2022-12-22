package starShips.configuration;

import starShips.GameState;
import starShips.model.Entities.*;
import starShips.model.Enums.Color;
import starShips.model.Enums.ShotType;
import starShips.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameConfigHandler {

    public static void saveGame(GameState gameState){
        writeSaveFile(gameState.getEntities(), gameState.getPlayers());
    }

    private static String getDirectory(){
        return "app\\src\\main\\java\\starShips\\configuration\\StShSave";
    }

    private static void writeSaveFile(List<Entity> entities, List<Player> players) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getDirectory()))){
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
        for (Player player: players){
            String toWrite = getStringToWrite(player);
            writer.write(toWrite + "\n");
        }
    }

    private static String getStringToWrite(Entity entity) {
        String str = "id:" + entity.getId() + ";type:" + entity.getType().toString() + ";x:" + entity.getX() + ";y:" + entity.getX() + ";rotation:" + entity.getRotation() + ";trajectory:" + entity.getTrajectory()
                + ";height:" + entity.getHeight() + ";width:" + entity.getWidth() + ";hitBox:" + entity.getHitBoxType() + ";color:" + entity.getColor().toString();
        return str + addUniqueParameters(entity);
    }

    private static String addUniqueParameters(Entity entity) {
        switch (entity.getType()){
            case SHIP -> {
                Ship ship = (Ship) entity;
                return ";previousShot:" + ship.getPreviousShot() + ";playerId:" + ship.getPlayerId() + ";speed:" + ship.getSpeed() + ";shotType:" + ship.getShotType();
            }
            case SHOT -> {
                Shot shot = (Shot) entity;
                return ";shooterId:" + shot.getShooterId() + ";damageOutput:" + shot.getDamageOutput() + ";shotType:" + shot.getShotType();
            }
            case ASTEROID -> {
                Asteroid asteroid = (Asteroid) entity;
                return ";rotatesClockwise:" + asteroid.isRotatesClockwise() + ";integrity:" + asteroid.getIntegrity();
            }
        }
        return "";
    }

    private static String getStringToWrite(Player player) {
        return "id:" + player.getId() + ";points:" + player.getPoints() + ";lives:" + player.getLives() + ";playerShip:" + player.getPlayerShip();
    }

    public static GameState getSavedGame() {
        List<String> configLines = getLinesFromFile();
        List<String> e = null;
        List<String> p = null;
        for (int i = 0; i < configLines.size(); i++) {
            String line = configLines.get(i);
            if (line.equals("")){
                e = configLines.subList(0, i);
                p = configLines.subList(i+1, configLines.size());
                break;
            }
        }
        List<Entity> entities = getSavedEntities(e);
        List<Player> players = getSavedPlayers(p);
        return new GameState(entities, players);
    }

    private static List<Player> getSavedPlayers(List<String> lines) {
        List<Player> players = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(";");
            String id = (String) transform(parts[0]);
            int points = (int) transform(parts[1]);
            int lives = (int) transform(parts[2]);
            String shipId = (String) transform(parts[3]);
            Player player = new Player(id, points, lives, shipId);
            players.add(player);
        }
        return players;
    }

    private static List<Entity> getSavedEntities(List<String> lines) {
        List<Entity> Entities = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(";");
            String id = (String) transform(parts[0]);
            String type = (String) transform(parts[1]);
            double x = (double) transform(parts[2]);
            double y = (double) transform(parts[3]);
            double rotation = (double) transform(parts[4]);
            double direction = (double) transform(parts[5]);
            double height = (double) transform(parts[6]);
            double width = (double) transform(parts[7]);
            String hitBox = (String) transform(parts[8]);
            String color = (String) transform(parts[9]);
            Entities.add(createEntity(parts, id, type, x, y, rotation, direction, height, width, color));
        }
        return Entities;
    }

    private static Entity createEntity(String[] parts, String id, String type, double x, double y, double rotation, double direction, double height, double width, String color) {
        switch (type){
            case "SHIP" -> {
                long previousShot = (long) transform(parts[10]);
                String playerId = (String) transform(parts[11]);
                double speed = (double) transform(parts[12]);
                String shotType = (String) transform(parts[13]);
                return new Ship(id, x, y, rotation, height, width, direction, getColor(color), playerId, getShotType(shotType) , speed, previousShot);
            }
            case "ASTEROID" -> {
                boolean clockwise = (boolean) transform(parts[10]);
                int initialHealthBar = (int) transform(parts[11]);
                return new Asteroid(id, x, y, rotation, height, width, direction, initialHealthBar, clockwise);
            }
            case "SHOT" -> {
                String shipId = (String) transform(parts[10]);
                int damageOutput = (int) transform(parts[11]);
                String shotType = (String) transform(parts[12]);
                return new Shot(id, x, y, rotation, height, width, direction, getColor(color),shipId, getShotType(shotType), damageOutput);
            }
        }
        return null;
    }

    private static ShotType getShotType(String shotType) {
        return switch (shotType) {
            case "PLASMA" -> ShotType.PLASMA;
            case "LIGHTNING" -> ShotType.LIGHTNING;
            default -> ShotType.LASER;
        };
    }

    private static Color getColor(String color) {
        return color.equals("RED") ? Color.RED : Color.BLUE;
    }

    private static Object transform(String line){
        String[] str = line.split(":");
        String variable = str[0];
        String value = str[1];
        return switch (variable){
            case "id", "playerId", "type", "color", "hitBox", "shipId", "shotType" -> value;
            case "points", "lives", "integrity", "damageOutput" -> Integer.parseInt(value);
            case "rotatesClockwise" -> Boolean.parseBoolean(value);
            case "x", "y", "rotation", "trajectory", "height", "width", "speed" -> Double.parseDouble(value);
            case "previousShot" -> Long.parseLong(value);
            default -> "";
        };
    }

    private static List<String> getLinesFromFile(){
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getDirectory()))){
            String line;
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
