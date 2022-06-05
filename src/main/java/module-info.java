module com.amg.os {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.amg.os to javafx.fxml;
  ///  exports com.amg.os;
}