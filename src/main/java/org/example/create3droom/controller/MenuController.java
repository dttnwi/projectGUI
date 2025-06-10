package org.example.create3droom.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.create3droom.utils.SceneTransitionUtil;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    @FXML
    private void onCreateRoomClicked(ActionEvent event) {
        try {
            Parent newRoot = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/view/room_setup.fxml")
                    )
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Создание комнаты");
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            SceneTransitionUtil.fadeToScene(stage, newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onShowPastWorksClicked(ActionEvent event) {
        try {
            Parent newRoot = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/view/past_works.fxml")
                    )
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Прошлые работы");
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            SceneTransitionUtil.fadeToScene(stage, newRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
