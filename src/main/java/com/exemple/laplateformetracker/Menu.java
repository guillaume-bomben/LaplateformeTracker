package com.exemple.laplateformetracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends VBox {
    public ArrayList<Student> students = new ArrayList<>();
    private Connection connection;

    public Menu(Connection connection) throws SQLException {
        this.connection = connection;
        this.students.clear();
        String sql = "SELECT * FROM parents";
        PreparedStatement statement1 = connection.prepareStatement(sql);
        ResultSet resultParents = statement1.executeQuery();
        
        sql = "SELECT * FROM enfants";
        PreparedStatement statement2 = connection.prepareStatement(sql);
        ResultSet resultEnfants = statement2.executeQuery();
        while (resultParents.next()) {
            ArrayList<Integer> gradeList = new ArrayList<>();
            int ID_student = 0;
            String sql_id = "SELECT ID FROM parents WHERE first_name = ? AND last_name = ? AND age = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql_id)) {
                statement.setString(1, resultParents.getString("first_name"));
                statement.setString(2, resultParents.getString("last_name"));
                statement.setInt(3, resultParents.getInt("age"));
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    ID_student = result.getInt("ID");
                }
            }
            while (resultEnfants.next()) {
                if (resultEnfants.getInt("ID_student") == ID_student) {
                    gradeList.add(resultEnfants.getInt("grade"));    
                }
            }
            students.add(new Student(ID_student, resultParents.getString("first_name"), resultParents.getString("last_name"), resultParents.getInt("age"), gradeList));
        }
        this.setSpacing(20); // Espacement entre les éléments
        this.setAlignment(Pos.CENTER); // Centrer les éléments verticalement
        this.setPadding(new Insets(20)); // Padding autour du VBox
        this.display();
    }

    public Menu(Menu menu, Connection connection) {
        this.connection = connection;
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
            Add addScreen = new Add((Stage) this.getScene().getWindow(), this, connection);
            addScreen.show((Stage) this.getScene().getWindow());
        });
        this.getChildren().add(addButton);

        Button modifyButton = createMenuButton("Modify Student");
        modifyButton.setOnAction(e -> {
            GetStudentScreen modifyScreen = new GetStudentScreen((Stage) this.getScene().getWindow(), this, true, connection);
            modifyScreen.show((Stage) this.getScene().getWindow());
        });
        this.getChildren().add(modifyButton);

        Button deleteButton = createMenuButton("Delete Student");
        deleteButton.setOnAction(e -> {
            GetStudentScreen modifyScreen = new GetStudentScreen((Stage) this.getScene().getWindow(), this, false, connection);
            modifyScreen.show((Stage) this.getScene().getWindow());
        });
        this.getChildren().add(deleteButton);

        Button displayButton = createMenuButton("Display Students");
        displayButton.setOnAction(e -> {
            if (students.size() > 0){
                Display displayScreen = new Display((Stage) this.getScene().getWindow(), this, this.students, this.connection);
                displayScreen.show((Stage) this.getScene().getWindow());
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No students yet");
                alert.showAndWait();
            }
        });
        this.getChildren().add(displayButton);

        Button searchButton = createMenuButton("Search Student");
        searchButton.setOnAction(e -> {
            Search searchScreen = new Search((Stage) this.getScene().getWindow(), this, this.students, this.connection);
            searchScreen.show((Stage) this.getScene().getWindow());

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
