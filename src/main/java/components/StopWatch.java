package components;

public class StopWatch {
    
    // StopWatch fields
    private long oldTime;
    private long deltaTime;
    private boolean status;              // StopWatch ON / OFF status
    private long currentTimeMs;          // in ms
    private long currentTimeSec;         // in seconds (ceiling of currentTimeMs / 1000)
    private LabelController timerController;

    // StopWatch constructor
    // Parameters:
    //        startValue - expressed in seconds, can be greater or equal to zero
    //        startUp - Boolean. If true the stopWatch starts immediately
    public StopWatch(long startValue, boolean startUp, LabelController timLabelController) {
        this.oldTime = System.currentTimeMillis();
        this.deltaTime = 0;
        this.currentTimeSec = startValue;
        this.currentTimeMs = startValue * 1000;
        this.status = startUp;
        this.timerController = timLabelController;
        
        // If startUp is true, start the stopwatch
        if (startUp) {
            startSW();
        }
    }

    // StopWatch Methods
    public void updateStopWatch() {                // This method should be called once every draw()
        if (status) {
            long t = System.currentTimeMillis();
            deltaTime = t - oldTime;
            oldTime = t;

            adjustValues();

            // System.out.println(currentTimeSec);
            this.timerController.setScreenText(formatElapsedTime(currentTimeSec));
        }
    }

    private void adjustValues() {
        if (currentTimeMs > 0) {
            if (status) {
                currentTimeMs += deltaTime;
                currentTimeSec = currentTimeMs / 1000;
            }
        }
    }

    public void resetStopWatch() {
        status = false;
        currentTimeMs = 0;
        currentTimeSec = 0;
        timerController.setScreenText(formatElapsedTime(currentTimeSec));
    }

    public void stopSW() {
        status = false;
        timerController.setScreenText("PAUSED");
    }

    public void startSW() {
        if (!status) {
            status = true;
            oldTime = System.currentTimeMillis();
            timerController.setScreenText(formatElapsedTime(currentTimeSec));
        }
    }

    public long getTimeMs() {
        return currentTimeMs;
    }

    public long getTimeSec() {
        return currentTimeSec;
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
