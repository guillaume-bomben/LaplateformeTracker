package com.exemple.laplateformetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Search extends VBox {
    private Stage primaryStage;
    private ArrayList<Student> students;
    private Menu menu;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField ageField;
    private ListView<String> studentListView;
    private Connection connection;

    public Search(Stage primaryStage, Menu menu, ArrayList<Student> students, Connection connection) {
        this.primaryStage = primaryStage;
        this.menu = menu;
        this.students = students;
        this.connection = connection;

        this.display();
    }

    public void display() {
        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        ageField = new TextField();
        ageField.setPromptText("Age");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> filterStudents());

        HBox filters = new HBox(10, new Label("First Name:"), firstNameField, new Label("Last Name:"), lastNameField, new Label("Age:"), ageField, searchButton);
        
        studentListView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        studentListView.setItems(items);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Menu menu = new Menu(this.menu, connection); // until SQL
            menu.show(primaryStage);
            primaryStage.setScene(menu.getScene());
        });

        this.getChildren().addAll(filters, studentListView, backButton);
    }

    private void filterStudents() {
        String firstName = firstNameField.getText().trim().toLowerCase();
        String lastName = lastNameField.getText().trim().toLowerCase();
        String ageText = ageField.getText().trim();

        List<Student> filteredStudents = students.stream()
                .filter(s -> (firstName.isEmpty() || s.getFirstName().toLowerCase().contains(firstName)) &&
                             (lastName.isEmpty() || s.getLastName().toLowerCase().contains(lastName)) &&
                             (ageText.isEmpty() || String.valueOf(s.getAge()).equals(ageText)))
                .collect(Collectors.toList());

        ObservableList<String> items = FXCollections.observableArrayList(
                filteredStudents.stream()
                        .map(s -> s.getFirstName() + " " + s.getLastName() + ", Age: " + s.getAge() + ", Grades: " + s.getGrades())
                        .collect(Collectors.toList())
        );
        studentListView.setItems(items);
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(this, 700, 600); // Ajuster la taille de la scène pour une meilleure présentation
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Display Student");
        primaryStage.show();
    }
}
