package components;
import javafx.scene.image.Image;

import components.Coordinate;


public class Robot {
    public Position currentPosition;
    public Position targetPosition;
    private float currentVelocity;
    private float batterPercentage;
    private boolean ballEngaged;
    private String intention;
    private Image icon;

    public int getId(){
        return matchId;
    }

    public Robot(int match_id){
        matchId = match_id;
        coordinate = new Coordinate(null, null, null, null);
        try {
            icon = new Image(getClass().getResource(imgPath).toExternalForm());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public float getCurrentVelocity() {
        return currentVelocity;
    }

    public float getBatterPercentage() {
        return batterPercentage;
    }

    public String getIntention() {
        return intention;
    }

    public boolean getBallEngaged(){
        return ballEngaged;
    }

    public Image getIcon(){
        return icon;
    }


    public void setCurrentPosition(double newX, double newY, double newZ) {
        currentPosition.setX(newX);
        currentPosition.setY(newY);
        currentPosition.setZ(newZ);
    }

    public void setTargetPosition(double newX, double newY, double newZ) {
        targetPosition.setX(newX);
        targetPosition.setY(newY);
        targetPosition.setZ(newZ);
    }

    public void setCurrentVelocity(float currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public void setBatterPercentage(float batterPercentage) {
        this.batterPercentage = batterPercentage;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public void setBallEngaged(boolean ballEngaged) {
        this.ballEngaged = ballEngaged;
    }
}
