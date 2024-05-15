package components;
import java.util.Vector;

import javafx.scene.image.Image;


public class Team {
    private String teamName;
    private Vector<Robot> robots;
    private Image logo;
    
    private int score;

    public Team(String teamName, int teamSize, String logoImgPath, String robotImgPath){
        robots = new Vector<Robot>();
        for (int i = 0; i < teamSize; i++){
            robots.add(new Robot(robotImgPath));
        }
        this.teamName = teamName;
        this.score = 0;

        setLogo(logoImgPath); // Update logo
    }

    public int getScore() {
        return score;   
    }

    public String getTeamName() {
        return teamName;
    }

    public Image getLogo() {
        return logo;
    }


    public Robot getPlayer(int idx){
        return robots.get(idx);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLogo(String logoImgPath) {
        try {
            logo = new Image(getClass().getResource(logoImgPath).toExternalForm());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
