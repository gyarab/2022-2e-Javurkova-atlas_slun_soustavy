module com.example.rocnikova_prace {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rocnikova_prace to javafx.fxml;
    exports com.example.rocnikova_prace;
}