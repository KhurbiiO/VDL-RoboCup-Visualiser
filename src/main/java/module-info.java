module visualizer.match_viewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires java.desktop; 

    opens visualizer.match_viewer to javafx.fxml;
    exports visualizer.match_viewer;
}