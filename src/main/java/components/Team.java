package components;

import components.controllers.*;

import java.util.Vector;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * The Team class represents a team of robots in a JavaFX application. It includes methods to manage
 * the team's properties, such as name, score, size, color, and IP address. It also includes methods
 * to bind visual components like logos, flags, and score displays.
 */
public class Team {
    private String teamName;           // The full name of the team
    private String smallName;          // The short name of the team
    private Vector<Robot> robots;      // A vector of Robot objects representing the team members
    private int teamScore;             // The score of the team
    private int teamSize;              // The size of the team (number of robots)
    private String teamColor;          // The color of the team
    private boolean isTeamB;           // Indicates if this team is Team B
    private String teamIPAddress;      // The IP address of the team
    public LabelController scoreDisplay;      // Controller for the score display label
    public Flag flagDisplay;                  // Controller for the team flag
    public Logo logoDisplay;                  // Controller for the team logo
    public ConIndactor connectionDisplay;     // Controller for the connection indicator

    /**
     * Constructor for Team.
     * Initializes the team properties and creates the robots.
     * 
     * @param teamName the full name of the team
     * @param smallName the short name of the team
     * @param teamSize the number of robots in the team
     * @param teamColor the color of the team
     * @param teamIPAddress the IP address of the team
     * @param isTeamB indicates if this team is Team B
     */
    public Team(String teamName, String smallName, int teamSize, String teamColor, String teamIPAddress, Boolean isTeamB) {
        robots = new Vector<Robot>();
        for (int i = 0; i < teamSize; i++) {
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

    /**
     * Binds the visual components (logo, flag, score display, connection indicator) to the team.
     * 
     * @param logoDisplay the Pane for the team logo
     * @param flagDisplay the Pane for the team flag
     * @param scoreDisplay the Label for the team score
     * @param connectionDisplay the Circle for the connection indicator
     */
    public void bindProperties(Pane logoDisplay, Pane flagDisplay, Label scoreDisplay, Circle connectionDisplay) {
        this.logoDisplay.bindPane(logoDisplay);
        this.flagDisplay.bindPane(flagDisplay);
        this.scoreDisplay.bindLabel(scoreDisplay);
        this.connectionDisplay.bindCircle(connectionDisplay);
    }

    /**
     * Gets the robot at the specified index.
     * 
     * @param idx the index of the robot
     * @return the Robot object at the specified index
     */
    public Robot getPlayer(int idx) {
        return robots.get(idx);
    }

    // Getter methods for team properties
    public String getTeamIPAddress() { return teamIPAddress; }
    public String getTeamName() { return teamName; }
    public String getSmallName() { return smallName; }
    public int getTeamScore() { return teamScore; }
    public int getTeamSize() { return teamSize; }
    public String getTeamColor() { return teamColor; }
    public boolean isTeamB() { return isTeamB; }

    // Setter methods for team properties
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public void setSmallName(String smallName) { this.smallName = smallName; }
    
    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
        scoreDisplay.setScreenText(Integer.toString(teamScore));
    }

    public void setTeamSize(int teamSize) { this.teamSize = teamSize; }
    public void setTeamIPAddress(String teamIPAddress) { this.teamIPAddress = teamIPAddress; }

    /**
     * Sets the team color and updates the visual representation of the robots and flag.
     * 
     * @param teamColor the new color of the team
     * @param stroke the stroke color for the robots
     * @param strokeWidth the stroke width for the robots
     */
    public void setTeamColor(String teamColor, String stroke, int strokeWidth) {
        this.teamColor = teamColor;
        for (int i = 0; i < teamSize; i++) {
            robots.get(i).setScreenColor(teamColor, stroke, strokeWidth);
        }
        flagDisplay.setScreenColor(teamColor);
    }
}
