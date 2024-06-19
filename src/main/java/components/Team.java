package components;

import java.util.Vector;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Team{
    private String teamName;
    private Vector<Robot> robots;    
    private int teamScore;
    private int teamSize;
    private String teamColor;
    private boolean isTeamB;
    private String teamIPAddress;
    public LabelController scoreDisplay;
    public PaneController flagDisplay;
    public PaneController logoDisplay;
    public CircleController connectionDisplay;

    public Team(String teamName, int teamSize, String teamColor, String teamIPAddress, Boolean isTeamB){
        robots = new Vector<Robot>();
        for (int i = 0; i < teamSize; i++){
            robots.add(new Robot());
        }
        this.teamName = teamName;
        this.teamScore = 0;
        this.teamColor = teamColor;
        this.teamSize = teamSize;
        this.isTeamB = isTeamB;
        this.scoreDisplay = new LabelController();
        this.logoDisplay = new PaneController();
        this.flagDisplay = new PaneController();
        this.teamIPAddress = teamIPAddress;
        this.connectionDisplay = new CircleController();
    }

    public void bindProperties(Pane logoDisplay, Pane flagDisplay, Label scoreDisplay, Circle connectionDisplay){
        this.logoDisplay.bindPane(logoDisplay);
        this.flagDisplay.bindPane(flagDisplay);
        this.scoreDisplay.bindLabel(scoreDisplay);
        this.connectionDisplay.bindCircle(connectionDisplay);
    }

    public Robot getPlayer(int idx){
        return robots.get(idx);
    }

    public String getTeamIPAddress() {
        return teamIPAddress;
    }
    public String getTeamName() {
        return teamName;
    }
    public int getTeamScore() {
        return teamScore;   
    }
    public int getTeamSize() {
        return teamSize;
    }
    public String getTeamColor() {
        return teamColor;
    }
    public boolean isTeamB(){
        return isTeamB;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
    public void setTeamIPAddress(String teamIPAddress) {
        this.teamIPAddress = teamIPAddress;
    }
    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
        for (int i = 0; i < teamSize; i++){
            robots.get(i).setScreenColor(teamColor, "#ffffff", 2);
        }
        flagDisplay.setScreenColor(teamColor);
    }
}
