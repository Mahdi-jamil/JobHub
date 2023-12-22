package com.jobhub.controller;

import com.jobhub.model.User;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public ImageView Exit;
    public AnchorPane pane1;
    public AnchorPane pane2;
    public ImageView menu;
    private User user;

    public void createUser(String name, String role,Integer id) {
        if (user == null) {
            user = new User(name, role, id);
        }
    }

    @FXML
    private AnchorPane body;

    private static final String LOGIN_FXML = "/com/jobhub/login.fxml";
    private static final String PROFILE_FXML = "/com/jobhub/profile.fxml";
    private static final String POST_JOB_FXML = "/com/jobhub/postJob.fxml";
    private static final String SETTINGS_FXML = "/com/jobhub/settings.fxml";
    private static final String SEARCH_FXML = "/com/jobhub/search.fxml";


    private final EventHandler<MouseEvent> closingMenu = event -> closeMenu();

    public void closeMenu() {
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeTransition1.setFromValue(0.15);
        fadeTransition1.setToValue(0);
        fadeTransition1.play();

        fadeTransition1.setOnFinished(event1 -> pane1.setVisible(false));

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateTransition1.setByX(-600);
        translateTransition1.play();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Exit.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Confirm Exit");
            alert.setContentText("To Exit press ok ");

            if (alert.showAndWait().orElse(ButtonType.CLOSE) == ButtonType.OK) {
                System.out.println("Exit Successfully");
                System.exit(0);
            }
        });

        pane1.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateTransition.setByX(-600);
        translateTransition.play();

        menu.setOnMouseClicked(event -> {

            pane1.setVisible(true);

            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            translateTransition1.setByX(+600);
            translateTransition1.play();

        });

        pane1.setOnMouseClicked(closingMenu);
        body.setOnMouseClicked(closingMenu);
    }

    protected FXMLLoader loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            closeMenu();
            body.getChildren().clear();
            body.getChildren().add(content);
            return loader;
        } catch (IOException e) {
            System.out.println("error in loading " + fxmlFile);
        }
        return null;
    }

    public void Logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(LOGIN_FXML)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Get Authentication");
        stage.setScene(scene);
        stage.show();
    }

    public void profile()  {
        FXMLLoader profileLoader = loadContent(PROFILE_FXML);
        ProfileController profileController = profileLoader.getController();
        profileController.setUser(user);
    }

    public void postJob() {
        if(Objects.equals(user.getRole(), "job_seeker")){
            showAlert();
            return;
        }
        FXMLLoader postLoader = loadContent(POST_JOB_FXML);
        PostController postController = postLoader.getController();
        postController.setUser(user);
    }

    public void settings() {
        FXMLLoader settingLoader = loadContent(SETTINGS_FXML);
        SettingsController settingsController = settingLoader.getController();
        settingsController.setUser(user);
    }

    public void search() {
        FXMLLoader searchLoader = loadContent(SEARCH_FXML);
        SearchController searchController = searchLoader.getController();
        searchController.setUser(user);
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Access Denied");
        alert.setContentText("You are not allow to post a job");
        alert.show();
    }
}
