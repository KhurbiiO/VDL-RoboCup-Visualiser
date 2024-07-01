package components;

import components.controllers.PaneController;

public class Flag extends PaneController{

    private String color;
    
    public Flag(){
        super();
    }

    @Override
    public void setScreenColor(String color) {
        this.color = color;
        super.setScreenColor(color);
    }

    public String getColor() {
        return color;
    }
}
