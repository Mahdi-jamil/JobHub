package com.jobhub.controller;

import com.jobhub.DataBase.RootConnection;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class RegisterController {
    public TextField FirstName;
    public RadioButton jobSeeker;
    public TextField LastName;
    public TextField UserName;
    public TextField email;
    public TextField phone;
    public PasswordField password;
    public Label userExist;
    private Integer id = null;
    public TextArea skills;
    private String selectedRole = "";

    public void signIn(ActionEvent event) {

        if (!validateInput()) return;

        String firstName = this.FirstName.getText();
        String lastName = this.LastName.getText();
        String username = UserName.getText().trim();
        String email = this.email.getText();
        String password = this.password.getText().trim();
        String phone = this.phone.getText();
        String skills = this.skills.getText();

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() {
                addAccount(firstName, lastName, username, email, password, phone, skills, event);
                return null;
            }
        };

        new Thread(loginTask).start();


    }

    private void addAccount(String firstName, String lastName, String username, String email, String password, String phone, String skills, ActionEvent event) {
        Platform.runLater(() -> {

            try {
                Connection connection = RootConnection.getConnection();
                String table = (selectedRole.equals("job-seeker") ? "job_seeker" : "recruiter");

                String query = "SELECT CheckUsernameExists(?, ?) AS userExists";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, table);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userExists = resultSet.getInt("userExists");
                        if (userExists == 1) {
                            userExist.setText("Username already exists, try another one");
                            return;
                        }
                    }
                }
                String insert = "INSERT INTO " + table + " (fname, lname, Username, password, email, phone,skills) VALUES (?, ?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(insert);

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, username);

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                preparedStatement.setString(4, hashedPassword);

                preparedStatement.setString(5, email);
                preparedStatement.setString(6, phone);
                preparedStatement.setString(7, skills);

                preparedStatement.executeUpdate();
                getId(table, username);
                goToHome(event, table, username);

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void selectRole() {
        selectedRole = jobSeeker.isSelected() ? "job-seeker" : "recruiter";
    }

    private void goToHome(ActionEvent event, String role, String name) throws IOException {
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

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
    private boolean validateInput() {
        if (selectedRole.isEmpty()) {
            showAlert("Role Not Selected", "Please select a role (Job Seeker)");
            return false;
        }

        if (!isValidEmail(email.getText())) {
            showAlert("Invalid Email", "Please enter a valid email address");
            return false;
        }

        if (!isNumeric(phone.getText())) {
            showAlert("Invalid Phone Number", "Please enter a valid phone number");
            return false;
        }

        if (FirstName.getText().isEmpty() || LastName.getText().isEmpty() || UserName.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert("Incomplete Form", "Please fill in all required fields");
            return false;
        }
        if( !isValidPassword(password.getText())){
            showAlert("Invalid Password", "Please enter more than 8 character ");
            return false;
        }
        return true;
    }

    public void backToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/login.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Get Authenticate");
        stage.setScene(scene);
        stage.show();

    }

    private void getId(String table, String username) throws SQLException {
        if (id != null) {
            return;
        }
        Connection connection = RootConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + (table.equals("recruiter") ? "rid" : "jid") + " from " + table + " where Username = \"" + username + "\"");
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            id = resultSet.getInt(1);
            return;
        }
        connection.close();

    }
}
