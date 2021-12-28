module com.uet.oop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;

    opens com.uet.oop to javafx.fxml;
    exports com.uet.oop;
    opens com.uet.oop.GraphicsControllers to javafx.fxml;
    exports com.uet.oop.GraphicsControllers;
}