package components.communication;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.Ball;
import components.CoordsTransformation;
import components.Robot;
import components.StopWatch;
import components.Team;
import components.controllers.LabelController;
import components.Field;
import javafx.application.Platform;

/**
 * The BroadCast class handles communication with the websocket server, processes received data, 
 * and updates the field, robots, and ball positions.
 */
public class BroadCast extends UDPClient {
    // Command constants
    private final String COMM_START = "START";
    private final String COMM_STOP = "STOP";
    private final String COMM_RESET = "RESET";
    private final String COMM_SUBSTITUTION = "SUBSTITUTION";
    private final String COMM_GOAL = "GOAL";

    private final String BC_TEAMS = "teams";
    private final String BC_EVENT = "event";

    private Team teamA;    
    private Team teamB;  
    private Ball ball;
    private Field field;
    private CoordsTransformation transform;    
    private LabelController refBoardController;
    private StopWatch stopWatch;

    private double fieldWidth;
    private double fieldHeight;

    /**
     * Constructor to initialize WSClient with necessary components.
     * 
     * @param port the port number to connect to
     * @param ipaddress the IP address to connect to
     * @param teamA the teamA object containing robots
     * @param teamB the teamB object containing robots
     * @param ball the ball object
     * @param field the field object
     * @param transform the coordinate transformation utility
     */
    public BroadCast(int port, String ipaddress, Team teamA, Team teamB, Ball ball, Field field, CoordsTransformation transform, StopWatch stopWatch, LabelController refBoardController) {
        super(port, ipaddress);
        this.teamA = teamA;
        this.teamB = teamB;
        this.ball = ball;
        this.field = field;
        this.transform = transform;
        this.stopWatch = stopWatch;
        this.refBoardController = refBoardController;

        // Calculate field dimensions
        fieldWidth = field.getA() + field.getL() * 2 + field.getM();
        fieldHeight = field.getB() + field.getL() * 2;
    }

    /**
     * Called when data is received from the server.
     * 
     * @param data the data received from the server as a byte array
     * @param ip the IP address of the sender
     * @param port the port number of the sender
     */
    @Override
    protected void onReceive(byte[] data, String ip, int port) {
        String json = new String(data, 0, data.length);
        updateField(json);
    }

    /**
     * Called when a timeout occurs.
     */
    @Override
    protected void onTimeout() {}

    /**
     * Starts or stops listening for data.
     * 
     * @param on whether to start or stop listening
     */
    @Override
    public void listen(boolean on) {
        super.listen(on);
    }

    /**
     * Updates the field, robots, and ball positions based on the received JSON data.
     * 
     * @param data the JSON formatted string containing the world state
     */
    private void updateField(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data);

