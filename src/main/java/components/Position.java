package components;

import java.util.Vector;

public class Position {
    private Vector<Double> coordinate;
    private Double digitalWidth;
    private Double digitalHeight;
    private Double realWidth;
    private Double realHeight;

    public Position(Double digital_grid_width, Double digital_grid_height, Double real_grid_width, Double real_grid_height){
        coordinate = new Vector<Double>(3);
        for (int i = 0; i < 3; i++)
            coordinate.add(0d);

        digitalWidth = digital_grid_width;
        digitalHeight = digital_grid_height;
        realWidth = real_grid_width;
        realHeight = real_grid_height;
    }

    // Getters
    public Double getX(){
        return getAxis(0);
    }

    public Double getY(){
        return getAxis(1);
    }

    public Double getZ(){
        return getAxis(2);
    }

    // Setters
    public void setX(Double value){
        setAxis(0, value);
    }

    public void setY(Double value){
        setAxis(1, value);
    }

    public void setZ(Double value){
        setAxis(2, value);
    }

    public Vector<Double> getCoordinate(){
        return coordinate;
    }

    public Vector<Double> getGridHVector(){
        Vector<Double> coordinate_ = new Vector<Double>(3);
        coordinate_.add(map(getX(), (realWidth/2), (-realWidth/2), 0d, digitalHeight));
        coordinate_.add(map(getX(), (realHeight/2), (-realHeight/2), 0d, digitalWidth));
        coordinate_.add(getZ());

        return coordinate_;
    }

    public Vector<Double> getGridVVector(){
        Vector<Double> coordinate_ = new Vector<Double>(3);
        coordinate_.add(map(getX(), (realWidth/2), (-realWidth/2), 0d, digitalWidth));
        coordinate_.add(map(getX(), (realHeight/2), (-realHeight/2), 0d, digitalHeight));
        coordinate_.add(getZ());

        return coordinate_;
    }

    private Double getAxis(int index){
        return coordinate.get(index);
    }

    private void setAxis(int index, Double new_value){
        coordinate.set(index, new_value);
    }

    private Double map(Double x, Double in_min, Double in_max, Double out_min, Double out_max){
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
