package components.communication;

import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.StopWatch;
import components.Team;
import components.controllers.LabelController;
import javafx.application.Platform;

public class RefClient extends TCPClient{
    private final String COMM_START = "START";
    private final String COMM_STOP = "STOP";
    private final String COMM_RESET = "RESET";
    private final String COMM_SUBSTITUTION = "SUBSTITUTION";
    private final String COMM_GOAL = "GOAL";

    private LabelController refBoardController;
    private Team teamA;
    private Team teamB;
    private StopWatch stopWatch;


    public RefClient(int port, String ipaddress, LabelController refBoardController, Team teamA, Team teamB, StopWatch stopWatch){
        super(ipaddress, port);
        this.refBoardController = refBoardController;
        this.teamA = teamA;
        this.teamB = teamB;
        this.stopWatch = stopWatch;
    }

    @Override
    protected void onReceive(String data) {
        System.out.println(data);
        update(data);        
    }

    @Override
    protected void onTimeout() {}

    public void update(String data){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data);
            try{
                String command = jsonNode.get("command").asText();
                switch (command) {
                    case COMM_START:
                        System.out.println("Handling COMM_START event");
                        Platform.runLater(() -> stopWatch.startSW());
                        break;

                    case COMM_GOAL:
                        System.out.println("Handling COMM_GOAL event");
                        try{
                            String target = jsonNode.get("targetTeam").asText();
                            if (target.startsWith(teamA.getTeamIPAddress())){
                                teamA.setTeamScore(0);
                                Platform.runLater(() -> {
                                    refBoardController.setScreenText(String.format("%s Scored!" ,teamA.getTeamName()));
                                    teamA.setTeamScore(teamA.getTeamScore()+1);
                                });
                            }
                            else if (target.startsWith(teamB.getTeamIPAddress())){
                                Platform.runLater(() -> {
                                    refBoardController.setScreenText(String.format("%s Scored!" ,teamB.getTeamName()));
                                    teamB.setTeamScore(teamB.getTeamScore()+1);
                                });
                            }
                        }catch(NoSuchElementException e){}
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
                        try{
                            String target = jsonNode.get("targetTeam").asText();
                            Platform.runLater(() -> refBoardController.setScreenText(String.format("%s %s",command, getTeam(target))));
                        }catch(NoSuchElementException e){
                            Platform.runLater(() -> refBoardController.setScreenText(command));
                        }
                }
            }
            catch(NoSuchElementException e){}
        }catch(JsonProcessingException e){}
    }

    private String getTeam(String ipaddres){
        if(ipaddres.startsWith(teamA.getTeamIPAddress())){
            return teamA.getTeamName();
        }
        return teamB.getTeamName();
    }
}
