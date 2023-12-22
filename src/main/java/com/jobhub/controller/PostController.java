package com.jobhub.controller;

import com.jobhub.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PostController implements Initializable {

    public Spinner<Double> salarySpinner;
    public Spinner<Integer> AvailableRoles;
    public TextField location;
    public TextArea requirement;
    public TextArea description;
    public TextField jobTitle;
    private Double salary;
    private Integer availableRoles;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 150000);
        valueFactory.setValue(0.0);
        salarySpinner.setValueFactory(valueFactory);
        salary = this.salarySpinner.getValue();

        salarySpinner.valueProperty().addListener((observableValue, integer, t1) -> salary = salarySpinner.getValue());

        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000);
        valueFactory2.setValue(1);
        AvailableRoles.setValueFactory(valueFactory2);
        availableRoles = this.AvailableRoles.getValue();

        AvailableRoles.valueProperty().addListener((observableValue, integer, t1) -> availableRoles = AvailableRoles.getValue());
        if(availableRoles<=0){
            showAlert("Available Roles Can't be zero or negative");
        }

    }

    public void Post(ActionEvent event) {
        if (!checkFields()) {
            showAlert("Please fill in all fields.");
            return;
        }

        String jobTitle = this.jobTitle.getText();
        String description = this.description.getText();
        String requirement = this.requirement.getText();
        String location = this.location.getText();

        Connection connection = user.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO job (title, description, Requirements, salary, location, numberOfApplication, rid) VALUES (?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, jobTitle);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, requirement);
            preparedStatement.setDouble(4, salary);
            preparedStatement.setString(5, location);
            preparedStatement.setInt(6, availableRoles);
            preparedStatement.setInt(7, user.getId());
            preparedStatement.executeUpdate();
            Cancel(event);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void Cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jobhub/home.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home");
        stage.setScene(scene);

        HomeController controller = loader.getController();
        controller.createUser(user.getUsername(), user.getRole(), user.getId());
        controller.search();
        stage.show();
    }

    private boolean checkFields() {
        return isEmpty(location) && isEmpty(requirement) && isEmpty(description) && isEmpty(jobTitle);
    }

    private boolean isEmpty(TextField textField) {
        return !textField.getText().trim().isEmpty();
    }

    private boolean isEmpty(TextArea textArea) {
        return !textArea.getText().trim().isEmpty();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}
