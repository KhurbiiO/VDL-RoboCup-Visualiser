package components;

    
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoordsTransformationTest {

    CoordsTransformation coordstransformation;

    @BeforeEach
    void setUp(){
        coordstransformation = new CoordsTransformation();
    }
    
    @Test
    public void givenRobotPosition_whenCalculatePosition_thenReturnTransformedPosition(){
        double[] originalPositionB = {3, -2, 0.5};

        double[] actualPositionB = {-3, 2, 3.6416};
        
        double[] transformedPositionB = coordstransformation.transformRobotCoords(originalPositionB);

        assertArrayEquals(actualPositionB, transformedPositionB, 0.0001);
    }

    @Test
    public void givenBallPosition_whenCalculatePosition_thenReturnTransformedPosition(){
        //Arrange
        double[] originalPositionB = {3, -2, 0.5};

        double[] actualPositionB = {-3, 2, 0.5};

        double[] transformedPositionB = coordstransformation.transformBallCoords(originalPositionB);
        
        assertArrayEquals(actualPositionB, transformedPositionB, 0.0001);
    }

    @Test
    public void givenInvalidRobotPosition_thenThrowException(){
        double[] invalidRobotPosition = {3,3,3,3};
        assertThrows(IllegalArgumentException.class, () -> {
            coordstransformation.transformRobotCoords(invalidRobotPosition);});
    }

    
    @Test
    public void givenInvalidBallPosition_thenThrowException(){
        double[] invalidBallPosition = {3,3,3,3};
        assertThrows(IllegalArgumentException.class, () -> {
            coordstransformation.transformBallCoords(invalidBallPosition);
        });
    }

}
