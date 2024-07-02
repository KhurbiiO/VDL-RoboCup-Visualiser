package components;

import components.controllers.PaneController;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Field extends PaneController{

    private double A;  
    private double B;  
    private double C; 
    private double D; 
    private double E;  
    private double F; 
    private double G;  
    private double H;  
    private double I;  
    private double J;  
    private double K;  
    private double L;  
    private double M;  
    private double N;  
    private double O;  
    private double P;  
    private double Q; 

    private WebView view;
    private WebEngine viewEngine;
    
    public Field(){
        super();
        A = 22;
        B = 14;
        C = 6.9;
        D = 3.9;
        E = 2.25;
        F = 0.75;
        G = 0.75;
        H = 4;
        I = 3.6;
        J = 0.15;
        K = 0.125;
        L = 1;
        M = 1;
        N = 7.5;
        O = 1;
        P = 0.5;
        Q = 3.5;
    }

    @Override
    public void bindPane(Pane node) {
        super.bindPane(node);

        view = new WebView();
        viewEngine = view.getEngine();

        view.prefWidthProperty().bind(this.getNode().widthProperty()); // Stretch image to fit the Pane's width
        view.prefHeightProperty().bind(this.getNode().heightProperty()); // Stretch image to fit the Pane's height

        this.getNode().getChildren().add(view);
    }

    public void drawField(String flagAColor, String flagBColor, String fieldColor){
        String htmlContent = "<html><body style='margin:0;padding:0;overflow:hidden; background:#52494a'>" +
                            "<div style='width:100vw;height:100vh;display:flex;justify-content:center;align-items:center;'>" +
                            "<svg viewBox=\"0 0 180 220\" width=\"100%\" height=\"100%\" fill=\"none\">" +
                            "<rect x=\"167.5\" y=\"0.5\" width=\"219\" height=\"159\" transform=\"rotate(90 167.5 0.5)\" fill=\""+ fieldColor +"\" stroke=\"black\"/>" +
                            "<path d=\"M157.5 10.5V209.5H18.5V10.5L157.5 10.5Z\" stroke=\"black\"/>" +
                            "<rect x=\"125.061\" y=\"184.162\" width=\"25.3374\" height=\"73.2683\" transform=\"rotate(90 125.061 184.162)\" stroke=\"black\"/>" +
                            "<rect x=\"125.061\" y=\"10.5\" width=\"25.3374\" height=\"73.2683\" transform=\"rotate(90 125.061 10.5)\" stroke=\"black\"/>" +
                            "<rect x=\"108.842\" y=\"10.5\" width=\"8.87653\" height=\"40.8293\" transform=\"rotate(90 108.842 10.5)\" stroke=\"black\"/>" +
                            "<rect x=\"108.84\" y=\"6.5\" width=\"4\" height=\"40.8293\" transform=\"rotate(90 108.84 6.5)\" fill=\"" + flagAColor + "\" stroke=\"black\"/>" +
                            "<rect x=\"108.93\" y=\"209.5\" width=\"4\" height=\"40.8293\" transform=\"rotate(90 108.93 209.5)\" fill=\"" + flagBColor + "\" stroke=\"black\"/>" +
                            "<rect x=\"108.842\" y=\"200.623\" width=\"8.87653\" height=\"40.8293\" transform=\"rotate(90 108.842 200.623)\" stroke=\"black\"/>" +
                            "<path d=\"M88.4272 131.311C75.7119 131.311 65.4516 121.56 65.4516 109.588C65.4516 97.617 75.7119 87.8662 88.4272 87.8662C101.143 87.8662 111.403 97.617 111.403 109.588C111.403 121.56 101.143 131.311 88.4272 131.311Z\" stroke=\"black\"/>" +
                            "<line x1=\"158\" y1=\"110.089\" x2=\"18\" y2=\"110.089\" stroke=\"black\"/>" +
                            "<rect x=\"9.61719\" y=\"0.499969\" width=\"218.429\" height=\"9.11685\" transform=\"rotate(90 9.61719 0.499969)\" fill=\"#A27D7D\" stroke=\"black\"/>" +
                            "<rect x=\"9.61719\" y=\"0.499969\" width=\"83.7796\" height=\"9\" transform=\"rotate(90 9.61719 0.499969)\" fill=\"#71BBD2\" stroke=\"black\"/>" +
                            "<rect x=\"9.61719\" y=\"135.72\" width=\"83.7796\" height=\"9\" transform=\"rotate(90 9.61719 135.72)\" fill=\"#59A9BB\" stroke=\"black\"/>" +
                            "</svg></div></body></html>";

        viewEngine.loadContent(htmlContent);
    }



    public void removePlayers(int n){
        for (int i = 1; i < n+1; i++){
            getNode().getChildren().remove(1);
        }
    }
    
    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    public double getD() {
        return D;
    }

    public void setD(double d) {
        D = d;
    }

    public double getE() {
        return E;
    }

    public void setE(double e) {
        E = e;
    }

    public double getF() {
        return F;
    }

    public void setF(double f) {
        F = f;
    }

    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

    public double getJ() {
        return J;
    }

    public void setJ(double j) {
        J = j;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }

    public double getN() {
        return N;
    }

    public void setN(double n) {
        N = n;
    }

    public double getO() {
        return O;
    }

    public void setO(double o) {
        O = o;
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }

    public double getQ() {
        return Q;
    }

    public void setQ(double q) {
        Q = q;
    }
}
