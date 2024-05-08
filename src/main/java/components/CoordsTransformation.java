package components;

public class CoordsTransformation {
    private double[][] transformationMatrix = {
        {-1, 0, 0},
        {0, -1, 0},
        {0, 0, 1}
    };

    private int RADIANS_INDEX = 2;

    public double[] transformRobotCoords(double[] coords){
        double[] transformedCoords = xyRadianstransform(coords);
        return transformedCoords;
    }

    public double[] transformBallCoords(double[] coords){
        double[] transformedCoords = xyzTransform(coords);
        return transformedCoords;
    }

    private double[] xyzTransform(double[] xyz){
        int m = transformationMatrix.length;
        int n = transformationMatrix[0].length;
        int p = xyz.length;

        if (n != p){
            throw new IllegalArgumentException("Matrix column count must be equal to vector length");
        }

        double[] result = new double[m];

        for (int i = 0; i < m; i++){
            for(int j = 0; j < p; j++){
                result[i] += transformationMatrix[i][j] * xyz[j];
            }
        }
        return result;
    }

    private double[] xyRadianstransform(double[] xyTheta){
        int m = transformationMatrix.length;
        int n = transformationMatrix[0].length;
        int p = xyTheta.length;

        if (n != p){
            throw new IllegalArgumentException("Matrix column count must be equal to vector length");
        }

        double[] result = new double[m];

        for (int i = 0; i < m; i++){
            for(int j = 0; j < p; j++){
                result[i] += transformationMatrix[i][j] * xyTheta[j];
            }
        }
        result[RADIANS_INDEX] = (result[RADIANS_INDEX]+Math.PI)%(2 * Math.PI);
        return result;
    }
}
