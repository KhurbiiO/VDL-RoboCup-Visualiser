package components;

import java.util.Vector;

/**
 * The Position class represents a 3D coordinate (x, y, z) and provides methods
 * to get, set, and transform these coordinates. It also includes methods to map
 * coordinates from real-world dimensions to digital dimensions.
 */
public class Position {
    private Vector<Double> coordinate; // A vector to store the 3D coordinates

    /**
     * Default constructor for Position.
     * Initializes the coordinate vector with three zeros.
     */
    public Position() {
        coordinate = new Vector<Double>(3);
        for (int i = 0; i < 3; i++) {
            coordinate.add(0d);
        }
    }

    /**
     * Gets the X coordinate.
     * 
     * @return the X coordinate
     */
    public Double getX() {
        return getAxis(0);
    }

    /**
     * Gets the Y coordinate.
     * 
     * @return the Y coordinate
     */
    public Double getY() {
        return getAxis(1);
    }

    /**
     * Gets the Z coordinate.
     * 
     * @return the Z coordinate
     */
    public Double getZ() {
        return getAxis(2);
    }

    /**
     * Sets the X coordinate.
     * 
     * @param value the new X coordinate
     */
    public void setX(Double value) {
        setAxis(0, value);
    }

    /**
     * Sets the Y coordinate.
     * 
     * @param value the new Y coordinate
     */
    public void setY(Double value) {
        setAxis(1, value);
    }

    /**
     * Sets the Z coordinate.
     * 
     * @param value the new Z coordinate
     */
    public void setZ(Double value) {
        setAxis(2, value);
    }

    /**
     * Sets all three coordinates (X, Y, Z).
     * 
     * @param newX the new X coordinate
     * @param newY the new Y coordinate
     * @param newZ the new Z coordinate
     */
    public void setCoordinate(double newX, double newY, double newZ) {
        setX(newX);
        setY(newY);
        setZ(newZ);
    }

    /**
     * Gets all three coordinates as an array.
     * 
     * @return an array containing the X, Y, and Z coordinates
     */
    public double[] getCoordinate() {
        double[] coordinate_ = { getX(), getY(), getZ() };
        return coordinate_;
    }

    /**
     * Maps the real-world coordinates to digital coordinates for a vertically oriented grid.
     * 
     * @param realWidth the real-world width of the grid
     * @param realHeight the real-world height of the grid
     * @param digitalWidth the digital width of the grid
     * @param digitalHeight the digital height of the grid
     * @return an array containing the mapped X, Y, and Z coordinates
     */
    public double[] getGridVVector(double realWidth, double realHeight, double digitalWidth, double digitalHeight) {
        double[] coordinate_ = {
            map(getX(), (-realWidth / 2), (realWidth / 2), 0d, digitalWidth),
            map(getY(), (-realHeight / 2), (realHeight / 2), digitalHeight, 0d),
            getZ()
        };

        return coordinate_;
    }

    /**
     * Maps the real-world coordinates to digital coordinates for a horizontally oriented grid.
     * 
     * @param realWidth the real-world width of the grid
     * @param realHeight the real-world height of the grid
     * @param digitalWidth the digital width of the grid
     * @param digitalHeight the digital height of the grid
     * @return an array containing the mapped X, Y, and Z coordinates
     */
    public double[] getGridHVector(double realWidth, double realHeight, double digitalWidth, double digitalHeight) {
        double[] coordinate_ = {
            map(getY(), (realWidth / 2), (-realWidth / 2), digitalWidth, 0d),
            map(getX(), (realHeight / 2), (-realHeight / 2), digitalHeight, 0d),
            getZ()
        };
        return coordinate_;
    }

    /**
     * Gets the value of a specific axis.
     * 
     * @param index the index of the axis (0 for X, 1 for Y, 2 for Z)
     * @return the value of the specified axis
     */
    private Double getAxis(int index) {
        return coordinate.get(index);
    }

    /**
     * Sets the value of a specific axis.
     * 
     * @param index the index of the axis (0 for X, 1 for Y, 2 for Z)
     * @param newValue the new value for the specified axis
     */
    private void setAxis(int index, Double newValue) {
        coordinate.set(index, newValue);
    }

    /**
     * Maps a value from one range to another.
     * 
     * @param x the value to be mapped
     * @param inMin the lower bound of the input range
     * @param inMax the upper bound of the input range
     * @param outMin the lower bound of the output range
     * @param outMax the upper bound of the output range
     * @return the mapped value
     */
    private double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
