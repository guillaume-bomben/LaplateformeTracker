package com.exemple.laplateformetracker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPane extends VBox {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    private Connection connection;

    public LoginPane(Connection connection) {
        this.connection = connection;
        setupUI();
        setupActions();
    }

    private void setupUI() {
        Label titleLabel = new Label("Connexion");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameField = new TextField();

        Label passwordLabel = new Label("Mot de passe:");
        passwordField = new PasswordField();

        loginButton = new Button("Se connecter");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);

        this.getChildren().addAll(titleLabel, gridPane);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
    }

    private void setupActions() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                try {
                    if (authenticate(username, password)) {
                        System.out.println("Login successful!");

                        // Pass the connection to the Menu and show it
                        Menu menu = new Menu(connection);
                        Stage stage = (Stage) getScene().getWindow(); // Get the current stage
                        menu.show(stage);
                    } else {
                        System.out.println("Login failed. Invalid username or password.");
                    }
                } catch (SQLException e) {
                    System.err.println("Error during authentication: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Please enter both username and password.");
            }
        });
    }

    private boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM parents WHERE user_name = ? AND MP = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // true if user exists with the given username and password
        }
    }
}
