package Configuration;

import Model.Enums.ButtonKey;
import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfig {
    private int numberOfPlayers;
    private int defaultLives;
    private Map<ButtonKey, KeyCode> keyBoardConfig;

    public GameConfig() {
        List<String> lines = getLinesFromFile();
        Map<String, String> map = getMap(lines);
        numberOfPlayers = Integer.parseInt(map.get("numberOfPlayers"));
        defaultLives = Integer.parseInt(map.get("defaultLives"));
        keyBoardConfig = getKeyBoardConfig(map.get("keyBoardConfig"));
    }

    private Map<ButtonKey, KeyCode> getKeyBoardConfig(String keyBoardConfig) {
        Map<ButtonKey, KeyCode> buttonMap = new HashMap<>();
        String[] split = keyBoardConfig.split(",");
        for (String s : split){
            String[] innerSplit = s.split("=");
            ButtonKey buttonKey = generateButtonKey(innerSplit);
            buttonMap.put(buttonKey, getKeyCode(split[1]));
        }
        return buttonMap;
    }

    private KeyCode getKeyCode(String s) {
        return KeyCode.getKeyCode(s);
    }

    private ButtonKey generateButtonKey(String[] innerSplit) {
       String header = innerSplit[0];
       if (header.equals("up")) return ButtonKey.UP;
       if (header.equals("down")) return ButtonKey.DOWN;
       if (header.equals("left")) return ButtonKey.LEFT;
       if (header.equals("right")) return ButtonKey.RIGHT;
       if (header.equals("shoot")) return ButtonKey.SHOOT;
       return ButtonKey.NO_KEY;
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
            BufferedReader br = new BufferedReader(new FileReader("app\\src\\main\\java\\Configuration\\StConfig"));
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

    public Map<ButtonKey, KeyCode> getKeyBoardConfig() {
        return keyBoardConfig;
    }
}
