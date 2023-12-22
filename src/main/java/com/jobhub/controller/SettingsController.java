package com.jobhub.controller;

import com.jobhub.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class SettingsController {
    public PasswordField newPassword;
    public PasswordField confirmPassword;
    public PasswordField oldPassword;
    public Button recruiterButton;
    private User user;

    public void setUser(User user) {
        this.user = user;
        if(user.getRole().equals("job_seeker"))recruiterButton.setVisible(false);
    }

    public void ChangePassword(ActionEvent event) throws IOException {
        String newPasswordText = newPassword.getText();
        String confirmPasswordText = confirmPassword.getText();
        String oldPasswordText = oldPassword.getText();

        if (!isValidPassword(newPasswordText)) {
            showAlert("Enter more than 8 character ");
            return;
        }
        if(!doPasswordsMatch(newPasswordText, confirmPasswordText)){
            showAlert("Passwords do not match");
            return;
        }
        if(!isOldPasswordCorrect(oldPasswordText)){
            showAlert("Old password is incorrect");
            return;
        }
        callChangePasswordSP(user.getUsername(), newPasswordText, user.getRole());
        Cancel(event);
    }

    private void callChangePasswordSP(String username, String newPassword, String role) {
        Connection connection = user.getConnection();
        String spCall = "{call ChangePassword_Sp(?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(spCall)) {
            callableStatement.setString(1, username);
            callableStatement.setString(2, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            callableStatement.setString(3, role);

            callableStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private boolean doPasswordsMatch(String password1, String password2) {
        return password1.equals(password2);
    }

    private boolean isOldPasswordCorrect(String oldPassword) {
        Connection connection = user.getConnection();
        String query = "SELECT password FROM " + user.getRole() + " WHERE Username = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return BCrypt.checkpw(oldPassword, storedPassword);
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking old password", e);
        }
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }


    public void Cancel(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/Home.fxml"));
        Parent root = fxmlLoader.load();

        HomeController controller = fxmlLoader.getController();
        controller.createUser(user.getUsername(), user.getRole(), user.getId());
        controller.search();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void ReviewApplications(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/ReviewApply.fxml"));
        Parent root = fxmlLoader.load();

        ReviewController controller = fxmlLoader.getController();
        controller.setUser(user);
        controller.initial();
        controller.initial2();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Review Applications");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void checkPostedJobs(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/myPostedJobs.fxml"));
        Parent root = fxmlLoader.load();

        PostedJobController controller = fxmlLoader.getController();
        controller.setUser(user);
        controller.initial();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Check Posted Jobs");
        stage.setScene(new Scene(root));
        stage.show();

    }
}
