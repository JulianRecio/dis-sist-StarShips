package starShips.model;

public class Player {
    private final String id;
    private final int points;
    private final int lives;
    private final String playerShip;

    public Player(String id, int points, int lives, String playerShip) {
        this.id = id;
        this.points = points;
        this.lives = lives;
        this.playerShip = playerShip;
    }

    public Player(String id, int lives, String playerShip) {
        this.id = id;
        this.points = 0;
        this.lives = lives;
        this.playerShip = playerShip;
    }

    public Player getNewPlayer(){
        return new Player(id, points, lives, playerShip);
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public String getPlayerShip() {
        return playerShip;
    }
}
