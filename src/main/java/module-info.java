module com.amg.os {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.amg.os to javafx.fxml;

    // exports com.amg.os to javafx.graphics;
    exports com.amg.os to javafx.graphics;
    exports com.amg.os.util.storage;
    exports com.amg.os.controllers to javafx.graphics;
    opens com.amg.os.controllers to javafx.fxml;

}