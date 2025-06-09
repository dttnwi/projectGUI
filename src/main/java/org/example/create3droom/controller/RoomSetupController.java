package org.example.create3droom.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomTypeComboBox.getItems().addAll("Спальня", "Кухня", "Гостиная", "Ванная");

        // Блокировка кнопки, если поля пустые
        BooleanBinding fieldsEmpty = widthField.textProperty().isEmpty()
                .or(lengthField.textProperty().isEmpty())
                .or(heightField.textProperty().isEmpty())
                .or(roomTypeComboBox.valueProperty().isNull());

        nextButton.disableProperty().bind(fieldsEmpty);
    }

    @FXML
    private Group root3DContainer;

    @FXML
    private void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/create3droom/view/menu.fxml"));
            Parent root = loader.load();

            // Используем root3DContainer для получения текущего Stage
            Stage stage = (Stage) root3DContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("3D Планировщик");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void onNextClicked() throws IOException {
        try {
            double width = Double.parseDouble(widthField.getText().replace(',', '.'));
            double length = Double.parseDouble(lengthField.getText().replace(',', '.'));
            double height = Double.parseDouble(heightField.getText().replace(',', '.'));
            String roomType = roomTypeComboBox.getValue();

            // Сохраняем параметры
            RoomParams.set(width, length, height, roomType);

            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/create3droom/view/furniture_selection.fxml")));
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(pane));

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Пожалуйста, введите корректные числа для параметров комнаты.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
