package com.exemple.laplateformetracker;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Add extends VBox {
    private Stage primaryStage;

    public static final ObservableList data = FXCollections.observableArrayList();
    private ArrayList<Integer> grades = new ArrayList<>();
    private Menu menu;                          // menu only until SQL
    private String id;
    private String fName;
    private String lName;
    private String age;

    public Add(Stage primaryStage, Menu menu) { // menu only until SQL
        this.menu = menu;
        this.primaryStage = primaryStage;
        this.id = "1234";
        this.fName = "John";
        this.lName = "Cena";
        this.age = "47";
        this.grades = new ArrayList<>();
        this.display();
    }

    public void display() {
        VBox mainBox = new VBox(10); // Horizontal box for input components
        mainBox.setPadding(new Insets(10)); // Padding around the main VBox

        Label idLabel = new Label("ID Number :");
        idLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(idLabel);

        TextField idText = new TextField(id);
        idText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(idText);
        idText.textProperty().addListener((observable, oldValue, newValue) -> {
            id = newValue;
        });

        Label fNameLabel = new Label("First Name :");
        fNameLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(fNameLabel);

        TextField fNameText = new TextField(fName);
        fNameText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(fNameText);
        fNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            fName = newValue;
        });

        Label lNameLabel = new Label("Last Name :");
        lNameLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(lNameLabel);

        TextField lNameText = new TextField(lName);
        lNameText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(lNameText);
        lNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            lName = newValue;
        });

        Label ageLabel = new Label("Age :");
        ageLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(ageLabel);

        TextField ageText = new TextField(age);
        ageText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(ageText);
        ageText.textProperty().addListener((observable, oldValue, newValue) -> {
            age = newValue;
        });

        Label gradeLabel = new Label("Grades :");
        gradeLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(gradeLabel);

        ListView listView = new ListView(data);
        listView.setPrefSize(200, 250);
        listView.setEditable(true);
        
        data.clear();
        for (int i = 0; i < grades.size(); i++) {
            data.add(grades.get(i));
        }
          
        listView.setItems(data);
        mainBox.getChildren().add(listView);

        Button plusButton = createMenuButton("+");
        plusButton.setMaxSize(5, 5);
        plusButton.setOnAction(e -> {
            gradeWindow(this);
        });
        mainBox.getChildren().add(plusButton);

        Button backButton = createMenuButton("Add Student");
        backButton.setMaxSize(75, 5);
        backButton.setOnAction(e -> {
            Student s = new Student(Integer.valueOf(idText.getText()), fNameText.getText(), lNameText.getText(), Integer.valueOf(ageText.getText()), grades);
            Menu menu = new Menu(this.menu); // until SQL
            menu.students.add(s);            // until SQL
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
        Scene scene = new Scene(this, 400, 500); // Ajuster la taille de la scène pour une meilleure présentation
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add Student");
        primaryStage.show();
    }

    public void addStudent(){
        //TODO: SQL
    }
    
    private void gradeWindow(Add add){
        Stage newWindow = new Stage();
        newWindow.setTitle("New Scene");
        
        VBox container = new VBox();
        container.setSpacing(15);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.CENTER);

        Label gradeLabel = new Label("Grade :");
        gradeLabel.setStyle("-fx-font-size: 14px;");
        container.getChildren().add(gradeLabel);

        TextField gradeText = new TextField("47");
        gradeText.setPrefWidth(200); // Adjusted width of text field
        container.getChildren().add(gradeText);

        Button backButton = createMenuButton("Add Grade");
        backButton.setMaxSize(75, 5);
        backButton.setOnAction(e -> {
            grades.add(Integer.parseInt(gradeText.getText()));
            newWindow.close();
            add.getChildren().clear();
            add.display();
        });
        container.getChildren().add(backButton);

        newWindow.setScene(new Scene(container));
        newWindow.show();
    }
}
