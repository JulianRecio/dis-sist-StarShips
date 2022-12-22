import org.junit.BeforeClass;
import org.junit.Test;
import starShips.model.Entities.Asteroid;
import starShips.model.Entities.*;
import starShips.model.Enums.Color;
import starShips.model.Enums.ShotType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testMovement {
    static Asteroid asteroid;
    static Shot shot;
    static Ship ship;

    @BeforeClass
    public static void previousMove(){
        asteroid = new Asteroid("asteroid-1", 300,300,  180, 50,50, 180,100 ,true);
        shot = new Shot("shot-1" , 200, 200,180,30,30,180,Color.RED, "starship-1", ShotType.LASER, 100);
        ship = new Ship("starship-1", 400,400, 180, 40, 40,180, Color.RED, "player-1", ShotType.LASER,50, System.currentTimeMillis());
    }

    @Test
    public void testAsteroid(){
        Asteroid newAsteroid = asteroid.update();
        assertEquals(newAsteroid.getX(), asteroid.getX() + 0.7 * Math.sin(Math.PI * 2 * asteroid.getTrajectory() / 360), 0);
        assertEquals(newAsteroid.getY(), asteroid.getY() + 0.7 * Math.sin(Math.PI * 2 * asteroid.getTrajectory() / 360), 0);

    }

    @Test
    public void testShot(){
        Shot newShot = shot.update();
        assertEquals(newShot.getX(), shot.getX() + 4 * Math.sin(Math.PI * 2 * shot.getTrajectory() / 360), 0);
        assertEquals(newShot.getY(), shot.getY() + 4 * Math.sin(Math.PI * 2 * shot.getTrajectory() / 360), 0);

    }

    @Test
    public void testShip(){
        Ship newShip = ship.update();
        assertEquals(newShip.getX(), ship.getX() + 4 * Math.sin(Math.PI * 2 * ship.getTrajectory() / 360), 0);
        assertEquals(newShip.getY(), ship.getY() + 4 * Math.sin(Math.PI * 2 * ship.getTrajectory() / 360), 0);

    }

    @Test
    public void testShipAccelerate(){
        Ship updated = ship.move(true);
        assertEquals(updated.getSpeed(), ship.getSpeed()+70, 0);
        updated = ship.move(false);
        assertTrue(updated.getSpeed() < 0);
    }
}
