module org.vi1ibus.courseworktimemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens org.vi1ibus.courseworktimemanagement to javafx.fxml;
    exports org.vi1ibus.courseworktimemanagement;
}