package components;

public class Velocity {
    private double[] xyradiansVelocity;
    private double[] ballVelocity;

    public void setBallVelocity(double[] ballPosition, double[] vector){
        ballVelocity = originTranslation(ballPosition, vector);
    }

    public double[]getBallVelocity(){
        return ballVelocity;
    }


    private double[] originTranslation(double[]object, double[] vector){
        if (object.length != vector.length) {
            throw new IllegalArgumentException("Arrays must be of equal length");
        }
        
        double[] result = new double[object.length];
        for (int i = 0; i < object.length; i++) {
            result[i] = object[i] + vector[i];
        }
        return result;
    }
}
