package components;

public class Ball {
    public Position currentPosition;
    private double velocity;
    private float confidence;
    

    public Ball(){
        currentPosition = new Position();
        velocity = 0d;
        confidence = 0f;
    }

    public double getVelocity() {
        return velocity;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

}
