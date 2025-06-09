module org.example.create3droom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example.create3droom.controller to javafx.fxml;
    exports org.example.create3droom;
    exports org.example.create3droom.model;
    exports org.example.create3droom.utils;
}
