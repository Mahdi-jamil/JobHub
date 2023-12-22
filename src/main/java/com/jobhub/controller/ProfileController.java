package com.jobhub.controller;

import com.jobhub.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class ProfileController {
    public TextField UserName;
    public TextField FirstName;
    public TextField LastName;
    public TextField email;
    public TextField phone;
    public TextArea skills;

    private User user;

    public void setUser(User user) {
        this.user = user;
        initial();
    }

    private void showAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private boolean uniqeUserName() {
        Connection connection = user.getConnection();
        String query = "SELECT CheckUsernameExists(?, ?) AS userExists";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, UserName.getText());
            preparedStatement.setString(2, user.getRole());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userExists = resultSet.getInt("userExists");
                return userExists == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean validateInput() {

        if (!isValidEmail(email.getText())) {
            showAlert("Invalid Email", "Please enter a valid email address");
            return false;
        }

        if (!isNumeric(phone.getText())) {
            showAlert("Invalid Phone Number", "Please enter a valid phone number");
            return false;
        }
        if (!uniqeUserName() && !Objects.equals(UserName.getText(), user.getUsername())) {
            showAlert("Username Exist", "Please enter another Username");
            return false;
        }

        if (FirstName.getText().isEmpty() || LastName.getText().isEmpty() || UserName.getText().isEmpty()) {
            showAlert("Incomplete Form", "Please fill in all required fields");
            return false;
        }
        return true;
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

    public void initial() {
        Connection connection = user.getConnection();

        try {

            String query = "SELECT Username,Fname, Lname, email, phone, skills FROM " + user.getRole() + " WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    UserName.setText(resultSet.getString("Username"));
                    FirstName.setText(resultSet.getString("Fname"));
                    LastName.setText(resultSet.getString("Lname"));
                    email.setText(resultSet.getString("email"));
                    phone.setText(resultSet.getString("phone"));
                    skills.setText(resultSet.getString("skills"));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR IN RETRIEVE DATA !!!");
        }

    }

    public void Save(ActionEvent event) {
        if (validateInput()) {
            try {
                Connection connection = user.getConnection();
                String updateQuery = "UPDATE " + user.getRole() + " SET Fname=?, Lname=?, email=?, phone=?, skills=?,Username=? WHERE Username=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, FirstName.getText());
                    preparedStatement.setString(2, LastName.getText());
                    preparedStatement.setString(3, email.getText());
                    preparedStatement.setString(4, phone.getText());
                    preparedStatement.setString(5, skills.getText());
                    preparedStatement.setString(6,UserName.getText());
                    preparedStatement.setString(7, user.getUsername());
                    preparedStatement.executeUpdate();
                    user.setUsername(UserName.getText());
                    Cancel(event);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (SQLException e) {
                System.out.println("Error in saving !!");
            }
        }
    }
}
