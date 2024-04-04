module com.example.pslab1fxclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pslab1fxclient to javafx.fxml;
    exports com.example.pslab1fxclient;
}