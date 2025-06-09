package org.example.create3droom.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.create3droom.model.RoomParams;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RoomSetupController implements Initializable {

    @FXML private TextField widthField;
    @FXML private TextField lengthField;
    @FXML private TextField heightField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private Button backButton;
    @FXML private Button nextButton;
    @FXML private Group root3DContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomTypeComboBox.getItems().addAll("Спальня", "Кухня", "Гостиная", "Ванная");

        BooleanBinding fieldsEmpty = widthField.textProperty().isEmpty()
                .or(lengthField.textProperty().isEmpty())
                .or(heightField.textProperty().isEmpty())
                .or(roomTypeComboBox.valueProperty().isNull());

        nextButton.disableProperty().bind(fieldsEmpty);
    }

    @FXML
    private void onBackClicked() {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/view/menu.fxml")
                    )
            );
            Stage stage = (Stage) root3DContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("3D Планировщик");

            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNextClicked() {
        try {
            double width = Double.parseDouble(widthField.getText().replace(',', '.'));
            double length = Double.parseDouble(lengthField.getText().replace(',', '.'));
            double height = Double.parseDouble(heightField.getText().replace(',', '.'));
            String roomType = roomTypeComboBox.getValue();

            RoomParams.set(width, length, height, roomType);

            AnchorPane pane = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/view/furniture_selection.fxml")
                    )
            );
            Stage stage = (Stage) nextButton.getScene().getWindow();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.setTitle("Редактор комнаты");

            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            stage.show();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Пожалуйста, введите корректные числа для параметров комнаты.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить следующий экран.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
