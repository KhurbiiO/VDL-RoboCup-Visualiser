package components;

import javafx.scene.image.Image;

import components.Position;

public class Ball {
    public Position currentPosition;
    private double velocity;
    private double confidence;
    private Image icon;
    

    public Ball(){
        currentPosition = new Position();
        velocity = 0d;
        confidence = 0d;

        setIcon("/media/ball.png");
    }

    public double getVelocity() {
        return velocity;
    }

    public double getConfidence() {
        return confidence;
    }

    public Image getIcon(){
        return icon;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setIcon(String iconImgPath){
        try {
            icon = new Image(getClass().getResource(iconImgPath).toExternalForm());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
