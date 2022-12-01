module com.example.roc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.roc to javafx.fxml;
    exports com.example.roc;
}