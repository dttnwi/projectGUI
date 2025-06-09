package org.example.create3droom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/create3droom/view/menu.fxml")
        );
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/org/example/create3droom/styles.css")
                        .toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.setTitle("Домик. 3D Планировщик");

        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
