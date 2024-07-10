package components;

import java.io.File;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private String fieldColor;
    private String restingAreaColor;

    private double width;
    private double height;

    private WebView view;
    private WebEngine viewEngine;

    /**
     * Default constructor for Field.
     * Initializes field dimensions and properties.
     */
    public Field() {
        super();
    }


    /**
     * Loads the field configuration from a JSON file.
     *
     * @param jsonFilePath the path to the JSON file
     */
    public void loadConfigFromJson(String jsonFilePath) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonFilePath));
        JsonNode measurements = root.path("measurements");
        this.goalwidth = measurements.path("goalWidth").asDouble();
        this.goaldepth = measurements.path("goalDepth").asDouble();
        this.A = measurements.path("A").asDouble();
        this.B = measurements.path("B").asDouble();
        this.C = measurements.path("C").asDouble();
        this.D = measurements.path("D").asDouble();
        this.E = measurements.path("E").asDouble();
        this.F = measurements.path("F").asDouble();
        this.G = measurements.path("G").asDouble();
        this.H = measurements.path("H").asDouble();
        this.I = measurements.path("I").asDouble();
        this.J = measurements.path("J").asDouble();
        this.K = measurements.path("K").asDouble();
        this.L = measurements.path("L").asDouble();
        this.M = measurements.path("M").asDouble();
        this.N = measurements.path("N").asDouble();
        this.O = measurements.path("O").asDouble();
        this.P = measurements.path("P").asDouble();
        this.Q = measurements.path("Q").asDouble();

        JsonNode appearance = root.path("appearance");
        this.fieldColor = appearance.path("color").asText();
        this.restingAreaColor = appearance.path("raColor").asText();

        width = B + 2 * L + 2 * M;
        height = A + 2 * L;
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
     */
    public void drawField(String flagAColor, String flagBColor) {

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
        // double svgWidth = fieldHeight + outerShell*2;
        double svgWidth = height * scale;
        // double svgHeight = fieldWidth + outerShell*2 + technicalFieldWidth*2;
        double svgHeight = width * scale;

        // Calculate positions based on scaled dimensions
        double centerX = svgWidth/2;
        double centerY = svgHeight/2;

        String htmlContent = "<html><body style='margin:0;padding:0;overflow:hidden; background:#52494a'>" +
        "<div style='width:100vw;height:100vh;display:flex;justify-content:center;align-items:center;'>" +
        "<svg viewBox=\"0 0 " + svgWidth + " " + svgHeight + "\" style=\"width:100%; height:100%;background:#ffffff'\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
        // "<g transform=\"rotate(-90 " + centerX + " " + centerY + ")\">" +
        // Outer field
        "<rect x=\"0\" y=\"0\" width=\"" + (fieldHeight + outerShell*2) + "\" height=\"" + (fieldWidth + outerShell*2 + technicalFieldWidth*2) + "\" fill=\"" + fieldColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" +
        "<rect x=\"0\" y=\"0\" width=\"" + svgWidth + "\" height=\"" + technicalFieldWidth + "\" fill=\"#000000\" />" + // Top ghost field
        "<rect x=\"" + outerShell + "\" y=\"" + (outerShell + technicalFieldWidth) + "\" width=\"" + fieldHeight + "\" height=\"" + fieldWidth + "\" fill=\"" + fieldColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" +
        // Penalty areas
        "<rect x=\"" + outerShell + "\" y=\"" + (centerY - penaltyAreaWidth/2) + "\" width=\"" + penaltyAreaDepth + "\" height=\"" + penaltyAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left penalty area
        "<rect x=\"" + (outerShell + fieldHeight - penaltyAreaDepth) + "\" y=\"" + (centerY - penaltyAreaWidth/2) + "\" width=\"" + penaltyAreaDepth + "\" height=\"" + penaltyAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right penalty area
        // Goal areas
        "<rect x=\"" + outerShell + "\" y=\"" + (centerY - goalAreaWidth/2 ) + "\" width=\"" + goalAreaDepth + "\" height=\"" + goalAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left goal area
        "<rect x=\"" + (outerShell + fieldHeight - goalAreaDepth) + "\" y=\"" + (centerY - goalAreaWidth/2 ) + "\" width=\"" + goalAreaDepth + "\" height=\"" + goalAreaWidth + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right goal area
        // Flags
        "<rect x=\"" + (outerShell - goalDepth) + "\" y=\"" + (centerY - goalWidth/2) + "\" width=\"" + goalDepth + "\" height=\"" + goalWidth + "\" fill=\"" + flagAColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left flag
        "<rect x=\"" + (outerShell + fieldHeight) + "\" y=\"" + (centerY - goalWidth/2) + "\" width=\"" + goalDepth + "\" height=\"" + goalWidth + "\" fill=\"" + flagBColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right flag
        // Center circle and mark
        "<circle cx=\"" + centerX + "\" cy=\"" + (centerY) + "\" r=\"" + (centerCircleDiameter / 2) + "\" fill=\"none\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Center circle
        "<circle cx=\"" + centerX + "\" cy=\"" + (centerY) + "\" r=\"" + (markDiameter / 2) + "\" fill=\"black\"/>" + // Center mark
        // Halfway line
        "<line x1=\"" + centerX + "\" y1=\"" + (outerShell + technicalFieldWidth) + "\" x2=\"" + centerX + "\" y2=\"" + (outerShell + fieldWidth + technicalFieldWidth) + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Halfway line
        // Technical fields
        "<rect x=\"0\" y=\"" + (outerShell * 2 + fieldWidth + technicalFieldWidth) + "\" width=\"" + technicalFieldLength + "\" height=\"" + technicalFieldWidth + "\" fill=\""+ restingAreaColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Left technical field
        "<rect x=\"" + (svgWidth - technicalFieldLength) + "\" y=\"" + (outerShell * 2 + fieldWidth + technicalFieldWidth) + "\" width=\"" + technicalFieldLength + "\" height=\"" + technicalFieldWidth + "\" fill=\"" + restingAreaColor + "\" stroke=\"black\" stroke-width=\"" + lineWidth + "\"/>" + // Right technical field
        "</svg></div></body></html>";

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
    public double getB() { return B; }
    public double getC() { return C; }
    public double getD() { return D; }
    public double getE() { return E; }
    public double getF() { return F; }
    public double getG() { return G; }
    public double getH() { return H; }
    public double getI() { return I; }
    public double getJ() { return J; }
    public double getK() { return K; }
    public double getL() { return L; }
    public double getM() { return M; }
    public double getN() { return N; }
    public double getO() { return O; }
    public double getP() { return P; }
    public double getQ() { return Q; }
    public double getGoaldepth() { return goaldepth; }
    public double getGoalwidth() { return goalwidth; }
    public String getFieldColor() { return fieldColor; }
    public String getRestingAreaColor() { return restingAreaColor; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }
}   
