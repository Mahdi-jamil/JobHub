package com.jobhub.controller;

import com.jobhub.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ApplyController implements Initializable {
    // add number of acceptance to job
    // trigger to decrease this number after each accept from recruiter
    public RadioButton Yes;
    public ChoiceBox<String> engLevel;
    public RadioButton Onsite;
    private User user;

    public Integer jobID;


    private Boolean hasBC = null;
    private Boolean onsite = null;
    private File CV = null;

    public void ToHome(ActionEvent event) throws IOException {
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

    enum EnglishLevel {
        NONE, CONVERSATIONAL, PROFESSIONAL, NATIVE
    }

    EnglishLevel level;

    private final String[] levels = {"None", "Conversational", "Professional", "Native"};

    public void setUser(User user) {
        this.user = user;
    }

    public void SelectEducation() {
        hasBC = Yes.isSelected();
    }

    public void SelectWhere() {
        onsite = Onsite.isSelected();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engLevel.getItems().addAll(levels);
        engLevel.setOnAction(this::setEngLevel);
    }

    private void setEngLevel(ActionEvent event) {
        String level = engLevel.getValue();
        switch (level) {
            case "None" -> this.level = EnglishLevel.NONE;
            case "Conversational" -> this.level = EnglishLevel.CONVERSATIONAL;
            case "Professional" -> this.level = EnglishLevel.PROFESSIONAL;
            default -> this.level = EnglishLevel.NATIVE;
        }
    }

    public void Apply(ActionEvent event) throws IOException {
        if (hasBC == null || onsite == null || CV == null) {
            showAlert("Form Not Completed", "Complete Actions");
            return;
        }

        insertApply();
        ToHome(event);
    }


    private void insertApply() throws FileNotFoundException {
        try {
            PreparedStatement preparedStatement = getPreparedStatement();

            preparedStatement.setInt(1, jobID);
            preparedStatement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setString(3, "pending");
            preparedStatement.setBlob(4, new FileInputStream(CV));
            preparedStatement.setBoolean(5, onsite);
            preparedStatement.setBoolean(6, hasBC);
            preparedStatement.setString(7, level.toString());
            preparedStatement.setInt(8, user.getId());

            SQLException exception = new SQLException();
            if (applyExist(jobID, user.getId())) throw exception;

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Application Redundant", "You already applied");
        }
    }

    private PreparedStatement getPreparedStatement() throws SQLException {
        Connection connection = user.getConnection();
        String query;
        query = "INSERT INTO Apply (JobID, date, status, CV, onsite, graduated, EnglishLevel," + ((user.getRole().equals("recruiter")) ? "rid" : "jid") + ") VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        return connection.prepareStatement(query);
    }

    public void chooseAndUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your CV");
        fileChooser.setInitialDirectory(new File("C:\\"));
        CV = fileChooser.showOpenDialog(new Stage());
        if (CV == null) {
            System.out.println("Error in Uploading file");
        }
        if (!CV.toString().toLowerCase().endsWith(".pdf")) {
            showAlert("Not Correct File", "Please upload .pdf file ");
            CV = null;
        }
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    private boolean applyExist(int jobID, int userID) {
        try {
            Connection connection = user.getConnection();
            String query = "SELECT * FROM Apply WHERE JobID = ? AND " + ((user.getRole().equals("recruiter")) ? "rid" : "jid") + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, jobID);
            preparedStatement.setInt(2, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

}
