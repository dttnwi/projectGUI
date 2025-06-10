package org.example.create3droom.utils;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneTransitionUtil {
    public static void fadeToScene(Stage stage, Parent newRoot) {
        Scene scene = stage.getScene();
        Parent oldRoot = scene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            scene.setRoot(newRoot);
            newRoot.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
