module com.example.psafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires lombok;
    requires java.desktop;

    opens com.example.psafx to javafx.fxml;
    exports com.example.psafx;
    exports com.example.psafx.views;
    opens com.example.psafx.views to javafx.fxml;
}