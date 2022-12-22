package starShips.model;

import starShips.configuration.GameConfig;
import starShips.model.Entities.Entity;
import starShips.model.Entities.Ship;
import starShips.model.Enums.Color;
import starShips.model.Enums.ShotType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipsGenerator {
    public static List<Entity> generate(int amountOfShips, List<Player> players, GameConfig gameConfiguration){
        List<Entity> objects = new ArrayList<>();
        addShips(objects, amountOfShips, players, gameConfiguration);
        return objects;
    }
    private static void addShips(List<Entity> objects, int amountOfShips, List<Player> players, GameConfig gameConfiguration){
        Map<String, ShotType> shotTypes = gameConfiguration.getShotTypes();
        Map<String, Color> shipColors = gameConfiguration.getColors();
        for (int i = 1; i <= amountOfShips ; i++) {
            String id = "starship-" + i;
            double xPosition = ((800 / amountOfShips) * i)/2;
            Player player = players.get(i-1);
            Ship ship = new Ship(id,xPosition, 300, 180, 40,40, 180, shipColors.get("color-" + i), player.getId(), shotTypes.get("shot-"+i), 0, System.currentTimeMillis());
            objects.add(ship);
        }
    }
}
