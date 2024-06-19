package components;

public class Ball extends CircleController{
    public Position currentPosition;
    private double velocity;
    private double confidence;

    public Ball(){
        super();
        currentPosition = new Position();
        velocity = 0d;
        confidence = 0d;
    }

    public double getVelocity() {
        return velocity;
    }
    public double getConfidence() {
        return confidence;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    public void setCurrentPosition(double newX, double newY, double newZ, double digitalHeight, double digitalWidth) {
        currentPosition.setX(newX);
        currentPosition.setY(newY);
        currentPosition.setZ(newZ);

        updateScreenCoordinate(currentPosition.getGridHVector(newX, newY, digitalWidth, digitalHeight), 1);
    }
}
