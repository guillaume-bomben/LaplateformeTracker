package com.exemple.laplateformetracker;

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

    public static final ObservableList data = FXCollections.observableArrayList();
    private ArrayList<Student> students = new ArrayList<>();
    private Menu menu;                          // menu only until SQL
    private Student selectedStudent;
    private int selectedIndex;

    public Display(Stage primaryStage, Menu menu, ArrayList<Student> students) { // menu only until SQL
        this.menu = menu;
        this.primaryStage = primaryStage;
        this.students = students;
        data.clear();
        for (int i = 0; i < students.size(); i++) {
            data.add(students.get(i).getId());
        }
        this.selectedStudent = students.get(0);
        this.selectedIndex = 0;
        this.display();
    }

    public void display() {
        VBox mainBox = new VBox(10); // Horizontal box for input components
        mainBox.setPadding(new Insets(10)); // Padding around the main VBox

        ListView listView = new ListView(data);
        listView.setMaxSize(100, 500);
        listView.setEditable(true);
        listView.getSelectionModel().select(selectedIndex);

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onSelected(listView));
        this.getChildren().add(listView);

        Label idLabel = new Label("ID Number :");
        idLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(idLabel);

        Label idText = new Label(selectedStudent.getId() + "");
        idText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(idText);

        Label fNameLabel = new Label("First Name :");
        fNameLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(fNameLabel);

        Label fNameText = new Label(selectedStudent.getFirstName());
        fNameText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(fNameText);

        Label lNameLabel = new Label("Last Name :");
        lNameLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(lNameLabel);

        Label lNameText = new Label(selectedStudent.getLastName());
        lNameText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(lNameText);

        Label ageLabel = new Label("Age :");
        ageLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(ageLabel);

        Label ageText = new Label(selectedStudent.getAge() + "");
        ageText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(ageText);

        Label gradesLabel = new Label("Grades :");
        gradesLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(gradesLabel);

        String gradeText = "";
        for (int i = 0; i < selectedStudent.getGrades().size()-1; i++) {
            gradeText += selectedStudent.getGrades().get(i) + ", ";
        }
        gradeText += selectedStudent.getGrades().get(selectedStudent.getGrades().size()-1);

        Label gradeLabel = new Label(gradeText);
        gradeLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(gradeLabel);

        Button backButton = createMenuButton("Back");
        backButton.setMaxSize(75, 5);
        backButton.setOnAction(e -> {
            Menu menu = new Menu(this.menu); // until SQL
            menu.show(primaryStage);
            primaryStage.setScene(menu.getScene());
        });
        mainBox.getChildren().add(backButton);

        this.getChildren().add(mainBox);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200); // Largeur fixe pour les boutons
        button.setPrefHeight(40); // Hauteur fixe pour les boutons
        button.setStyle("-fx-font-size: 14px;");
        return button;
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(this, 700, 600); // Ajuster la taille de la scène pour une meilleure présentation
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Display Student");
        primaryStage.show();
    }

    private void onSelected(ListView listView){
        int id = (int)listView.getSelectionModel().getSelectedItem();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id){
                selectedStudent = students.get(i);
                selectedIndex = i;
            }
        }

        this.getChildren().clear();
        this.display();
    }
}
