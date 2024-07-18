package com.exemple.laplateformetracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Add extends VBox {
    private Stage primaryStage;
    private Connection connection;

    public static final ObservableList<Integer> data = FXCollections.observableArrayList();
    private ArrayList<Integer> grades = new ArrayList<>();
    private Menu menu;
    private String fName;
    private String lName;
    private String age;
    private int ID_student = 0;


    public Add(Stage primaryStage, Menu menu, Connection connection) {
        this.menu = menu;
        this.primaryStage = primaryStage;
        this.connection = connection;
        this.fName = "John";
        this.lName = "Cena";
        this.age = "47";
        this.grades = new ArrayList<>();
        this.display();
    }

    public void display() {
        VBox mainBox = new VBox(10); // Horizontal box for input components
        mainBox.setPadding(new Insets(10)); // Padding around the main VBox

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

        ListView<Integer> listView = new ListView<>(data);
        listView.setPrefSize(200, 250);
        listView.setEditable(true);

        data.clear();
        data.addAll(grades);

        listView.setItems(data);
        mainBox.getChildren().add(listView);

        Button minusButton = createMenuButton("-");
        minusButton.setMaxSize(5, 5);
        minusButton.setOnAction(e -> {
            gradeWindow(this, false);
        });
        mainBox.getChildren().add(minusButton);

        Button plusButton = createMenuButton("+");
        plusButton.setMaxSize(5, 5);
        plusButton.setOnAction(e -> {
            gradeWindow(this, true);
        });
        mainBox.getChildren().add(plusButton);

        Button backButton = createMenuButton("Add Student");
        backButton.setMaxSize(75, 5);
        backButton.setOnAction(e -> {
            if (grades.size() > 0) {
                try {
                    addStudent();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Student added successfully");
                    alert.showAndWait();

                    Menu menu = new Menu(this.menu, connection);
                    menu.students.add(new Student(ID_student, fName, lName, Integer.parseInt(age), grades));
                    menu.show(primaryStage);
                    primaryStage.setScene(menu.getScene());
                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error adding student to the database: " + ex.getMessage());
                    alert.showAndWait();
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No grades yet");
                alert.showAndWait();
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

    public void addStudent() throws SQLException {

        String sql_check = "SELECT ID FROM parents WHERE first_name = ? AND last_name = ? AND age = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql_check)) {
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setInt(3, Integer.parseInt(age));
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                ID_student = result.getInt("ID");
            }
        }

        if (ID_student == 0){
            String sql = "INSERT INTO parents (first_name, last_name, age) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, fName);
                statement.setString(2, lName);
                statement.setInt(3, Integer.parseInt(age));
                statement.executeUpdate();
            }
        
            String sql_id = "SELECT ID FROM parents WHERE first_name = ? AND last_name = ? AND age = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql_id)) {
                statement.setString(1, fName);
                statement.setString(2, lName);
                statement.setInt(3, Integer.parseInt(age));
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    ID_student = result.getInt("ID");
                }
            }
        
            for (int grade : grades) {
                String gradeSql = "INSERT INTO enfants (ID_student, grade) VALUES (?, ?)";
                try (PreparedStatement gradeStatement = connection.prepareStatement(gradeSql)) {
                    gradeStatement.setInt(1, ID_student);
                    gradeStatement.setInt(2, grade);
                    gradeStatement.executeUpdate();
                }
            }
        }
    }
    

    private void gradeWindow(Add add, boolean addGrade) {
        if (addGrade) {
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
        } else {
            grades.remove(grades.size() - 1);
            add.getChildren().clear();
            add.display();
        }
    }
}
