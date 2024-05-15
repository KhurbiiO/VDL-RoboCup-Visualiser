package components;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VelocityTest {
    Velocity velocity;

    @BeforeEach
    void setUp(){
        velocity = new Velocity();
    }

    @Test
    public void itDoesCorrectOriginTranslationForAnObject(){
        double[] ballPosition = {3, -2, 0.5};
        double[] velocityVector = {-0.000472, -0.000724, 0.001935};

        double[] expectedVelocity = {2.999528, -2.000724, 0.501935};

        velocity.setBallVelocity(ballPosition, velocityVector);

        double[] translatedVelocity = velocity.getBallVelocity();

        assertArrayEquals(expectedVelocity, translatedVelocity,0.0001);
    }

    @Test
    public void itThrowsAnExceptionWhenCoordsAndVelocityVectorsDoNotMatch(){
        double[] invalidBallPosition = {3, -2, 0.5, 10};
        double[] velocityVector = {-0.000472, -0.000724, 0.001935};

         assertThrows(IllegalArgumentException.class, () -> {
            velocity.setBallVelocity(invalidBallPosition, velocityVector);});
    }

    @Test
    public void itDoesCorrectOriginTranslationForRobots(){
        double[] robotPosition = {3, -2, 0.5};
        double[] velocityVector = {-0.000472, -0.000724, 0.001935};

        double[] expectedVector = {2.999528, -2.000724, 0.501935};

        velocity.setRobotVelocity(robotPosition, velocityVector);

        double[] translatedVector = velocity.getRobotVelocity();

        assertArrayEquals(expectedVector, translatedVector, 0.0001);
    }

    @Test
    public void itThrowsAnExceptionWhenPositionAndVectorsDoNotMatch(){
        double[] invalidRobotPosition = {3, -2, 0.5, 10};
        double[] velocityVector = {-0.000472, -0.000724, 0.001935};

        assertThrows(IllegalArgumentException.class, () -> {
            velocity.setBallVelocity(invalidRobotPosition, velocityVector);});
    }
}
