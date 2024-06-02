package components;

import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    private Robot robot;

    @BeforeEach
    public void setUp() {
        robot = new Robot("/media/robot.png");
    }

    @Test
    public void testConstructor() {
        assertNotNull(robot.currentPosition, "Current Position should not be null");
        assertNotNull(robot.targetPosition, "Target Position should not be null");
        assertNotNull(robot.getCurrentVelocity(), "Current Velocity should not be null");
        assertEquals(100.0, robot.getBatterPercentage(), "Battery Percentage should be 100");
        assertFalse(robot.getBallEngaged(), "Ball Engaged should be false by default");
        assertEquals("NULL", robot.getIntention(), "Intention should be 'NULL' by default");
        // assertNotNull(robot.getIcon(), "Icon should not be null");
    }

    @Test
    public void testSetCurrentPosition() {
        robot.setCurrentPosition(10.0, 20.0, 30.0);
        assertEquals(10.0, robot.currentPosition.getX(), "Current X position should be 10.0");
        assertEquals(20.0, robot.currentPosition.getY(), "Current Y position should be 20.0");
        assertEquals(30.0, robot.currentPosition.getZ(), "Current Z position should be 30.0");
    }

    @Test
    public void testSetTargetPosition() {
        robot.setTargetPosition(40.0, 50.0, 60.0);
        assertEquals(40.0, robot.targetPosition.getX(), "Target X position should be 40.0");
        assertEquals(50.0, robot.targetPosition.getY(), "Target Y position should be 50.0");
        assertEquals(60.0, robot.targetPosition.getZ(), "Target Z position should be 60.0");
    }

    @Test
    public void testSetCurrentVelocity() {
        double[] newVelocity = {1.0, 2.0, 3.0};
        robot.setCurrentVelocity(newVelocity);
        assertArrayEquals(newVelocity, robot.getCurrentVelocity(), "Current Velocity should be {1.0, 2.0, 3.0}");
    }

    @Test
    public void testSetBatterPercentage() {
        robot.setBatterPercentage(75.0);
        assertEquals(75.0, robot.getBatterPercentage(), "Battery Percentage should be 75.0");
    }

    @Test
    public void testSetIntention() {
        robot.setIntention("Move Forward");
        assertEquals("Move Forward", robot.getIntention(), "Intention should be 'Move Forward'");
    }

    @Test
    public void testSetBallEngaged() {
        robot.setBallEngaged(true);
        assertTrue(robot.getBallEngaged(), "Ball Engaged should be true");
    }

    // @Test
    // public void testSetIconValidPath() {
    //     robot.setIcon("/media/robot.png");
    //     assertNotNull(robot.getIcon(), "Icon should not be null after setting a valid path");
    // }

    // @Test
    // public void testSetIconInvalidPath() {
    //     robot.setIcon("/media/nonexistent.png");
    //     // Expecting the icon to be null or an error to be handled
    //     assertNull(robot.getIcon(), "Icon should be null after setting an invalid path");
    // }

}
