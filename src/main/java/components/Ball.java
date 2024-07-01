package components;

import components.controllers.CircleController;

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

    public void setCurrentPosition(double newX, double newY, double newZ, double realHeight, double realWidth, double digitalHeight, double digitalWidth) {
        currentPosition.setCoordinate(newX, newY, newZ);
        updateVisuals(realHeight, realWidth, digitalHeight, digitalWidth);
    }

    public void updateVisuals(double realHeight, double realWidth, double digitalHeight, double digitalWidth){
        updateScreenCoordinate(currentPosition.getGridVVector(realWidth, realHeight, digitalWidth, digitalHeight), 1);
    }
}
