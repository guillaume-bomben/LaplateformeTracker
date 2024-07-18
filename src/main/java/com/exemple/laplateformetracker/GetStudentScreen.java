package com.exemple.laplateformetracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private Connection connection;


    public GetStudentScreen(Stage primaryStage, Menu menu, boolean modify, Connection connection) { // menu only until SQL
        this.menu = menu;
        this.primaryStage = primaryStage;
        this.id = "1234";
        this.modify = modify;
        this.connection = connection;
        this.display();
    }

public void display() {
    VBox mainBox = new VBox(10);
    mainBox.setPadding(new Insets(10));

    Label idLabel;
    if (modify) {
        idLabel = new Label("Enter ID to modify:");
    } else {
        idLabel = new Label("Enter ID to delete:");
    }
    idLabel.setStyle("-fx-font-size: 14px;");
    mainBox.getChildren().add(idLabel);

    TextField idText = new TextField(id);
    idText.setPrefWidth(200);
    mainBox.getChildren().add(idText);
    idText.textProperty().addListener((observable, oldValue, newValue) -> {
        id = newValue;
    });

    String buttonText = modify ? "Modify" : "Delete";
    Button actionButton = createMenuButton(buttonText);
    actionButton.setMaxSize(75, 5);
    actionButton.setOnAction(e -> {
        if (modify) {
            Modify modify = new Modify(primaryStage, menu, Integer.parseInt(id), connection);
            modify.show(primaryStage);
            primaryStage.setScene(modify.getScene());
        } else {
            // Delete student from database
            String deleteStudentQuery = "DELETE FROM parents WHERE ID = ?";
            String deleteGradesQuery = "DELETE FROM enfants WHERE ID_student = ?";
            
            try {
                connection.setAutoCommit(false); // Start transaction

                // Delete from enfants table
                try (PreparedStatement deleteGradesPstmt = connection.prepareStatement(deleteGradesQuery)) {
                    deleteGradesPstmt.setInt(1, Integer.parseInt(id));
                    int gradesDeleted = deleteGradesPstmt.executeUpdate();
                    System.out.println("Deleted grades: " + gradesDeleted);
                }

                // Delete from parents table
                try (PreparedStatement deleteStudentPstmt = connection.prepareStatement(deleteStudentQuery)) {
                    deleteStudentPstmt.setInt(1, Integer.parseInt(id));
                    int studentDeleted = deleteStudentPstmt.executeUpdate();
                    System.out.println("Deleted student: " + studentDeleted);
                }

                connection.commit(); // Commit transaction
                System.out.println("Transaction committed successfully.");
            } catch (SQLException ex) {
                try {
                    connection.rollback(); // Rollback transaction in case of error
                    System.out.println("Transaction rolled back due to error.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                ex.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit mode
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            // Show menu after deletion
            Menu menu = new Menu(this.menu, connection);
            menu.show(primaryStage);
            primaryStage.setScene(menu.getScene());
        }
    });
    mainBox.getChildren().add(actionButton);

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
