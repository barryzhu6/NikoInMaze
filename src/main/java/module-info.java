module edu.dsbbproj.nikoinmaze {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens edu.dsbbproj.nikoinmaze to javafx.fxml;
    exports edu.dsbbproj.nikoinmaze;
}