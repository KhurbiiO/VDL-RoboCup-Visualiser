package components;

public class Robot extends CircleController{
    public Position currentPosition;
    public Position targetPosition;
    private double[] currentVelocity;
    private double batterPercentage;
    private boolean ballEngaged;
    private String intention;

    public Robot(){
        super();     
        currentPosition = new Position();
        targetPosition = new Position();
        currentVelocity = new double[3];
        batterPercentage = 100f;
        ballEngaged = false;
        intention = "NULL";
    }

    public double[] getCurrentVelocity() {
        return currentVelocity;
    }
    public double getBatterPercentage() {
        return batterPercentage;
    }
    public String getIntention() {
        return intention;
    }
    public boolean getBallEngaged(){
        return ballEngaged;
    }

    public void setCurrentPosition(double newX, double newY, double newZ, double digitalHeight, double digitalWidth) {
        currentPosition.setX(newX);
        currentPosition.setY(newY);
        currentPosition.setZ(newZ);

        updateScreenCoordinate(currentPosition.getGridHVector(newX, newY, digitalWidth, digitalHeight), 1);
    }
    public void setTargetPosition(double newX, double newY, double newZ) {
        targetPosition.setX(newX);
        targetPosition.setY(newY);
        targetPosition.setZ(newZ);
    }
    public void setCurrentVelocity(double[] currentVelocity) {
        this.currentVelocity = currentVelocity;
    }
    public void setBatterPercentage(double batterPercentage) {
        this.batterPercentage = batterPercentage;
    }
    public void setIntention(String intention) {
        this.intention = intention;
    }
    public void setBallEngaged(boolean ballEngaged) {
        this.ballEngaged = ballEngaged;
    }
}