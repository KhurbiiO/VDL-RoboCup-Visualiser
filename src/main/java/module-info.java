module visualizer.match_viewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;
    opens visualizer.match_viewer to javafx.fxml;
    exports visualizer.match_viewer;
}