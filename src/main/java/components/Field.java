package components;

import components.controllers.PaneController;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * The Field class extends PaneController to manage the visual representation
 * of a field in a JavaFX application. It includes methods to draw the field,
 * manage players, and bind a WebView to the Pane.
 */
public class Field extends PaneController {

    // Field dimensions and properties
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
    private double goaldepth;
    private double goalwidth;

    private WebView view;
    private WebEngine viewEngine;

    /**
     * Default constructor for Field.
     * Initializes field dimensions and properties.
     */
    public Field() {
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
        goalwidth = 2.42;
        goaldepth = 0.4;

    }

    /**
     * Binds a Pane node to this controller and initializes the WebView.
     * 
     * @param node the Pane node to be managed
     */
    @Override
    public void bindPane(Pane node) {
        super.bindPane(node);

        view = new WebView();
        viewEngine = view.getEngine();

        // Stretch image to fit the Pane's width and height
        view.prefWidthProperty().bind(this.getNode().widthProperty());
        view.prefHeightProperty().bind(this.getNode().heightProperty());

        this.getNode().getChildren().add(view);
    }

    /**
     * Draws the field with specified colors for team flags and field background.
     * 
     * @param flagAColor the color of team A's flag
     * @param flagBColor the color of team B's flag
     * @param fieldColor the background color of the field
     */
    public void drawField(String flagAColor, String flagBColor, String fieldColor) {

        // Scale factor to fit field into SVG
        double scale = 10.0;
        
        // Calculate dimensions for SVG
        double fieldHeight = A * scale;
        double fieldWidth = B * scale;
        double penaltyAreaWidth = C * scale;
        double goalAreaWidth = D * scale;
        double penaltyAreaDepth = E * scale;
        double goalAreaDepth = F * scale;
        double centerCircleDiameter = H * scale;
        
        double goalDepth = goaldepth * scale;
        double goalWidth = goalwidth * scale;

        // double penaltyMarkDistance = I * scale;
        double markDiameter = J * scale;
        double lineWidth = K * scale;
        double outerShell = L * scale;
        double technicalFieldWidth = M * scale;
        double technicalFieldLength = N * scale;

         // Total width and height for the SVG canvas
        double svgWidth = fieldHeight + outerShell*2;
        double svgHeight = fieldWidth + outerShell*2 + technicalFieldWidth*2;

        // Calculate positions based on scaled dimensions
        double centerX = svgWidth/2;
        double centerY = svgHeight/2;

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

        //TODO: Continue the complete SVG field generator.
        // String htmlContent = "<html><body style='margin:0;padding:0;overflow:hidden; background:#52494a'>" +
        // "<div style='width:100vw;height:100vh;display:flex;justify-content:center;align-items:center;'>" +
        // "<svg viewBox=\"0 0 " + svgWidth + " " + svgHeight + "\" style=\"width:100%; height:100%;\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
        // "<g transform=\"rotate(-90 " + centerX + " " + centerY + ")\">" +
        // // Outer field
        // "<rect x=\"0\" y=\"0\" width=\"" + (fieldHeight + outerShell*2) + "\" height=\"" + (fieldWidth + outerShell*2 + technicalFieldWidth*2) + "\" fill=\"" + fieldColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" +
        // "<rect x=\"0\" y=\"0\" width=\"" + svgWidth + "\" height=\"" + technicalFieldWidth + "\" fill=\"#000000\" />" + // Top ghost field
        // "<rect x=\"" + outerShell + "\" y=\"" + (outerShell + technicalFieldWidth) + "\" width=\"" + fieldHeight + "\" height=\"" + fieldWidth + "\" fill=\"" + fieldColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" +
        // // Penalty areas
        // "<rect x=\"" + outerShell + "\" y=\"" + (centerY - penaltyAreaWidth/2) + "\" width=\"" + penaltyAreaDepth + "\" height=\"" + penaltyAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left penalty area
        // "<rect x=\"" + (outerShell + fieldHeight - penaltyAreaDepth) + "\" y=\"" + (centerY - penaltyAreaWidth/2) + "\" width=\"" + penaltyAreaDepth + "\" height=\"" + penaltyAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right penalty area
        // // Goal areas
        // "<rect x=\"" + outerShell + "\" y=\"" + (centerY - goalAreaWidth/2 ) + "\" width=\"" + goalAreaDepth + "\" height=\"" + goalAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left goal area
        // "<rect x=\"" + (outerShell + fieldHeight - goalAreaDepth) + "\" y=\"" + (centerY - goalAreaWidth/2 ) + "\" width=\"" + goalAreaDepth + "\" height=\"" + goalAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right goal area
        // // Flags
        // "<rect x=\"" + (outerShell - goalDepth) + "\" y=\"" + (centerY - goalWidth/2) + "\" width=\"" + goalDepth + "\" height=\"" + goalWidth + "\" fill=\"" + flagAColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left flag
        // "<rect x=\"" + (outerShell + fieldHeight) + "\" y=\"" + (centerY - goalWidth/2) + "\" width=\"" + goalDepth + "\" height=\"" + goalWidth + "\" fill=\"" + flagBColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right flag
        // // Center circle and mark
        // "<circle cx=\"" + centerX + "\" cy=\"" + (centerY) + "\" r=\"" + (centerCircleDiameter / 2) + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Center circle
        // "<circle cx=\"" + centerX + "\" cy=\"" + (centerY) + "\" r=\"" + (markDiameter / 2) + "\" fill=\"black\"/>" + // Center mark
        // // Halfway line
        // "<line x1=\"" + centerX + "\" y1=\"" + (outerShell) + "\" x2=\"" + centerX + "\" y2=\"" + (outerShell + fieldWidth + technicalFieldWidth) + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Halfway line
        // // Technical fields
        // "<rect x=\"0\" y=\"" + (outerShell * 2 + fieldWidth + technicalFieldWidth) + "\" width=\"" + technicalFieldLength + "\" height=\"" + technicalFieldWidth + "\" fill=\"#71BBD2\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left technical field
        // "<rect x=\"" + (svgWidth - technicalFieldLength) + "\" y=\"" + (outerShell * 2 + fieldWidth + technicalFieldWidth) + "\" width=\"" + technicalFieldLength + "\" height=\"" + technicalFieldWidth + "\" fill=\"#71BBD2\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right technical field
        // "</g></svg></div></body></html>";



        viewEngine.loadContent(htmlContent);
    }

