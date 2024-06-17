package components;

import java.util.Vector;

public class Position {
    private Vector<Double> coordinate;

    public Position(){
        coordinate = new Vector<Double>(3);
        for (int i = 0; i < 3; i++)
            coordinate.add(0d);
    }

    public Double getX(){
        return getAxis(0);
    }

    public Double getY(){
        return getAxis(1);
    }

    public Double getZ(){
        return getAxis(2);
    }

    public void setX(Double value){
        setAxis(0, value);
    }

    public void setY(Double value){
        setAxis(1, value);
    }

    public void setZ(Double value){
        setAxis(2, value);
    }

    public double[] getCoordinate(){
        double coordinate_[] = {getX(), getY(), getZ()};
        return coordinate_;
    }

    public double[] getGridVVector(double realWidth, double realHeight, double digitalWidth, double digitalHeight){
        double coordinate_[] = {map(getX(), (realWidth/2), (-realWidth/2), digitalWidth, 0d), 
                                map(getY(), (realHeight/2), (-realHeight/2), digitalHeight, 0d), 
                                getZ()};
        return coordinate_;
    }

    public double[] getGridHVector(double realWidth, double realHeight, double digitalWidth, double digitalHeight){
        double coordinate_[] = {map(getY(), (realWidth/2), (-realWidth/2), digitalWidth, 0d), 
                                map(getX(), (realHeight/2), (-realHeight/2), digitalHeight, 0d), 
                                getZ()};
        return coordinate_;
    }

    private Double getAxis(int index){
        return coordinate.get(index);
    }
    
    private void setAxis(int index, Double newValue){
        coordinate.set(index, newValue);
    }

    private double map(double x, double inMin, double inMax, double outMin, double outMax){
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}