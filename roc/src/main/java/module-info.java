module com.example.zkouska {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.zkouska to javafx.fxml;
    exports com.example.zkouska;
}