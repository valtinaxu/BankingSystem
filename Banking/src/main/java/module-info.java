module banking.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.java;

    opens login.bankingsystem to javafx.fxml;
    exports login.bankingsystem;

    opens createaccount to javafx.fxml;
    exports createaccount;

    opens forgotpsw to javafx.fxml;
    exports forgotpsw;

    opens dashboard to javafx.fxml;
    exports dashboard;

    opens accountinfo to javafx.fxml;
    exports accountinfo;

    opens withdraw to javafx.fxml;
    exports withdraw;

    opens deposit to javafx.fxml;
    exports deposit;
}