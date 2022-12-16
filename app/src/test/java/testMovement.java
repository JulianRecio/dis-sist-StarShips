import org.junit.BeforeClass;
import org.junit.Test;
import starShips.model.Entities.Asteroid;
import starShips.model.Entities.*;
import starShips.model.Enums.EntityType;
import starShips.model.Enums.ShotType;
import starShips.model.Position;

import static org.junit.Assert.assertEquals;

public class testMovement {
    static Asteroid asteroid;
    static Shot shot;
    static Ship ship;

    @BeforeClass
    public static void previousMove(){
        asteroid = new Asteroid("asteroid-1", new Position(200,200),  180, 0, 50,50, 100, true, 100);
        ship = new Ship("starship-1", new Position(400,400), 180,0, 40, 40, 0,new Weapon(ShotType.LASER, 0), "player-1", false);
        shot = new Shot("shot-1" , new Position(300,300), 180, 0 ,30,30, 0, "starship-1", ShotType.LASER,20);

    }

    @Test
    public void testAsteroid(){
        Asteroid newAsteroid = asteroid.update();
        assertEquals(newAsteroid.getEntityPosition().getX(), asteroid.getEntityPosition().getX() + 0.7 * Math.sin(Math.PI * 2 * asteroid.getTrajectory() / 360), 0);
        assertEquals(newAsteroid.getEntityPosition().getY(), asteroid.getEntityPosition().getY() + 0.7 * Math.sin(Math.PI * 2 * asteroid.getTrajectory() / 360), 0);

    }

    @Test
    public void testShot(){
        Shot newShot = shot.update();
        assertEquals(newShot.getEntityPosition().getX(), asteroid.getEntityPosition().getX() + 4 * Math.sin(Math.PI * 2 * shot.getTrajectory() / 360), 0);
        assertEquals(newShot.getEntityPosition().getY(), asteroid.getEntityPosition().getY() + 4 * Math.sin(Math.PI * 2 * shot.getTrajectory() / 360), 0);

    }

    @Test
    public void testShip(){
        Ship newShip = ship.update();
        assertEquals(newShip.getEntityPosition().getX(), asteroid.getEntityPosition().getX() + 4 * Math.sin(Math.PI * 2 * ship.getTrajectory() / 360), 0);
        assertEquals(newShip.getEntityPosition().getY(), asteroid.getEntityPosition().getY() + 4 * Math.sin(Math.PI * 2 * ship.getTrajectory() / 360), 0);

    }
}
