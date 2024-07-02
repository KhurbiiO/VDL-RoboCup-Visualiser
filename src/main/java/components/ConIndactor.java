package components;

import components.controllers.CircleController;

public class ConIndactor extends CircleController {

    private boolean connected;

    public ConIndactor(){
        super();
        connected = false;
    }

    public void toggle(){
        connected = !connected;
        if (connected){
            setScreenColor("#0CFE03", "#000000", 1);
        }
        else{
            setScreenColor("#FE1203", "#000000", 1);
        }
    }


}
