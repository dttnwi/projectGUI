package org.example.create3droom;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

//hbu
public class Main extends Application {
//коментприй
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/menu.fxml")
        );
        AnchorPane root = loader.load();

        root.setOpacity(0);

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

        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
