package com.jobhub.controller;

import com.jobhub.model.Application;
import com.jobhub.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class ReviewController {

    public TableView<Application> ApplicationStatus;
    public TableColumn<Application, Integer> applyId;
    public TableColumn<Application, Integer> jobId;
    public TableColumn<Application, String> title;
    public TableColumn<Application, String> description;
    public TableColumn<Application, Date> date;
    public TableColumn<Application, Double> salary;

    public TableColumn<Application, String> Location;
    private User user;
    ObservableList<Application> applicationObservableList = FXCollections.observableArrayList();

    public void setUser(User user) {
        this.user = user;
    }
    public void initial() {


        Connection connection = user.getConnection();
        String query = "select ApplyID,job.JobID,title,Description,date,salary,location,apply.status " +
                "from job INNER JOIN apply ON job.jobid = apply.jobid " +
                "where " + (user.getRole().equals("recruiter") ? "apply.rid" : "jid") + " =" + user.getId();
        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                int queryApplyId = queryOutput.getInt("ApplyID");
                int queryJobID = queryOutput.getInt("JobID");
                String queryTable = queryOutput.getString("title");
                String queryDescription = queryOutput.getString("Description");
                Date queryDate = queryOutput.getDate("date");
                Double querySalary = queryOutput.getDouble("salary");
                String queryLocation = queryOutput.getString("location");
                String queryStatus = queryOutput.getString("status");
                applicationObservableList.add(new Application(queryApplyId, queryJobID, queryTable, queryDescription, queryDate, querySalary, queryLocation, queryStatus));
            }
            applyId.setCellValueFactory(new PropertyValueFactory<>("ApplyID"));
            jobId.setCellValueFactory(new PropertyValueFactory<>("JobId"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("Description"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            Location.setCellValueFactory(new PropertyValueFactory<>("location"));

            ApplicationStatus.setItems(applicationObservableList);


        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void initial2() {
        ApplicationStatus.setRowFactory(tv -> {
            TableRow<Application> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    if (newItem.getStatus().equalsIgnoreCase("accepted")) {
                        row.setStyle("-fx-background-color: lightgreen;");
                    } else if (newItem.getStatus().equalsIgnoreCase("pending")) {
                        row.setStyle("-fx-background-color: gray;");
                    } else {
                        row.setStyle("-fx-background-color: red;");
                    }
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jobhub/Home.fxml"));
        Parent root = fxmlLoader.load();

        HomeController controller = fxmlLoader.getController();
        controller.createUser(user.getUsername(), user.getRole(),user.getId());
        controller.settings();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Review Applications");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
