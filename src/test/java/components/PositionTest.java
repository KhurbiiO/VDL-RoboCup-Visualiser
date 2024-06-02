package components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    private Position position;

    @BeforeEach
    public void setUp() {
        position = new Position();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0d, position.getX(), "Default X coordinate should be 0");
        assertEquals(0d, position.getY(), "Default Y coordinate should be 0");
        assertEquals(0d, position.getZ(), "Default Z coordinate should be 0");
    }

    @Test
    public void testSetX() {
        position.setX(10.0);
        assertEquals(10.0, position.getX(), "X coordinate should be set to 10.0");
    }

    @Test
    public void testSetY() {
        position.setY(20.0);
        assertEquals(20.0, position.getY(), "Y coordinate should be set to 20.0");
    }

    @Test
    public void testSetZ() {
        position.setZ(30.0);
        assertEquals(30.0, position.getZ(), "Z coordinate should be set to 30.0");
    }

    @Test
    public void testGetCoordinate() {
        double[] coordinates = position.getCoordinate();
        assertArrayEquals(new double[]{0d, 0d, 0d}, coordinates, "Default coordinates should be {0, 0, 0}");
        
        position.setX(10.0);
        position.setY(20.0);
        position.setZ(30.0);
        coordinates = position.getCoordinate();
        assertArrayEquals(new double[]{10.0, 20.0, 30.0}, coordinates, "Coordinates should be {10.0, 20.0, 30.0}");
    }

    @Test
    public void testGetGridVVector() {
        position.setX(0.0);
        position.setY(0.0);
        position.setZ(0.0);
        
        double[] gridVector = position.getGridVVector(200, 100, 400, 200);
        assertArrayEquals(new double[]{200.0, 100.0, 0.0}, gridVector, 0.001, "Grid vector should be {200.0, 100.0, 0.0}");
        
        position.setX(100.0);
        position.setY(50.0);
        gridVector = position.getGridVVector(200, 100, 400, 200);
        assertArrayEquals(new double[]{400.0, 200.0, 0.0}, gridVector, 0.001, "Grid vector should be {400.0, 0.0, 50.0}");


        position.setX(50.0);
        position.setY(25.0);
        gridVector = position.getGridVVector(200, 100, 400, 200);
        assertArrayEquals(new double[]{300.0, 150.0, 0.0}, gridVector, 0.001, "Grid vector should be {400.0, 0.0, 50.0}");
    }

}
