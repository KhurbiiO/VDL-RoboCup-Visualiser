package components;
import java.util.Vector;


public class Team {
    private String teamName;
    private boolean isTeamA;
    private Vector<Robot> robots;
    private String robotImgPath;
    private String logoImgPath;

    public Team(String teamName, int teamSize, boolean isTeamA, String logoImgPath, String robotImgPath){
        robots = new Vector<Robot>();
        for (int i = 0; i < teamSize; i++){
            robots.add(new Robot(robotImgPath));
        }
        this.isTeamA = isTeamA;
        this.robotImgPath = robotImgPath;
        this.logoImgPath = logoImgPath;
    }


    public String getTeamName() {
        return teamName;
    }

    public String getLogoImgPath() {
        return logoImgPath;
    }

    public String getRobotImgPath() {
        return robotImgPath;
    }

    public boolean isTeamA(){
        return isTeamA;
    }

    public Robot getPlayer(int idx){
        return robots.get(idx);
    }
}
