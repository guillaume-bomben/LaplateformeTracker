package com.exemple.laplateformetracker;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    @Override
    
    public void start(Stage primaryStage) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            System.out.println("Connection to database established successfully.");

            Menu menu = new Menu(connection); // Assuming Menu class accepts a Connection parameter
            menu.show(primaryStage);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
            
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
