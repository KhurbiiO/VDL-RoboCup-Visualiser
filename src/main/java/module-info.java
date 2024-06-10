module visualizer.match_viewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens visualizer.match_viewer to javafx.fxml;
    exports visualizer.match_viewer;
    requires org.bytedeco.javacpp;

}