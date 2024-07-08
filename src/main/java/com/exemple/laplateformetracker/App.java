package com.exemple.laplateformetracker;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Menu menu = new Menu();
        menu.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}