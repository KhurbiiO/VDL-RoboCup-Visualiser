package components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * The Config class is responsible for reading configuration data from a JSON file
 * and storing it for later use.
 */
public class Config {
    private boolean loaded; // Indicates whether the configuration has been successfully loaded

    // Network configuration variables
    private String refboxIP;
    private int refboxPort;
    private String teamAIP;
    private int teamAPort;
    private String teamBIP;
    private int teamBPort;
    private String broadCastIP;
    private int broadCastPort;

    // Field appearance configuration variables
    private String fieldColor;
    private int fieldA;
    private int fieldB;
    private double fieldC;
    private double fieldD;
    private double fieldE;
    private double fieldF;
    private double fieldG;
    private double fieldH;
    private double fieldI;
    private double fieldJ;
    private double fieldK;
    private double fieldL;
    private double fieldM;
    private double fieldN;
    private double fieldO;
    private double fieldP;
    private double fieldQ;
    private double goalWidth;
    private double goalDepth;

    // Team A appearance configuration variables
    private String teamAFullname;
    private String teamASmallname;
    private int teamASize;
    private String teamAFlagcolor;
    private String teamALogoPath;

    // Team B appearance configuration variables
    private String teamBFullname;
    private String teamBSmallname;
    private int teamBSize;
    private String teamBFlagcolor;
    private String teamBLogoPath;

    // Player appearance configuration variables
    private double playerRadius;
    private String playerStroke;
    private int playerStrokeWidth;

    // Ball appearance configuration variables
    private String ballColor;
    private double ballRadius;
    private String ballStroke;
    private int ballStrokeWidth;

    /**
     * Default constructor for Config.
     * Initializes the loaded flag to false.
     */
    public Config() {
        loaded = false;
    }

    /**
     * Reads the configuration from a JSON file and stores it in the appropriate fields.
     * 
     * @param filepath the path to the configuration file
     */
    public void readConfig(String filepath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(filepath));

            // Read networking configuration
            JsonNode networking = rootNode.path("networking");
            refboxIP = networking.path("refboxIP").asText();
            refboxPort = networking.path("refboxPort").asInt();
            broadCastIP = networking.path("broadcastIP").asText();
            broadCastPort = networking.path("broadcastPort").asInt();

            JsonNode teamA = networking.path("teamA");
            teamAIP = teamA.path("ipaddress").asText();
            teamAPort = teamA.path("port").asInt();

            JsonNode teamB = networking.path("teamB");
            teamBIP = teamB.path("ipaddress").asText();
            teamBPort = teamB.path("port").asInt();

            // Read appearance configuration
            JsonNode appearance = rootNode.path("appearance");

            // Read field appearance configuration
            JsonNode field = appearance.path("field");
            fieldColor = field.path("color").asText();
            fieldA = field.path("A").asInt();
            fieldB = field.path("B").asInt();
            fieldC = field.path("C").asDouble();
            fieldD = field.path("D").asDouble();
            fieldE = field.path("E").asDouble();
            fieldF = field.path("F").asDouble();
            fieldG = field.path("G").asDouble();
            fieldH = field.path("H").asDouble();
            fieldI = field.path("I").asDouble();
            fieldJ = field.path("J").asDouble();
            fieldK = field.path("K").asDouble();
            fieldL = field.path("L").asDouble();
            fieldM = field.path("M").asDouble();
            fieldN = field.path("N").asDouble();
            fieldO = field.path("O").asDouble();
            fieldP = field.path("P").asDouble();
            fieldQ = field.path("Q").asDouble();
            goalWidth = field.path("goalwidth").asDouble();
            goalDepth = field.path("goaldepth").asDouble();

            // Read team A appearance configuration
            JsonNode teamAAppearance = appearance.path("teamA");
            teamAFullname = teamAAppearance.path("fullname").asText();
            teamASmallname = teamAAppearance.path("smallname").asText();
            teamASize = teamAAppearance.path("size").asInt();
            teamAFlagcolor = teamAAppearance.path("flagcolor").asText();
            teamALogoPath = teamAAppearance.path("logoPath").asText();

            // Read team B appearance configuration
            JsonNode teamBAppearance = appearance.path("teamB");
            teamBFullname = teamBAppearance.path("fullname").asText();
            teamBSmallname = teamBAppearance.path("smallname").asText();
            teamBSize = teamBAppearance.path("size").asInt();
            teamBFlagcolor = teamBAppearance.path("flagcolor").asText();
            teamBLogoPath = teamBAppearance.path("logoPath").asText();

            // Read player appearance configuration
            JsonNode player = appearance.path("player");
            playerRadius = player.path("radius").asDouble();
            playerStroke = player.path("stroke").asText();
            playerStrokeWidth = player.path("strokeWidth").asInt();

            // Read ball appearance configuration
            JsonNode ball = appearance.path("ball");
            ballColor = ball.path("color").asText();
            ballRadius = ball.path("radius").asDouble();
            ballStroke = ball.path("stroke").asText();
            ballStrokeWidth = ball.path("strokeWidth").asInt();

        } catch (IOException e) {
            e.printStackTrace();
        }
        loaded = true;
    }

    // Getter methods for all variables
    public boolean getLoaded() { return loaded; }

    public String getRefboxIP() { return refboxIP; }
    public int getRefboxPort() { return refboxPort; }
    public String getTeamAIP() { return teamAIP; }
    public int getTeamAPort() { return teamAPort; }
    public String getTeamBIP() { return teamBIP; }
    public int getTeamBPort() { return teamBPort; }
    public String getBroadCastIP() {
        return broadCastIP;
    }
    public int getBroadCastPort() {
        return broadCastPort;
    }

    public String getFieldColor() { return fieldColor; }
    public int getFieldA() { return fieldA; }
    public int getFieldB() { return fieldB; }
    public double getFieldC() { return fieldC; }
    public double getFieldD() { return fieldD; }
    public double getFieldE() { return fieldE; }
    public double getFieldF() { return fieldF; }
    public double getFieldG() { return fieldG; }
    public double getFieldH() { return fieldH; }
    public double getFieldI() { return fieldI; }
    public double getFieldJ() { return fieldJ; }
    public double getFieldK() { return fieldK; }
    public double getFieldL() { return fieldL; }
    public double getFieldM() { return fieldM; }
    public double getFieldN() { return fieldN; }
    public double getFieldO() { return fieldO; }
    public double getFieldP() { return fieldP; }
    public double getFieldQ() { return fieldQ; }
    public double getGoalWidth() { return goalWidth; }
    public double getGoalDepth() { return goalDepth; }

    public String getTeamAFullname() { return teamAFullname; }
    public String getTeamASmallname() { return teamASmallname; }
    public int getTeamASize() { return teamASize; }
    public String getTeamAFlagcolor() { return teamAFlagcolor; }
    public String getTeamALogoPath() { return teamALogoPath; }

    public String getTeamBFullname() { return teamBFullname; }
    public String getTeamBSmallname() { return teamBSmallname; }
    public int getTeamBSize() { return teamBSize; }
    public String getTeamBFlagcolor() { return teamBFlagcolor; }
    public String getTeamBLogoPath() { return teamBLogoPath; }

    public double getPlayerRadius() { return playerRadius; }
    public String getPlayerStroke() { return playerStroke; }
    public int getPlayerStrokeWidth() { return playerStrokeWidth; }

    public String getBallColor() { return ballColor; }
    public double getBallRadius() { return ballRadius; }
    public String getBallStroke() { return ballStroke; }
    public int getBallStrokeWidth() { return ballStrokeWidth; }
}
