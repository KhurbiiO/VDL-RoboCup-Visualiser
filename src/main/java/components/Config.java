package components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;

public class Config {
    private boolean loaded;

    private String refboxIP;
    private int refboxPort;
    private String teamAIP;
    private int teamAPort;
    private String teamBIP;
    private int teamBPort;

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

    private String teamAFullname;
    private String teamASmallname;
    private int teamASize;
    private String teamAFlagcolor;
    private String teamALogoPath;

    private String teamBFullname;
    private String teamBSmallname;
    private int teamBSize;
    private String teamBFlagcolor;
    private String teamBLogoPath;


    private double playerRadius;
    private String playerStroke;
    private int playerStrokeWidth;

    private String ballColor;
    private double ballRadius;
    private String ballStroke;
    private int ballStrokeWidth;

    public Config(){
        loaded = false;
    }

    public void readConfig(String filepath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(filepath));

            JsonNode networking = rootNode.path("networking");
            refboxIP = networking.path("refboxIP").asText();
            refboxPort = networking.path("refboxPort").asInt();

            JsonNode teamA = networking.path("teamA");
            teamAIP = teamA.path("ipaddress").asText();
            teamAPort = teamA.path("port").asInt();

            JsonNode teamB = networking.path("teamB");
            teamBIP = teamB.path("ipaddress").asText();
            teamBPort = teamB.path("port").asInt();

            JsonNode appearance = rootNode.path("appearance");

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

            JsonNode teamAAppearance = appearance.path("teamA");
            teamAFullname = teamAAppearance.path("fullname").asText();
            teamASmallname = teamAAppearance.path("smallname").asText();
            teamASize = teamAAppearance.path("size").asInt();
            teamAFlagcolor = teamAAppearance.path("flagcolor").asText();
            teamALogoPath = teamAAppearance.path("logoPath").asText();

            JsonNode teamBAppearance = appearance.path("teamB");
            teamBFullname = teamBAppearance.path("fullname").asText();
            teamBSmallname = teamBAppearance.path("smallname").asText();
            teamBSize = teamBAppearance.path("size").asInt();
            teamBFlagcolor = teamBAppearance.path("flagcolor").asText();
            teamBLogoPath = teamBAppearance.path("logoPath").asText();

            JsonNode player = appearance.path("player");
            playerRadius = player.path("radius").asDouble();
            playerStroke = player.path("stroke").asText();
            playerStrokeWidth = player.path("strokeWidth").asInt();

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
   public boolean getLoaded(){ return loaded; }

   public String getRefboxIP() { return refboxIP; }
   public int getRefboxPort() { return refboxPort; }
   public String getTeamAIP() { return teamAIP; }
   public int getTeamAPort() { return teamAPort; }
   public String getTeamBIP() { return teamBIP; }
   public int getTeamBPort() { return teamBPort; }

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
