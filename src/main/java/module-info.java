module com.jobhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires jbcrypt;
    requires java.desktop;
    requires lombok;


    opens com.jobhub to javafx.fxml;
    exports com.jobhub;
    exports com.jobhub.controller;
    exports com.jobhub.model;
    opens com.jobhub.controller to javafx.fxml;

}