package components.communication;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.Ball;
import components.CoordsTransformation;
import components.Robot;
import components.Team;
import components.Field;
import javafx.application.Platform;

public class WSClient extends UDPClient {
    private Team team;
    private Ball ball;
    private Field field;
    private CoordsTransformation transform;

    private double fieldWidth;
    private double fieldHeight;

    public WSClient(int port, String ipaddress, Team team, Ball ball, Field field, CoordsTransformation transform){
        super(port, ipaddress);
        this.team = team;
        this.ball = ball;
        this.field = field;
        this.transform = transform;

        fieldWidth = field.getA() + field.getL()*2 + field.getM();
        fieldHeight = field.getB() + field.getL()*2;
    }

    @Override
    protected void onReceive(byte[] data, String ip, int port) {
        String json = new String(data, 0, data.length);

        updateField(json);
    }

    @Override
    protected void onTimeout() {}

    @Override
    public void listen(boolean on) {
        super.listen(on);
    }

    private void updateField(String data){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data);

            Iterator<JsonNode> robots = jsonNode.get("worldstate").get("robots").elements();

            for (int i = 0; i < team.getTeamSize(); i++) {
                try{
                    JsonNode robot = robots.next();
                    Robot player = team.getPlayer(i);

                    player.setScreenOpacity(1); // Visually enable robot

                    player.setBallEngaged(robot.get("ballEngaged").asBoolean());
                    player.setBatterPercentage(robot.get("batteryLevel").asDouble());
                    player.setIntention(robot.get("intention").asText());
                    Iterator<JsonNode> currentPose_ = robot.get("pose").elements();
                    final double[] currentPose = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};
                    
                    Iterator<JsonNode> targetPose_ = robot.get("target pose").elements();
                    final double[] targetPose = {targetPose_.next().asDouble(), targetPose_.next().asDouble(), targetPose_.next().asDouble()};
                    
                    final double[] transformedCurrentPose;
                    final double[] transformedTargetPose;
                    
                    if(team.isTeamB()){
                        transformedCurrentPose = transform.transformRobotCoords(currentPose);
                        transformedTargetPose = transform.transformRobotCoords(targetPose);
                    } else {
                        transformedCurrentPose = currentPose;
                        transformedTargetPose = targetPose;
                    }
                    
                    Platform.runLater(() -> player.setCurrentPosition(transformedCurrentPose[0], transformedCurrentPose[1], transformedCurrentPose[2], fieldHeight, fieldWidth, field.getNode().getPrefHeight(), field.getNode().getPrefWidth()));
                    player.setTargetPosition(transformedTargetPose[0], transformedTargetPose[1], transformedTargetPose[2]);
                }
                catch(NoSuchElementException e){
                    for (int j = i; j < team.getTeamSize(); j++){
                        team.getPlayer(i).setScreenOpacity(0); // Visually disable the rest of robots
                    }
                    break;
                }
            }

            try{
                Iterator<JsonNode> balls = jsonNode.get("worldstate").get("balls").elements();
                try{
                    JsonNode ball_ = balls.next(); // List is in order of confidence
                    Iterator<JsonNode> currentPose_ = ball_.get("position").elements();
                    final double currentPose[] = {currentPose_.next().asDouble(), currentPose_.next().asDouble(), currentPose_.next().asDouble()};
                    ball.setConfidence(ball_.get("confidence").asDouble());
                    
                    final double[] transformedCurrentPose;

                    if(team.isTeamB()){
                        transformedCurrentPose = transform.transformBallCoords(currentPose);
                    }
                    else{
                        transformedCurrentPose = currentPose;
                    }
                    Platform.runLater(() -> ball.setCurrentPosition(transformedCurrentPose[0], transformedCurrentPose[1], transformedCurrentPose[2], fieldHeight, fieldWidth, field.getNode().getPrefHeight(), field.getNode().getPrefWidth()));
                }catch(NoSuchElementException e){}
            }catch(NoSuchElementException e){}
        }catch(JsonProcessingException e){}
    }
    
}
