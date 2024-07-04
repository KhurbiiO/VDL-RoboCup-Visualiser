package components.communication;

import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.StopWatch;
import components.Team;
import components.controllers.LabelController;
import javafx.application.Platform;

/**
 * The RefClient class handles communication with the referee application, processes received commands, 
 * and updates the application state accordingly.
 */
public class RefClient extends TCPClient {
    // Command constants
    private final String COMM_START = "START";
    private final String COMM_STOP = "STOP";
    private final String COMM_RESET = "RESET";
    private final String COMM_SUBSTITUTION = "SUBSTITUTION";
    private final String COMM_GOAL = "GOAL";

    private LabelController refBoardController;
    private Team teamA;
    private Team teamB;
    private StopWatch stopWatch;

    /**
     * Constructor to initialize RefClient with necessary components.
     * 
     * @param port the port number to connect to
     * @param ipaddress the IP address to connect to
     * @param refBoardController the controller for updating the display board
     * @param teamA the first team
     * @param teamB the second team
     * @param stopWatch the stopwatch to manage game time
     */
    public RefClient(int port, String ipaddress, LabelController refBoardController, Team teamA, Team teamB, StopWatch stopWatch) {
        super(ipaddress, port);
        this.refBoardController = refBoardController;
        this.teamA = teamA;
        this.teamB = teamB;
        this.stopWatch = stopWatch;
    }

    /**
     * Called when data is received from the server.
     * 
     * @param data the data received from the server
     */
    @Override
    protected void onReceive(String data) {
        System.out.println(data);
        update(data);        
    }

    /**
     * Called when a timeout occurs.
     */
    @Override
    protected void onTimeout() {}

    /**
     * Processes the received data, parses the JSON, and updates the application state based on the command.
     * 
     * @param data the JSON formatted string containing the command and related data
     */
    public void update(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data);
            try {
                String command = jsonNode.get("command").asText();
                switch (command) {
                    case COMM_START:
                        System.out.println("Handling COMM_START event");
                        Platform.runLater(() -> stopWatch.startSW());
                        break;

                    case COMM_GOAL:
                        System.out.println("Handling COMM_GOAL event");
                        try {
                            String target = jsonNode.get("targetTeam").asText();
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
                            String target = jsonNode.get("targetTeam").asText();
                            Platform.runLater(() -> refBoardController.setScreenText(String.format("%s %s", command, getTeam(target))));
                        } catch (NoSuchElementException e) {
                            Platform.runLater(() -> refBoardController.setScreenText(command));
                        }
                }
            } catch (NoSuchElementException e) {}
        } catch (JsonProcessingException e) {}
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
