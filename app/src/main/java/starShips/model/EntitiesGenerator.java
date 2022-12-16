package starShips.model;

import starShips.configuration.GameConfig;
import starShips.model.Entities.Entity;
import starShips.model.Entities.Ship;
import starShips.model.Entities.Weapon;
import starShips.model.Enums.EntityType;
import starShips.model.Enums.HitBoxType;
import starShips.model.Enums.ShotType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntitiesGenerator {
    public static List<Entity> generate(int amountOfShips, List<Player> players, GameConfig gameConfiguration){
        List<Entity> objects = new ArrayList<>();
        addShips(objects, amountOfShips, players, gameConfiguration);
        return objects;
    }
    private static void addShips(List<Entity> objects, int amountOfShips, List<Player> players, GameConfig gameConfiguration){
        Map<String, ShotType> shotTypes = gameConfiguration.getShotTypes();
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            double xPosition = ((800 / amountOfShips) * i)/2;
            Player player = players.get(i-1);
            Ship ship = new Ship(id,new Position(xPosition, 300), 180, 0,40, 40,0, new Weapon(shotTypes.get("shotType-"+i), System.currentTimeMillis()), player.getId(), false);
            objects.add(ship);
        }
    }
}