            switch(jsonNode.get("type").asText()){
                case(BC_EVENT): {
                    try {
                        String command = jsonNode.get("eventCode").asText();
                        switch (command) {
                            case COMM_START:
                                System.out.println("Handling COMM_START event");
                                Platform.runLater(() -> stopWatch.startSW());
                                break;
        
                            case COMM_GOAL:
                                System.out.println("Handling COMM_GOAL event");
                                try {
                                    String target = jsonNode.get("team").asText();
                                    if (target.startsWith(teamA.getTeamIPAddress())) {
                                        teamA.setTeamScore(0); // Reset score (if needed)
                                        Platform.runLater(() -> {
                                            refBoardController.setScreenText(String.format("%s Scored!", teamA.getTeamName()));
                                            teamA.setTeamScore(teamA.getTeamScore() + 1);
                                        });
                                    } else if (target.startsWith(teamB.getTeamIPAddress())) {
                                        Platform.runLater(() -> {
                                            refBoardController.setScreenText(String.format("%s Scored!", teamB.getTeamName()));
                                            teamB.setTeamScore(teamB.getTeamScore() + 1);
                                        });
                                    }
                                } catch (NoSuchElementException e) {}
                                break;
        
                            case COMM_STOP:
                                System.out.println("Handling COMM_STOP event");
                                Platform.runLater(() -> stopWatch.stopSW());
                                break;
        
                            case COMM_RESET:
                                System.out.println("Handling COMM_RESET event");
                                Platform.runLater(() -> {
                                    stopWatch.resetStopWatch();
                                    teamA.setTeamScore(0);
                                    teamB.setTeamScore(0);
                                });
                                break;
        
                            case COMM_SUBSTITUTION:
                                System.out.println("Handling COMM_SUBSTITUTION event");
                                break;
        
                            default:
                                System.out.println("Handling event");
                                try {
                                    String target = jsonNode.get("team").asText();
                                    Platform.runLater(() -> refBoardController.setScreenText(String.format("%s %s", command, getTeam(target))));
                                } catch (NoSuchElementException e) {
                                    Platform.runLater(() -> refBoardController.setScreenText(command));
                                }
                        }
                    } catch (NoSuchElementException e) {}
                }

                case (BC_TEAMS): {
                    //TODO: Clean code.
                    Team team = teamA;
                    // Update robots
                    Iterator<JsonNode> robots = jsonNode.get("teamA").get("worldState").get("robots").elements();
                    for (int i = 0; i < team.getTeamSize(); i++) {
                        try {
                            JsonNode robot = robots.next();
                            Robot player = team.getPlayer(i);

                            player.setScreenOpacity(1); // Visually enable robot

                            player.setBallEngaged(robot.get("ballEngaged").asBoolean());
                            player.setBatterPercentage(robot.get("batteryLevel").asDouble());
                            player.setIntention(robot.get("intention").asText());

                            // Get current pose of the robot
                            Iterator<JsonNode> currentPose_ = robot.get("pose").elements();
                            final double[] currentPose = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};

                            // Get target pose of the robot
                            Iterator<JsonNode> targetPose_ = robot.get("target pose").elements();
                            final double[] targetPose = {targetPose_.next().asDouble(), targetPose_.next().asDouble(), targetPose_.next().asDouble()};

                            final double[] transformedCurrentPose = currentPose;
                            final double[] transformedTargetPose = targetPose;

                            // Update the player's current and target positions on the JavaFX application thread
                            Platform.runLater(() -> player.setCurrentPosition(transformedCurrentPose[0], transformedCurrentPose[1], transformedCurrentPose[2], fieldHeight, fieldWidth, field.getNode().getPrefHeight(), field.getNode().getPrefWidth()));
                            player.setTargetPosition(transformedTargetPose[0], transformedTargetPose[1], transformedTargetPose[2]);
                        } catch (NoSuchElementException e) {
                            // Visually disable the rest of the robots if there are fewer robots than expected
                            for (int j = i; j < team.getTeamSize(); j++) {
                                team.getPlayer(i).setScreenOpacity(0);
                            }
                            break;
                        }
                    }

                    team = teamB;
                    robots = jsonNode.get("teamB").get("worldState").get("robots").elements();
                    for (int i = 0; i < team.getTeamSize(); i++) {
                        try {
                            JsonNode robot = robots.next();
                            Robot player = team.getPlayer(i);

                            player.setScreenOpacity(1); // Visually enable robot

                            player.setBallEngaged(robot.get("ballEngaged").asBoolean());
                            player.setBatterPercentage(robot.get("batteryLevel").asDouble());
                            player.setIntention(robot.get("intention").asText());

                            // Get current pose of the robot
                            Iterator<JsonNode> currentPose_ = robot.get("pose").elements();
                            final double[] currentPose = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};

                            // Get target pose of the robot
                            Iterator<JsonNode> targetPose_ = robot.get("target pose").elements();
                            final double[] targetPose = {targetPose_.next().asDouble(), targetPose_.next().asDouble(), targetPose_.next().asDouble()};

                            final double[] transformedCurrentPose = transform.transformRobotCoords(currentPose);;
                            final double[] transformedTargetPose = transform.transformRobotCoords(targetPose);

                            // Update the player's current and target positions on the JavaFX application thread
                            Platform.runLater(() -> player.setCurrentPosition(transformedCurrentPose[0], transformedCurrentPose[1], transformedCurrentPose[2], fieldHeight, fieldWidth, field.getNode().getPrefHeight(), field.getNode().getPrefWidth()));
                            player.setTargetPosition(transformedTargetPose[0], transformedTargetPose[1], transformedTargetPose[2]);
                        } catch (NoSuchElementException e) {
                            // Visually disable the rest of the robots if there are fewer robots than expected
                            for (int j = i; j < team.getTeamSize(); j++) {
                                team.getPlayer(i).setScreenOpacity(0);
                            }
                            break;
                        }
                    }

                    // Update ball
                    try {
                        Iterator<JsonNode> ballsA = jsonNode.get("teamA").get("worldState").get("balls").elements();
                        Iterator<JsonNode> ballsB = jsonNode.get("teamB").get("worldState").get("balls").elements();

                        try {
                            //TODO: Seperate Try catch (for cases where team doesn't have ball data)
                            JsonNode ballA_ = ballsA.next(); // List is in order of confidence
                            JsonNode ballB_ = ballsB.next(); // List is in order of confidence
                            JsonNode ball_ = (ballA_.get("confidence").asDouble() > ballB_.get("confidence").asDouble()) ? ballA_ : ballB_;


                            Iterator<JsonNode> currentPose_ = ball_.get("position").elements();
                            final double[] currentPose = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};
                            ball.setConfidence(ball_.get("confidence").asDouble());

                            final double[] transformedCurrentPose;

                            // Transform coordinates if the team is team B
                            if (team.isTeamB()) {
                                transformedCurrentPose = transform.transformBallCoords(currentPose);
                            } else {
                                transformedCurrentPose = currentPose;
                            }

                            // Update the ball's current position on the JavaFX application thread
                            Platform.runLater(() -> ball.setCurrentPosition(transformedCurrentPose[0], transformedCurrentPose[1], transformedCurrentPose[2], fieldHeight, fieldWidth, field.getNode().getPrefHeight(), field.getNode().getPrefWidth()));
                        } catch (NoSuchElementException e) {}
                    } catch (NoSuchElementException e) {}
                        }

                    }
            
        } catch (JsonProcessingException | NullPointerException e) {}
    }

    /**
     * Helper method to get the team name based on the IP address.
     * 
     * @param ipaddress the IP address to identify the team
     * @return the name of the team
     */
    private String getTeam(String ipaddress) {
        if (ipaddress.startsWith(teamA.getTeamIPAddress())) {
            return teamA.getTeamName();
        }
        return teamB.getTeamName();
    }
}
