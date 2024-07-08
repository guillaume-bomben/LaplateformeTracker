module com.exemple.laplateformetracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.exemple.laplateformetracker to javafx.fxml;
    exports com.exemple.laplateformetracker;
}