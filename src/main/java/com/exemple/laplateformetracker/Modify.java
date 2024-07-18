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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Modify extends VBox {
    private Stage primaryStage;
    private Connection connection;

    public static final ObservableList data = FXCollections.observableArrayList();
    private ArrayList<Integer> grades = new ArrayList<>();
    private Menu menu;
    private int id;
    private String fName;
    private String lName;
    private String age;

    public Modify(Stage primaryStage, Menu menu, int id, Connection connection) {
        this.menu = menu;
        this.primaryStage = primaryStage;
        this.id = id;
        this.connection = connection;
        for (Student student : menu.students) {
            if (id == student.getId()){
                this.fName = student.getFirstName();
                this.lName = student.getLastName();
                this.age = student.getAge() + "";
                this.grades = student.getGrades();
            }
        }
        this.display();
    }

    public void display() {
        VBox mainBox = new VBox(10);
        mainBox.setPadding(new Insets(10));

        Label idLabel = new Label("ID Number :");
        idLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(idLabel);

        TextField idText = new TextField(id + "");
        idText.setPrefWidth(200);
        mainBox.getChildren().add(idText);
        idText.textProperty().addListener((observable, oldValue, newValue) -> {
            id = Integer.parseInt(newValue);
        });

        Label fNameLabel = new Label("First Name :");
        fNameLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(fNameLabel);

        TextField fNameText = new TextField(fName);
        fNameText.setPrefWidth(200);
        mainBox.getChildren().add(fNameText);
        fNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            fName = newValue;
        });

        Label lNameLabel = new Label("Last Name :");
        lNameLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(lNameLabel);

        TextField lNameText = new TextField(lName);
        lNameText.setPrefWidth(200);
        mainBox.getChildren().add(lNameText);
        lNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            lName = newValue;
        });

        Label ageLabel = new Label("Age :");
        ageLabel.setStyle("-fx-font-size: 14px;");
        mainBox.getChildren().add(ageLabel);

        TextField ageText = new TextField(age);
        ageText.setPrefWidth(200);
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

        Button minusButton = createMenuButton("-");
        minusButton.setMaxSize(5, 5);
        minusButton.setOnAction(e -> gradeWindow(this, false));
        mainBox.getChildren().add(minusButton);

        Button plusButton = createMenuButton("+");
        plusButton.setMaxSize(5, 5);
        plusButton.setOnAction(e -> gradeWindow(this, true));
        mainBox.getChildren().add(plusButton);

        Button modifyButton = createMenuButton("Modify Student");
        modifyButton.setMaxSize(75, 5);
        modifyButton.setOnAction(e -> {
            updateStudentInDatabase(id, fName, lName, Integer.parseInt(age), grades);
            Menu menu = new Menu(this.menu, connection);
            menu.show(primaryStage);
            primaryStage.setScene(menu.getScene());
        });
        mainBox.getChildren().add(modifyButton);

        this.getChildren().add(mainBox);
    }

    private void updateStudentInDatabase(int id, String firstName, String lastName, int age, ArrayList<Integer> grades) {
        String updateQuery = "UPDATE parents SET first_name = ?, last_name = ?, age = ? WHERE ID = ?";
        String deleteGradesQuery = "DELETE FROM enfants WHERE ID_student = ?";
        String insertGradesQuery = "INSERT INTO enfants (ID_student, grade) VALUES (?, ?)";
    
        try {
            connection.setAutoCommit(false); // Start transaction
    
            // Update student details
            try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setInt(3, age);
                pstmt.setInt(4, id);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Updated rows in parents table: " + rowsAffected);
            }
    
            // Delete old grades
            try (PreparedStatement deletePstmt = connection.prepareStatement(deleteGradesQuery)) {
                deletePstmt.setInt(1, id);
                int rowsDeleted = deletePstmt.executeUpdate();
                System.out.println("Deleted rows in enfants table: " + rowsDeleted);
            }
    
            // Insert new grades
            try (PreparedStatement insertPstmt = connection.prepareStatement(insertGradesQuery)) {
                for (Integer grade : grades) {
                    insertPstmt.setInt(1, id);
                    insertPstmt.setInt(2, grade);
                    insertPstmt.addBatch();
                }
                int[] batchResults = insertPstmt.executeBatch();
                System.out.println("Inserted rows in enfants table: " + batchResults.length);
            }
    
            connection.commit(); // Commit transaction
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction in case of error
                System.out.println("Transaction rolled back due to error.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle("-fx-font-size: 14px;");
        return button;
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(this, 400, 500);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Modify Student");
        primaryStage.show();
    }

    private void gradeWindow(Modify modify, boolean addGrade) {
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
            gradeText.setPrefWidth(200);
            container.getChildren().add(gradeText);

            Button addButton = createMenuButton("Add Grade");
            addButton.setMaxSize(75, 5);
            addButton.setOnAction(e -> {
                grades.add(Integer.parseInt(gradeText.getText()));
                newWindow.close();
                modify.getChildren().clear();
                modify.display();
            });
            container.getChildren().add(addButton);

            newWindow.setScene(new Scene(container));
            newWindow.show();
        } else {
            grades.remove(grades.size() - 1);
            modify.getChildren().clear();
            modify.display();
        }
    }
}
