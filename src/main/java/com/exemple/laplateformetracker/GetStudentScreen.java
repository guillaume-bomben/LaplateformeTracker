package com.exemple.laplateformetracker;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GetStudentScreen extends VBox {
    private Stage primaryStage;

    public static final ObservableList data = FXCollections.observableArrayList();
    private ArrayList<Integer> grades = new ArrayList<>();
    private Menu menu;                          // menu only until SQL
    private String id;
    private boolean modify;

    public GetStudentScreen(Stage primaryStage, Menu menu, boolean modify) { // menu only until SQL
        this.menu = menu;
        this.primaryStage = primaryStage;
        this.id = "1234";
        this.modify = modify;
        this.display();
    }

    public void display() {
        VBox mainBox = new VBox(10); // Horizontal box for input components
        mainBox.setPadding(new Insets(10)); // Padding around the main VBox

        if (modify){
            Label idLabel = new Label("Enter ID to modify :");
            idLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(idLabel);
        }
        else{
            Label idLabel = new Label("Enter ID to delete :");
            idLabel.setStyle("-fx-font-size: 14px;");
            mainBox.getChildren().add(idLabel);
        }

        TextField idText = new TextField(id);
        idText.setPrefWidth(200); // Adjusted width of text field
        mainBox.getChildren().add(idText);
        idText.textProperty().addListener((observable, oldValue, newValue) -> {
            id = newValue;
        });

        String text = "";
        if (modify){
            text = "Modify";
        }
        else{
            text = "Delete";
        }
        Button backButton = createMenuButton(text);
        backButton.setMaxSize(75, 5);
        backButton.setOnAction(e -> {
            if (modify){
                Modify modify = new Modify(primaryStage, menu, Integer.parseInt(id));
                modify.show(primaryStage);
                primaryStage.setScene(modify.getScene());
            }
            else{
                for (int i = 0; i < menu.students.size(); i++) {
                    if (menu.students.get(i).getId() == Integer.parseInt(id)) {
                        menu.students.remove(i);
                    }
                }
                Menu menu = new Menu(this.menu);
                menu.show(primaryStage);
                primaryStage.setScene(menu.getScene());
            }
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
    
    private void gradeWindow(GetStudentScreen add){
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
