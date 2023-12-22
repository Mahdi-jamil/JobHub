package com.jobhub.controller;

import com.jobhub.DataBase.RootConnection;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private RadioButton jobSeeker;
    @FXML
    private Label errorMsg;
    private String selectedRole = "";

    public void goToRegisterPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/jobhub/register.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Register Now");
        stage.setScene(scene);
        stage.show();
    }

    public void selectRole() {
        selectedRole = jobSeeker.isSelected() ? "job-seeker" : "recruiter";
    }

    public void login(ActionEvent event) {
        String enteredUsername = username.getText().trim();
        String enteredPassword = password.getText().trim();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty() || selectedRole.isEmpty()) {
            showAlert();
            return;
        }

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() {
                checkAccount(enteredUsername, enteredPassword, event);
                return null;
            }
        };
        new Thread(loginTask).start();
    }

    private void checkAccount(String enteredUsername, String enteredPassword, ActionEvent event) {

        Platform.runLater(() -> {
            try {
                Connection connection = RootConnection.getConnection();
                String table = (selectedRole.equals("job-seeker") ? "job_seeker" : "recruiter");
                String query = String.format("SELECT %s,Username, password FROM %s",(table.equals("recruiter") ? "rid" : "jid"), table);

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        String user = resultSet.getString("Username");
                        String hashedPassword = resultSet.getString("password");
                        Integer id = resultSet.getInt((table.equals("recruiter") ? "rid" : "jid"));

                        if (user.equals(enteredUsername) && BCrypt.checkpw(enteredPassword, hashedPassword)) {
                            goToHome(event, table, enteredUsername,id);
                            return;
                        }
                    }
                    errorMsg.setText("Wrong Username or Password");
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    private void goToHome(ActionEvent event, String role, String name,Integer id) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/Home.fxml"));
        Parent root = fxmlLoader.load();

        HomeController controller = fxmlLoader.getController();
        controller.createUser(name, role,id);
        controller.search();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Form Not Completed");
        alert.setContentText("Fill all fields");
        alert.show();
    }


}
