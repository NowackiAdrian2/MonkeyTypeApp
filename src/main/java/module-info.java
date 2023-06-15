module typeapp.typeapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens typeapp.typeapp to javafx.fxml;
    exports typeapp.typeapp;
}