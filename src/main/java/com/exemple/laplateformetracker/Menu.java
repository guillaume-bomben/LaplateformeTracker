package com.exemple.laplateformetracker;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends VBox {
    public ArrayList<Student> students;

    public Menu() {
        this.students = new ArrayList<>();
        this.setSpacing(20); // Espacement entre les éléments
        this.setAlignment(Pos.CENTER); // Centrer les éléments verticalement
        this.setPadding(new Insets(20)); // Padding autour du VBox
        this.display();
    }

    public Menu(Menu menu){                                             // whole function until SQL
        this.students = menu.students;
        this.setSpacing(20); // Espacement entre les éléments
        this.setAlignment(Pos.CENTER); // Centrer les éléments verticalement
        this.setPadding(new Insets(20)); // Padding autour du VBox
        this.display();
    }

    public void display() {
        Label label = new Label("Choose an option:");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        this.getChildren().add(label);

        Button addButton = createMenuButton("Add Student");
        addButton.setOnAction(e -> {
            Add addScreen = new Add((Stage) this.getScene().getWindow(), this);
            addScreen.show((Stage) this.getScene().getWindow());
        });
        this.getChildren().add(addButton);

        Button modifyButton = createMenuButton("Modify Student");
        modifyButton.setOnAction(e -> {
            for (Student student : students) {
                System.out.println(student.getFirstName());
                System.out.println(student.getLastName());
                System.out.println();
            }
        });
        this.getChildren().add(modifyButton);

        Button deleteButton = createMenuButton("Delete Student");
        deleteButton.setOnAction(e -> {
            System.out.println("ascii cesar");
        });
        this.getChildren().add(deleteButton);

        Button displayButton = createMenuButton("Display Students");
        displayButton.setOnAction(e -> {
            System.out.println("display");
        });
        this.getChildren().add(displayButton);

        Button searchButton = createMenuButton("Search Student");
        searchButton.setOnAction(e -> {
            System.out.println("display");
        });
        this.getChildren().add(searchButton);

        Button quitButton = createMenuButton("Quit");
        quitButton.setOnAction(e -> {
            System.exit(0);
        });
        this.getChildren().add(quitButton);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200); // Largeur fixe pour les boutons
        button.setPrefHeight(40); // Hauteur fixe pour les boutons
        button.setStyle("-fx-font-size: 14px;");
        return button;
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(this, 400, 400); // Ajuster la taille de la scène pour une meilleure présentation
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }
}
