package com.jobhub.controller;

import com.jobhub.DataBase.RootConnection;
import com.jobhub.model.Job;
import com.jobhub.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SearchController implements Initializable {
    public TableView<Job> tableView;
    public TableColumn<Job, Integer> IDField;
    public TableColumn<Job, String> titleField;
    public TableColumn<Job, String> descriptionField;
    public TableColumn<Job, String> requirementsID;
    public TableColumn<Job, Double> salaryField;
    public TableColumn<Job, String> locationField;
    public TextField SearchField;
    private User user;
    ObservableList<Job> jobObservableList = FXCollections.observableArrayList();


    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Job selectedJob = tableView.getSelectionModel().getSelectedItem();
                if (selectedJob != null && !Objects.equals(selectedJob.rid, user.getId())) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jobhub/apply.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setTitle("Apply");
                        stage.setScene(scene);

                        ApplyController controller = loader.getController();
                        controller.setUser(user);
                        controller.jobID = selectedJob.getJobID();
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("Error in loading apply");
                    }
                }
            }
        });

        tableView.setRowFactory(tv -> {
            TableRow<Job> row = new TableRow<>();
            Popup details = new Popup();

            row.setOnMouseEntered(event -> {
                Job job = row.getItem();
                if (job != null) {
                    VBox popupContent = new VBox();
                    popupContent.setStyle("-fx-background-color: white; -fx-padding: 10;");

                    Label titleLabel = new Label("Title: " + job.getTitle());
                    Label descriptionLabel = new Label("Description: " + job.getDescription());
                    Label requirementsLabel = new Label("Requirements: " + job.getRequirements());
                    Label salaryLabel = new Label("Salary: " + job.getSalary());
                    Label locationLabel = new Label("Location: " + job.getLocation());

                    popupContent.getChildren().addAll(titleLabel, descriptionLabel, requirementsLabel, salaryLabel, locationLabel);
                    details.getContent().add(popupContent);

                    details.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            row.setOnMouseExited(event -> {
                if (row.getItem() != null) {
                    details.hide();
                }
            });

            return row;
        });


        Connection connection = RootConnection.getConnection();
        String jobViewQuery = "SELECT JobID,title,Description,Requirements,salary,location,rid,status from job";

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
            FilteredList<Job> filteredData = new FilteredList<>(jobObservableList, b -> true);

            SearchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(Job -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (Job.getJobID().toString().equals(searchKeyword)) {
                    return true;
                } else if (Job.getTitle().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (Job.getDescription().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (Job.getRequirements().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchKeyword.matches("-?\\d+(\\.\\d+)?") && Job.getSalary() >= Double.parseDouble(searchKeyword)) {
                    return true;
                } else return Job.getLocation().toLowerCase().contains(searchKeyword);
            }));

            SortedList<Job> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);

        } catch (SQLException e) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
