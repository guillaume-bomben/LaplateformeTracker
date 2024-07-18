package com.exemple.laplateformetracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            System.out.println("Connection to database established successfully.");

            LoginPane loginPane = new LoginPane(connection);
            Scene scene = new Scene(loginPane, 400, 300);

            primaryStage.setTitle("Page de connexion");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
