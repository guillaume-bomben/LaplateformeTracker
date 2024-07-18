module com.exemple.laplateformetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.exemple.laplateformetracker to javafx.fxml;
    exports com.exemple.laplateformetracker;
}