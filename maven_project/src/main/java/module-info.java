module Aplikasi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens Aplikasi to javafx.fxml;
    exports Aplikasi;
}
