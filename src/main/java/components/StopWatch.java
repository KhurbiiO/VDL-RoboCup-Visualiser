package components;

import components.controllers.LabelController;

/**
 * The StopWatch class extends LabelController to manage a stopwatch in a JavaFX application.
 * It includes methods to start, stop, reset, and update the stopwatch, as well as to format
 * and display the elapsed time.
 */
public class StopWatch extends LabelController {
    private boolean status;       // Indicates whether the stopwatch is running
    private double increment;     // The increment in milliseconds to update the stopwatch
    private long currentTimeMs;   // The current elapsed time in milliseconds

    /**
     * Constructor for StopWatch.
     * Initializes the increment, current time, and status of the stopwatch.
     * 
     * @param increment the increment in milliseconds to update the stopwatch
     */
    public StopWatch(long increment) {
        super();
        this.increment = increment; // SW accuracy
        this.currentTimeMs = 0;
        this.status = false;
    }

    /**
     * Updates the stopwatch by incrementing the current time.
     * This method should be called once every draw or update cycle.
     */
    public void updateStopWatch() {
        if (status) {
            currentTimeMs += this.increment;
            setScreenText(formatElapsedTime(getTimeSec()));
        }
    }

    /**
     * Resets the stopwatch to 0 and updates the display.
     */
    public void resetStopWatch() {
        status = false;
        currentTimeMs = 0;
        setScreenText(formatElapsedTime(getTimeSec()));
    }

    /**
     * Stops the stopwatch and updates the display to show "PAUSED".
     */
    public void stopSW() {
        status = false;
        setScreenText("PAUSED");
    }

    /**
     * Starts the stopwatch and updates the display to show the current time.
     */
    public void startSW() {
        status = true;
        setScreenText(formatElapsedTime(getTimeSec()));
    }

    // Getter methods for stopwatch properties

    /**
     * Gets the current elapsed time in milliseconds.
     * 
     * @return the current elapsed time in milliseconds
     */
    public long getTimeMs() {
        return currentTimeMs;
    }

    /**
     * Gets the current elapsed time in seconds.
     * 
     * @return the current elapsed time in seconds
     */
    public long getTimeSec() {
        return currentTimeMs / 1000;
    }

    /**
     * Gets the status of the stopwatch (running or not).
     * 
     * @return true if the stopwatch is running, false otherwise
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Formats the elapsed time in hours, minutes, and seconds.
     * 
     * @param totalSeconds the total elapsed time in seconds
     * @return a formatted string representing the elapsed time
     */
    private String formatElapsedTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
