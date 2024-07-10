package components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class UIConfig {

    private double borderSpaceWidth;

    private double relativeWidthRatio;
    private double relativeHeightRatio;
    private double relativeTeamASectionHeightRatio;
    private double relativeTeamBSectionHeightRatio;
    private double relativeTeamAScoreboardWidthRatio;
    private double relativeTeamAFlagWidthRatio;
    private double relativeTeamALogoWidthRatio;
    private double relativeTeamBScoreboardWidthRatio;
    private double relativeTeamBFlagWidthRatio;
    private double relativeTeamBLogoWidthRatio;
    private double relativeTimerboardWidthRatio;
    private double relativeTimerboardHeightRatio;
    private double relativeRefboardWidthRatio;
    private double relativeRefboardHeightRatio;

    private double playerRadius;
    private String playerStroke;
    private double playerStrokeWidth;

    private String ballColor;
    private double ballRadius;
    private String ballStroke;
    private double ballStrokeWidth;

    public UIConfig(String filePath) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        // Field values
        borderSpaceWidth = rootNode.path("field").path("borderSpaceWidth").asDouble();

        // MatchStats values
        relativeWidthRatio = rootNode.path("matchStats").path("relativeWidthRatio").asDouble();
        relativeHeightRatio = rootNode.path("matchStats").path("relativeHeightRatio").asDouble();
        
        relativeTeamASectionHeightRatio = rootNode.path("matchStats").path("ralativeTeamASectionHeightRatio").asDouble();
        relativeTeamBSectionHeightRatio = rootNode.path("matchStats").path("ralativeTeamBSectionHeightRatio").asDouble();

        relativeTeamAScoreboardWidthRatio = rootNode.path("matchStats").path("relativeTeamAScoreboardWidthRatio").asDouble();
        relativeTeamAFlagWidthRatio = rootNode.path("matchStats").path("relativeTeamAFlagWidthRatio").asDouble();
        relativeTeamALogoWidthRatio = rootNode.path("matchStats").path("relativeTeamALogoWidthRatio").asDouble();
        relativeTeamBScoreboardWidthRatio = rootNode.path("matchStats").path("relativeTeamBScoreboardWidthRatio").asDouble();
        relativeTeamBFlagWidthRatio = rootNode.path("matchStats").path("relativeTeamBFlagWidthRatio").asDouble();
        relativeTeamBLogoWidthRatio = rootNode.path("matchStats").path("relativeTeamBLogoWidthRatio").asDouble();
        relativeTimerboardWidthRatio = rootNode.path("matchStats").path("relativeTimerboardWidthRatio").asDouble();
        relativeTimerboardHeightRatio = rootNode.path("matchStats").path("relativeTimerboardHeightRatio").asDouble();
        relativeRefboardWidthRatio = rootNode.path("matchStats").path("relativeRefboardWidthRatio").asDouble();
        relativeRefboardHeightRatio = rootNode.path("matchStats").path("relativeRefboardHeightRatio").asDouble();

        // Player values
        playerRadius = rootNode.path("player").path("radius").asDouble();
        playerStroke = rootNode.path("player").path("stroke").asText();
        playerStrokeWidth = rootNode.path("player").path("strokeWidth").asDouble();

        // Ball values
        ballColor = rootNode.path("ball").path("color").asText();
        ballRadius = rootNode.path("ball").path("radius").asDouble();
        ballStroke = rootNode.path("ball").path("stroke").asText();
        ballStrokeWidth = rootNode.path("ball").path("strokeWidth").asDouble();
    }

    // Getters for field values
    public double getBorderSpaceWidth() { return borderSpaceWidth; }

    // Getters for matchStats values
    public double getRelativeWidthRatio() { return relativeWidthRatio; }
    public double getRelativeHeightRatio() { return relativeHeightRatio; }
    public double getRelativeTeamASectionHeightRatio() { return relativeTeamASectionHeightRatio; }
    public double getRelativeTeamBSectionHeightRatio() { return relativeTeamBSectionHeightRatio; }
    public double getRelativeTeamAScoreboardWidthRatio() { return relativeTeamAScoreboardWidthRatio; }
    public double getRelativeTeamAFlagWidthRatio() { return relativeTeamAFlagWidthRatio; }
    public double getRelativeTeamALogoWidthRatio() { return relativeTeamALogoWidthRatio; }
    public double getRelativeTeamBScoreboardWidthRatio() { return relativeTeamBScoreboardWidthRatio; }
    public double getRelativeTeamBFlagWidthRatio() { return relativeTeamBFlagWidthRatio; }
    public double getRelativeTeamBLogoWidthRatio() { return relativeTeamBLogoWidthRatio; }
    public double getRelativeTimerboardWidthRatio() { return relativeTimerboardWidthRatio; }
    public double getRelativeTimerboardHeightRatio() { return relativeTimerboardHeightRatio; }
    public double getRelativeRefboardWidthRatio() { return relativeRefboardWidthRatio; }
    public double getRelativeRefboardHeightRatio() { return relativeRefboardHeightRatio; }

    // Getters for player values
    public double getPlayerRadius() { return playerRadius; }
    public String getPlayerStroke() { return playerStroke; }
    public double getPlayerStrokeWidth() { return playerStrokeWidth; }

    // Getters for ball values
    public String getBallColor() { return ballColor; }
    public double getBallRadius() { return ballRadius; }
    public String getBallStroke() { return ballStroke; }
    public double getBallStrokeWidth() { return ballStrokeWidth; }
}