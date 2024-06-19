package components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {

    private Ball ball;

    @BeforeEach
    public void setUp() {
        ball = new Ball();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(ball.currentPosition, "Position should not be null");
        assertEquals(0d, ball.getVelocity(), "Default velocity should be 0");
        assertEquals(0d, ball.getConfidence(), "Default confidence should be 0");
        // assertNotNull(ball.getIcon(), "Icon should not be null");
    }

    @Test
    public void testSetVelocity() {
        ball.setVelocity(10.5);
        assertEquals(10.5, ball.getVelocity(), "Velocity should be set to 10.5");
    }

    @Test
    public void testSetConfidence() {
        ball.setConfidence(0.85);
        assertEquals(0.85, ball.getConfidence(), "Confidence should be set to 0.85");
    }

    // @Test
    // public void testSetIconValidPath() {
    //     ball.setIcon("/media/ball.png");
    //     assertNotNull(ball.getIcon(), "Icon should not be null after setting a valid path");
    // }

    // @Test
    // public void testSetIconInvalidPath() {
    //     ball.setIcon("/media/nonexistent.png");
    //     // Expecting the icon to be null or an error to be handled
    //     assertNull(ball.getIcon(), "Icon should be null after setting an invalid path");
    // }

}
