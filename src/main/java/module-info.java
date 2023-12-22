module com.example.jobhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.jobhub to javafx.fxml;
    exports com.jobhub;
}