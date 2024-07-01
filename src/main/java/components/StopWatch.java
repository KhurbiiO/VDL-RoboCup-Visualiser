package components;

import components.controllers.LabelController;

public class StopWatch extends LabelController{
    private boolean status;      
    private double increment;        
    private long currentTimeMs;          

    public StopWatch(long increment) {
        super();
        this.increment = increment; //SW accuracy
        this.currentTimeMs = 0;
        this.status = false;
    }

    public void updateStopWatch() {                // This method should be called once every draw()
        if (status) {
            currentTimeMs += this.increment;
            setScreenText(formatElapsedTime(getTimeSec()));
        }
    }

    public void resetStopWatch() {
        status = false;
        currentTimeMs = 0;
        setScreenText(formatElapsedTime(getTimeSec()));
    }

    public void stopSW() {
        status = false;
        setScreenText("PAUSED");
    }

    public void startSW() {
        status = true;
        setScreenText(formatElapsedTime(getTimeSec()));
    }

    public long getTimeMs() {
        return currentTimeMs;
    }

    public long getTimeSec() {
        return currentTimeMs/1000;
    }

    public boolean getStatus() {
        return status;
    }

    private String formatElapsedTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
