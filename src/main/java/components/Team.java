package components;

import components.controllers.*;

import java.io.File;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private String unicastAddr;        // The unicast IP range of the team
    private String multicastAddr;      // The multicast IP of the team
    private String logoPath;           // Relative path of the team logo
    private Vector<Robot> robots;      // A vector of Robot objects representing the team members
    private int teamScore;             // The score of the team
    private int teamSize;              // The size of the team (number of robots)
    private String teamColor1;          // The color of the team
    private String teamColor2;          // The color of the team
    private boolean isTeamB;           // Indicates if this team is Team B
    private boolean isHome;            // Indicates if team should use color 1

    public LabelController scoreDisplay;      // Controller for the score display label
    public Flag flagDisplay;                  // Controller for the team flag
    public Logo logoDisplay;                  // Controller for the team logo
    public ConIndactor connectionDisplay;     // Controller for the connection indicator

    /**
     * Constructor for Team.
     * Initializes the team properties and creates the robots.
     * 
     * @param isTeamB indicates if this team is Team B
     */
    public Team(Boolean isTeamB) {
        robots = new Vector<Robot>();
        this.teamScore = 0;

        this.scoreDisplay = new LabelController();
        this.logoDisplay = new Logo();
        this.flagDisplay = new Flag();
        this.connectionDisplay = new ConIndactor();
    }

    /**
     * Updates the team's properties based on the given JSON configuration.
     * 
     * @param jsonFilePath the path to the JSON configuration file
     * @throws Exception if there is an error reading the file (either IO or NullPointer)
     */
        public void updateFromJson(String jsonFilePath, boolean isHome) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            JsonNode networkingNode = rootNode.path("networking");
            this.unicastAddr = networkingNode.path("UnicastAddr").asText();
            this.multicastAddr = networkingNode.path("MulticastAddr").asText();

            JsonNode appearanceNode = rootNode.path("appearance");
            this.teamName = appearanceNode.path("longame24").asText();
            this.smallName = appearanceNode.path("shortname8").asText();
            this.logoPath = appearanceNode.path("logoPath").asText();
            this.teamSize = appearanceNode.path("size").asInt();
            this.teamColor1 = appearanceNode.path("colorOne").asText();  // Assuming colorOne is the primary team color
            this.teamColor2 = appearanceNode.path("colorTwo").asText();  // Assuming colorOne is the primary team color
            this.isHome = isHome;

            //init robots
            robots.clear();
            for (int i = 0; i < teamSize; i++) {
                robots.add(new Robot());
            }
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

    public void setIsTeamB(boolean isTeamB){
        this.isTeamB = isTeamB;
    }
    
    /**
     * Sets the team color and updates the visual representation of the robots and flag.
     * 
     * @param teamColor the new color of the team
     * @param playerStroke the stroke color for the robots
     * @param playerStrokeWidth the stroke width for the robots
     */
    public void loadTeam(String playerStroke, double playerStrokeWidth) {
        String teamColor = getTeamColor();
        for (int i = 0; i < this.teamSize; i++) {
            robots.get(i).setScreenColor(teamColor, playerStroke, playerStrokeWidth);
        }
        flagDisplay.setScreenColor(teamColor);
        logoDisplay.setImage(logoPath);
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
    public String getUnicastAddr() { return unicastAddr; }
    public String getMulticastAddr() { return multicastAddr; }
    public String getTeamName() { return teamName; }
    public String getSmallName() { return smallName; }
    public String getLogoPath() { return logoPath; }
    public int getTeamScore() { return teamScore; }
    public int getTeamSize() { return teamSize; }
    public String getTeamColor1() { return teamColor1; }
    public String getTeamColor2() { return teamColor2; }
    public boolean isTeamB() { return isTeamB; }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
        scoreDisplay.setScreenText(Integer.toString(teamScore));
    }

    public String getTeamColor(){
        if (isHome) return teamColor1;
        return teamColor2;
    }
}
