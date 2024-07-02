package components;

import components.controllers.*;

import java.util.Vector;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Team{
    private String teamName;
    private String smallName;
    private Vector<Robot> robots;    
    private int teamScore;
    private int teamSize;
    private String teamColor;
    private boolean isTeamB;
    private String teamIPAddress;
    public LabelController scoreDisplay;
    public Flag flagDisplay;
    public Logo logoDisplay;
    public ConIndactor connectionDisplay;
    

    public Team(String teamName, String smallName, int teamSize, String teamColor, String teamIPAddress, Boolean isTeamB){
        robots = new Vector<Robot>();
        for (int i = 0; i < teamSize; i++){
            robots.add(new Robot());
        }
        this.teamName = teamName;
        this.smallName = smallName;
        this.teamScore = 0;
        this.teamColor = teamColor;
        this.teamSize = teamSize;
        this.isTeamB = isTeamB;
        this.scoreDisplay = new LabelController();
        this.logoDisplay = new Logo();
        this.flagDisplay = new Flag();
        this.connectionDisplay = new ConIndactor();
        this.teamIPAddress = teamIPAddress;
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
    public String getSmallName() {
        return smallName;
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
    public void setSmallName(String smallName) {
        this.smallName = smallName;
    }
    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
        scoreDisplay.setScreenText(Integer.toString(teamScore));
    }
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
    public void setTeamIPAddress(String teamIPAddress) {
        this.teamIPAddress = teamIPAddress;
    }
    public void setTeamColor(String teamColor, String stroke, int strokeWidth) {
        this.teamColor = teamColor;
        for (int i = 0; i < teamSize; i++){
            robots.get(i).setScreenColor(teamColor, stroke, strokeWidth);
        }
        flagDisplay.setScreenColor(teamColor);
    }
}
