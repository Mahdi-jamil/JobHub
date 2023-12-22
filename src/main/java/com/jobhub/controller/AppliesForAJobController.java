package com.jobhub.controller;

import com.jobhub.model.ApplicationInfo;
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

import java.io.*;
import java.sql.*;
import java.util.Date;

public class AppliesForAJobController {

    public Integer jobID;
    public TableView<ApplicationInfo> ApplicationStatus;
    public TableColumn<ApplicationInfo, Integer> applyId;
    public TableColumn<ApplicationInfo, Integer> jobId;
    public TableColumn<ApplicationInfo, Date> date;
    public TableColumn<ApplicationInfo, String> status;
    public TableColumn<ApplicationInfo, String> onsite;
    public TableColumn<ApplicationInfo, String> EnglishLevel;
    public TableColumn<ApplicationInfo, String> Graduated;
    private User user;
    ObservableList<ApplicationInfo> applicationObservableList = FXCollections.observableArrayList();

    public void setUser(User user) {
        this.user = user;
    }

    public void initial() {//todo get cv
        Connection connection = user.getConnection();
        String query = "select ApplyID,job.JobID,date,apply.status,onsite,graduated,EnglishLevel" +
                " from job INNER JOIN apply ON job.jobid = apply.jobid " +
                "where job.JobID = " + jobID;
        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next()) {
                int queryApplyId = queryOutput.getInt("ApplyID");
                int queryJobID = queryOutput.getInt("JobID");
                Date queryDate = queryOutput.getDate("date");
                String queryStatus = queryOutput.getString("status");
                int queryWorkPlace = queryOutput.getInt("onsite");
                int queryGraduated = queryOutput.getInt("graduated");
                String queryEnglishLevel = queryOutput.getString("EnglishLevel");
                applicationObservableList.add(new ApplicationInfo(queryApplyId, queryJobID, queryDate, queryStatus, queryEnglishLevel,
                        (queryWorkPlace == 1) ? "Yes" : "No", (queryGraduated == 1) ? "Yes" : "No"));
            }
            applyId.setCellValueFactory(new PropertyValueFactory<>("ApplyID"));
            jobId.setCellValueFactory(new PropertyValueFactory<>("JobId"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            onsite.setCellValueFactory(new PropertyValueFactory<>("onsite"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            EnglishLevel.setCellValueFactory(new PropertyValueFactory<>("EnglishLevel"));
            Graduated.setCellValueFactory(new PropertyValueFactory<>("graduated"));

            ApplicationStatus.setItems(applicationObservableList);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        ApplicationStatus.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ApplicationInfo selectedApplication = ApplicationStatus.getSelectionModel().getSelectedItem();
                if (selectedApplication != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jobhub/showApplicant.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setTitle("View Applicant");
                        stage.setScene(scene);

                        ShowApplicantController controller = loader.getController();
                        setController(controller, selectedApplication);

                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void back(ActionEvent event) throws IOException {
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


    private void setController(ShowApplicantController controller, ApplicationInfo application) throws SQLException {
        controller.setUser(user);
        Connection connection = user.getConnection();
        Integer applyId = application.getApplyID();

        String getIDQuery = "select rid,jid,CV from apply where applyID = " + applyId;
        try (Statement statement = connection.createStatement();
             ResultSet queryOutput = statement.executeQuery(getIDQuery)) {

            Integer rid = null, jid = null;
            File CV = File.createTempFile("tempCV", ".pdf");
            if (queryOutput.next()) {
                jid = queryOutput.getInt("jid");
                rid = queryOutput.getInt("rid");
                Blob blob = queryOutput.getBlob("CV");

                if (blob != null) {
                    try (InputStream inputStream = blob.getBinaryStream();
                         OutputStream outputStream = new FileOutputStream(CV)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            Integer id = (jid != null && jid != 0) ? jid : rid;
            String table = (jid != null && jid != 0) ? "job_seeker" : "recruiter";
            putData(id, controller, connection, table,CV);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void putData(Integer id, ShowApplicantController controller, Connection connection, String table, File CV) throws SQLException {
        String retrievePersonalData = "select fname,lname,phone,email,skills " +
                "from " + table +
                " where " + (table.equals("recruiter") ? "rid" : "jid") + " = " + id;
        Statement statement = connection.createStatement();
        ResultSet queryOutput = statement.executeQuery(retrievePersonalData);
        if (queryOutput.next()) {
            controller.name.setText("Name : " + queryOutput.getString("fname") + " " + queryOutput.getString("lname"));
            controller.phone.setText("Phone : " + queryOutput.getString("phone"));
            controller.email.setText("Email : " + queryOutput.getString("email"));
            controller.skills.setText("Skills : " + queryOutput.getString("skills"));
        }
        controller.onJobID = jobID;
        controller.setCV(CV);

    }
}