    /**
     * Removes a specified number of player nodes from the field.
     * 
     * @param n the number of players to remove
     */
    public void removePlayers(int n) {
        for (int i = 1; i < n + 1; i++) {
            getNode().getChildren().remove(1);
        }
    }

    // Getters and setters for field dimensions and properties
    public double getA() { return A; }
    public void setA(double a) { A = a; }

    public double getB() { return B; }
    public void setB(double b) { B = b; }

    public double getC() { return C; }
    public void setC(double c) { C = c; }

    public double getD() { return D; }
    public void setD(double d) { D = d; }

    public double getE() { return E; }
    public void setE(double e) { E = e; }

    public double getF() { return F; }
    public void setF(double f) { F = f; }

    public double getG() { return G; }
    public void setG(double g) { G = g; }

    public double getH() { return H; }
    public void setH(double h) { H = h; }

    public double getI() { return I; }
    public void setI(double i) { I = i; }

    public double getJ() { return J; }
    public void setJ(double j) { J = j; }

    public double getK() { return K; }
    public void setK(double k) { K = k; }

    public double getL() { return L; }
    public void setL(double l) { L = l; }

    public double getM() { return M; }
    public void setM(double m) { M = m; }

    public double getN() { return N; }
    public void setN(double n) { N = n; }

    public double getO() { return O; }
    public void setO(double o) { O = o; }

    public double getP() { return P; }
    public void setP(double p) { P = p; }

    public double getQ() { return Q; }
    public void setQ(double q) { Q = q; }

    public double getGoaldepth() { return goaldepth; }
    public void setGoaldepth(double goaldepth) { this.goaldepth = goaldepth; }

    public double getGoalwidth() { return goalwidth; }
    public void setGoalwidth(double goalwidth) { this.goalwidth = goalwidth; }; 
}   
