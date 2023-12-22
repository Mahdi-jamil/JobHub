package com.jobhub.controller;

import com.jobhub.DataBase.RootConnection;
import com.jobhub.model.Job;
import com.jobhub.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostedJobController {
    private User user;

    public TableView<Job> tableView;
    public TableColumn<Job, Integer> IDField;
    public TableColumn<Job, String> titleField;
    public TableColumn<Job, String> descriptionField;
    public TableColumn<Job, String> requirementsID;
    public TableColumn<Job, Double> salaryField;
    public TableColumn<Job, String> locationField;
    ObservableList<Job> jobObservableList = FXCollections.observableArrayList();

    public void setUser(User user) {
        this.user = user;
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/Home.fxml"));
        Parent root = fxmlLoader.load();

        HomeController controller = fxmlLoader.getController();
        controller.createUser(user.getUsername(), user.getRole(), user.getId());
        controller.settings();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void initial() {
        Connection connection = RootConnection.getConnection();
        String jobViewQuery = "SELECT JobID,title,Description,Requirements,salary,location,rid,status from job where rid = " + user.getId();
        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(jobViewQuery);

            while (queryOutput.next()) {
                int queryJobID = queryOutput.getInt("JobID");
                String queryTable = queryOutput.getString("title");
                String queryDescription = queryOutput.getString("Description");
                String queryRequirements = queryOutput.getString("Requirements");
                Double querySalary = queryOutput.getDouble("salary");
                String queryLocation = queryOutput.getString("location");
                Integer rid = queryOutput.getInt("rid");
                String status = queryOutput.getString("status");
                if(status.equals("closed"))continue;
                jobObservableList.add(new Job(queryJobID, queryTable, queryDescription, queryRequirements, querySalary, queryLocation, rid,status));
            }
            IDField.setCellValueFactory(new PropertyValueFactory<>("JobID"));
            titleField.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionField.setCellValueFactory(new PropertyValueFactory<>("Description"));
            requirementsID.setCellValueFactory(new PropertyValueFactory<>("Requirements"));
            salaryField.setCellValueFactory(new PropertyValueFactory<>("Salary"));
            locationField.setCellValueFactory(new PropertyValueFactory<>("Location"));
            tableView.setItems(jobObservableList);

        } catch (SQLException e) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
        }

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Job selectedJob = tableView.getSelectionModel().getSelectedItem();
                if (selectedJob != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jobhub/appliesForAJob.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setTitle("Applications on a job");
                        stage.setScene(scene);

                        AppliesForAJobController controller = loader.getController();
                        controller.setUser(user);
                        controller.jobID = selectedJob.getJobID();
                        controller.initial();
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("Error in loading apply");
                    }
                }
            }
        });

    }



}
