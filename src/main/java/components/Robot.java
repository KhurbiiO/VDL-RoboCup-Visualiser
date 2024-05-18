package components;
import javafx.scene.image.Image;

import components.Coordinate;


public class Robot {
    private int matchId;
    public Coordinate coordinate;
    private Image icon;

    public int getId(){
        return matchId;
    }

    public Robot(int match_id){
        matchId = match_id;
        coordinate = new Coordinate(null, null, null, null);
        try {
            icon = new Image(getClass().getResource("/media/robot.png").toExternalForm());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Image getIcon(){
        return icon;
    }
}
