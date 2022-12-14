package starShips.configuration;

import javafx.scene.input.KeyCode;
import starShips.model.Enums.Color;
import starShips.model.Enums.ShotType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfig {
    private final int numberOfPlayers;
    private final int defaultLives;
    private final Map<String, KeyCode> keyBoardConfig;
    private final Map<String, ShotType> shotTypes;
    private final Map<String, Color> shipColors;

    public GameConfig() {
        List<String> lines = getLinesFromFile();
        Map<String, String> map = getMap(lines);
        numberOfPlayers = Integer.parseInt(map.get("numberOfPlayers"));
        defaultLives = Integer.parseInt(map.get("defaultLives"));
        keyBoardConfig = getKeyBoardConfigMap(map.get("keyBoardConfig"));
        shotTypes = getShotTypesMap(map.get("shotTypes"));
        shipColors = getColorsMap(map.get("ships"));
    }

    private Map<String, Color> getColorsMap(String ships) {
        Map<String, Color> map = new HashMap<>();
        String[] split = ships.split(";");
        for (String s : split) {
            String[] innerSplit = s.split("=");
            map.put(innerSplit[0], getColor(innerSplit[1]));
        }
        return map;
    }

    private Map<String, ShotType> getShotTypesMap(String shotTypes) {
        Map<String, ShotType> shotMap = new HashMap<>();
        String[] split = shotTypes.split(";");
        for (String s : split){
            String[] innerSplit = s.split("=");
            shotMap.put(innerSplit[0], getShotType(innerSplit[1]));
        }
        return shotMap;
    }

    private ShotType getShotType(String s) {
        return switch (s){
            default -> ShotType.LASER;
            case "LIGHTNING" -> ShotType.LIGHTNING;
            case "PLASMA" -> ShotType.PLASMA;
        };
    }

    private Map<String, KeyCode> getKeyBoardConfigMap(String keyBoardConfig) {
        Map<String, KeyCode> buttonMap = new HashMap<>();
        String[] split = keyBoardConfig.split(";");
        for (String s : split){
            String[] innerSplit = s.split("=");
            buttonMap.put(innerSplit[0], getKeyCode(innerSplit[1]));
        }
        return buttonMap;
    }

    private KeyCode getKeyCode(String s) {
        return KeyCode.getKeyCode(s);
    }

    private Color getColor(String s) {
        return s.equals("RED") ? Color.RED : Color.BLUE;
    }

    private Map<String, String> getMap(List<String> lines) {
        Map<String, String> map = new HashMap<>();
        for (String line: lines){
            String[] split = line.split(":");
            map.put(split[0], split[1]);
        }
        return map;
    }

    private List<String> getLinesFromFile(){
        List<String> lines =new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("app\\src\\main\\java\\starShips\\configuration\\StConfig"));
            String line;
            while ((line = br.readLine()) != null){
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getDefaultLives() {
        return defaultLives;
    }

    public Map<String, KeyCode> getKeyBoardConfig() {
        return keyBoardConfig;
    }

    public Map<String, ShotType> getShotTypes() {
        return shotTypes;
    }

    public Map<String, Color> getColors() {return shipColors;}
}
