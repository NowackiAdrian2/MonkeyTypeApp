module typeapp.typeapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens typeapp.typeapp to javafx.fxml;
    exports typeapp.typeapp;
}