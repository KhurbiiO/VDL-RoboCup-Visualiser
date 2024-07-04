package components;

/**
 * The CoordsTransformation class provides methods to transform coordinates for robots, balls,
 * and obstacles using predefined transformation matrices.
 */
public class CoordsTransformation {
    // Transformation matrix for general coordinates (including robot coordinates)
    private double[][] transformationMatrix = {
        {-1, 0, 0},
        {0, -1, 0},
        {0, 0, 1}
    };

    // Transformation matrix for obstacle coordinates
    private double[][] obstacleTransformationMatrix = {
        {-1, 0},
        {0, -1}
    };

    private int RADIANS_INDEX = 2; // Index for the radians component in coordinate arrays

    /**
     * Transforms robot coordinates using the transformation matrix.
     * 
     * @param coords the original coordinates of the robot
     * @return the transformed coordinates
     */
    public double[] transformRobotCoords(double[] coords) {
        return xyRadianstransform(coords);
    }

    /**
     * Transforms ball coordinates using the transformation matrix.
     * 
     * @param coords the original coordinates of the ball
     * @return the transformed coordinates
     */
    public double[] transformBallCoords(double[] coords) {
        return xyzTransform(coords);
    }

    /**
     * Transforms obstacle coordinates using the obstacle transformation matrix.
     * 
     * @param coords the original coordinates of the obstacle
     * @return the transformed coordinates
     */
    public double[] transformObstacleCoords(double[] coords) {
        return xyTransform(coords);
    }

    /**
     * Transforms coordinates using the general transformation matrix.
     * 
     * @param xyz the original coordinates
     * @return the transformed coordinates
     */
    private double[] xyzTransform(double[] xyz) {
        int m = transformationMatrix.length;
        int n = transformationMatrix[0].length;
        int p = xyz.length;

        if (n != p) {
            throw new IllegalArgumentException("Matrix column count must be equal to vector length");
        }

        double[] result = new double[m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                result[i] += transformationMatrix[i][j] * xyz[j];
            }
        }
        return result;
    }

    /**
     * Transforms coordinates with an angle component (radians) using the general transformation matrix.
     * 
     * @param xyTheta the original coordinates with an angle component
     * @return the transformed coordinates with the angle adjusted
     */
    private double[] xyRadianstransform(double[] xyTheta) {
        int m = transformationMatrix.length;
        int n = transformationMatrix[0].length;
        int p = xyTheta.length;

        if (n != p) {
            throw new IllegalArgumentException("Matrix column count must be equal to vector length");
        }

        double[] result = new double[m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                result[i] += transformationMatrix[i][j] * xyTheta[j];
            }
        }
        result[RADIANS_INDEX] = (result[RADIANS_INDEX] + Math.PI) % (2 * Math.PI);
        return result;
    }

    /**
     * Transforms coordinates using the obstacle transformation matrix.
     * 
     * @param xy the original coordinates of the obstacle
     * @return the transformed coordinates
     */
    private double[] xyTransform(double[] xy) {
        int m = obstacleTransformationMatrix.length;
        int n = obstacleTransformationMatrix[0].length;
        int p = xy.length;

        if (n != p) {
            throw new IllegalArgumentException("Matrix column count must be equal to vector length");
        }

        double[] result = new double[m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                result[i] += obstacleTransformationMatrix[i][j] * xy[j];
            }
        }
        return result;
    }
}
