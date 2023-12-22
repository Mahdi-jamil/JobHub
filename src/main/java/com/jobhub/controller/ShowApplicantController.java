package com.jobhub.controller;

import com.jobhub.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowApplicantController {
    public Label name;
    public Label phone;
    public Label skills;
    public Label email;
    public RadioButton accept;
    public RadioButton reject;
    public RadioButton later;
    private User user;
    public Integer onJobID;
    private File CV;

    public void setCV(File CV) {
        this.CV = CV;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/appliesForAJob.fxml"));
        Parent root = fxmlLoader.load();

        AppliesForAJobController controller = fxmlLoader.getController();
        controller.setUser(user);
        controller.jobID = onJobID;
        controller.initial();


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Applications on a job");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void submit(ActionEvent event) throws IOException, SQLException {
        String status = status();
        if (status.equals("pending")) return;

        UpdateStatus(status);
        back(event);
    }

    private void UpdateStatus(String status) throws SQLException {
        String updateStatusQuery = "UPDATE apply SET status = ? where jobID = " +onJobID ;
        try (PreparedStatement preparedStatement = user.getConnection().prepareStatement(updateStatusQuery)) {
            preparedStatement.setString(1, status);
            preparedStatement.executeUpdate();
        }
    }
    public void showCV() {
        if (CV != null && CV.exists() && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(CV);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("CV file is not available or cannot be opened.");
        }
    }

    public String status() {
        if (accept.isSelected()) {
            return "accepted";
        } else if (reject.isSelected()) {
            return "rejected";
        } else return "pending";
    }
}
