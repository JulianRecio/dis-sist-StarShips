package starShips;

import starShips.model.Entities.Entity;
import starShips.model.Player;

import java.util.List;

public class GameState {
    private final List<Entity> entities;
    private final List<Player> players;


    public GameState(List<Entity> entities, List<Player> players) {
        this.entities = entities;
        this.players = players;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
