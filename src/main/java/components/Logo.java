package components;

import components.controllers.PaneController;

public class Logo extends PaneController{

    private String path;

    public Logo(){
        super();
    }

    public void setImage(String path){
        this.path = Logo.class.getResource(path).toExternalForm();
        this.getNode().setStyle("-fx-background-image: url(" + this.path + ");");
    }

    public String getPath() {
        return path;
    }
}
