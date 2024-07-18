package com.exemple.laplateformetracker;

import java.sql.Connection;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Display extends VBox {
    private Stage primaryStage;
    private Connection connection;
    private ArrayList<Student> students;
    private Menu menu;
    private Student selectedStudent;
    private int selectedIndex;

    public static final ObservableList<Integer> data = FXCollections.observableArrayList();

    public Display(Stage primaryStage, Menu menu, ArrayList<Student> students, Connection connection) {
        this.primaryStage = primaryStage;
        this.menu = menu;
        this.students = students;
        this.connection = connection;
        this.selectedStudent = students.size() > 0 ? students.get(0) : null;
        this.selectedIndex = 0;

        data.clear();
        for (Student student : students) {
            data.add(student.getId());
        }

        this.display();
    }

    public void display() {
        VBox mainBox = new VBox(10); // Horizontal box for input components
        mainBox.setPadding(new Insets(10)); // Padding around the main VBox

        ListView<Integer> listView = new ListView<>(data);
        listView.setMaxSize(100, 500);
        listView.setEditable(true);
        listView.getSelectionModel().select(selectedIndex);

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onSelected(listView));
        this.getChildren().add(listView);

        if (selectedStudent != null) {
            Label idLabel = new Label("ID Number :");
            idLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(idLabel);

            Label idText = new Label(String.valueOf(selectedStudent.getId()));
            idText.setPrefWidth(200);
            mainBox.getChildren().add(idText);

            Label fNameLabel = new Label("First Name :");
            fNameLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(fNameLabel);

            Label fNameText = new Label(selectedStudent.getFirstName());
            fNameText.setPrefWidth(200);
            mainBox.getChildren().add(fNameText);

            Label lNameLabel = new Label("Last Name :");
            lNameLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(lNameLabel);

            Label lNameText = new Label(selectedStudent.getLastName());
            lNameText.setPrefWidth(200);
            mainBox.getChildren().add(lNameText);

            Label ageLabel = new Label("Age :");
            ageLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(ageLabel);

            Label ageText = new Label(String.valueOf(selectedStudent.getAge()));
            ageText.setPrefWidth(200);
            mainBox.getChildren().add(ageText);

            Label gradesLabel = new Label("Grades :");
            gradesLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(gradesLabel);

            StringBuilder gradeText = new StringBuilder();
            for (int i = 0; i < selectedStudent.getGrades().size(); i++) {
                gradeText.append(selectedStudent.getGrades().get(i));
                if (i < selectedStudent.getGrades().size() - 1) {
                    gradeText.append(", ");
                }
            }

            Label gradeLabel = new Label(gradeText.toString());
            gradeLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(gradeLabel);
        } else {
            Label noStudentLabel = new Label("No student selected");
            noStudentLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(noStudentLabel);
        }

        Button backButton = createMenuButton("Back");
        backButton.setMaxSize(75, 5);
        backButton.setOnAction(e -> {
            Menu menu = new Menu(this.menu, connection);
            menu.show(primaryStage);
            primaryStage.setScene(menu.getScene());
        });
        mainBox.getChildren().add(backButton);

        this.getChildren().add(mainBox);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle("-fx-font-size: 14px;");
        return button;
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(this, 700, 600);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Display Student");
        primaryStage.show();
    }

    private void onSelected(ListView<Integer> listView) {
        int id = listView.getSelectionModel().getSelectedItem();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                selectedStudent = students.get(i);
                selectedIndex = i;
                break;
            }
        }

        this.getChildren().clear();
        this.display();
    }
}
