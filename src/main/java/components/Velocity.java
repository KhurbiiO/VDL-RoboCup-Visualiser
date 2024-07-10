package components;

public class Velocity {
    private double[] robotVelocity;
    private double[] ballVelocity;
    private double[] obstacleVelocity;

    private int RADIANS_INDEX = 2;

    public void setBallVelocity(double[] ballPosition, double[] vector){
        ballVelocity = originTranslation(ballPosition, vector);
    }

    public double[]getBallVelocity(){
        return ballVelocity;
    }

    public void setRobotVelocity(double[] robotPosition, double[] vector){
        robotVelocity = originTranslationRadians(robotPosition, vector);
    }

    public double[] getRobotVelocity(){
        return robotVelocity;
    }

    public void setObstacleVelocity(double[] obstaclePosition, double[] vector){
        obstacleVelocity = origintranslationObstacle(obstaclePosition, vector);
    }

    public double[] getObstacleVelocity(){
        return obstacleVelocity;
    }

      
    private double[] origintranslationObstacle(double[] obstacle, double[] vector) throws IllegalArgumentException{
        if(obstacle.length != vector.length){
            throw new IllegalArgumentException("Arrays must be of equal length");
        }
        double[] result = new double[obstacle.length];
        for (int i = 0; i < obstacle.length; i++){
            result[i] = obstacle[i] + vector[i];
        }
        return result;
    }

    private double[] originTranslationRadians(double[] robot, double[] vector) throws IllegalArgumentException{
        if (robot.length != vector.length) {
            throw new IllegalArgumentException("Arrays must be of equal length");
        }
        double[] result = new double[robot.length];
        for (int i = 0; i < RADIANS_INDEX; i++){
            result[i] = robot[i] + vector[i];
        }
        
        double angle = robot[RADIANS_INDEX] + vector[RADIANS_INDEX];
        angle = (angle + 2 * Math.PI) % (2 * Math.PI); 

        result[RADIANS_INDEX] = angle;

        return result;
    }

    private double[] originTranslation(double[]object, double[] vector) throws IllegalArgumentException{
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
