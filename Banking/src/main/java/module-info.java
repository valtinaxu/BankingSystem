module banking.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens login.bankingsystem to javafx.fxml;
    exports login.bankingsystem;
}