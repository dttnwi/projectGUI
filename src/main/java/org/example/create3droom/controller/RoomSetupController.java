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
import org.example.create3droom.utils.SceneTransitionUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RoomSetupController implements Initializable {

    @FXML private TextField widthField;
    @FXML private TextField lengthField;
    @FXML private TextField heightField;
    @FXML private Button backButton;
    @FXML private Button nextButton;

    @FXML private AnchorPane rootPane; // Reference to the root AnchorPane for background setting

    @FXML private Group root3DContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the background image (update the path as needed)
        setBackgroundImage(getClass().getResource("/fon.png").toExternalForm()); // Use the correct resource path

        BooleanBinding fieldsEmpty = widthField.textProperty().isEmpty()
                .or(lengthField.textProperty().isEmpty())
                .or(heightField.textProperty().isEmpty());

        nextButton.disableProperty().bind(fieldsEmpty);
    }

    @FXML
    private void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();

            if (root3DContainer != null && root3DContainer.getScene() != null) {
                Parent menuRoot = FXMLLoader.load(
                        Objects.requireNonNull(getClass().getResource("/view/menu.fxml"))
                );
                Stage stage = (Stage) root3DContainer.getScene().getWindow();
                stage.setFullScreen(true);
                stage.setTitle("3D Планировщик");
                SceneTransitionUtil.fadeToScene(stage, menuRoot);
            } else {
                System.err.println("root3DContainer is null or not attached to a scene.");
            }
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


            RoomParams.set(width, length, height, ""); 


            AnchorPane pane = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/view/furniture_selection.fxml")
                    )
            );
            Stage stage = (Stage) nextButton.getScene().getWindow();
            Scene scene = new Scene(pane);
            Parent menuRoot = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/view/furniture_selection.fxml"))
            );
            stage.setScene(scene);
            stage.setTitle("Редактор комнаты");
            stage.setFullScreen(true);
            SceneTransitionUtil.fadeToScene(stage, menuRoot);
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

    /**
     * Set a background image on the root scene background.
     * @param url Image URL or path
     */
    public void setBackgroundImage(String url) {
        String imageStyle = String.format(
                "-fx-background-image: url('%s');" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;", url);
        rootPane.setStyle(imageStyle);
    }

    // New methods for button hover effects
    @FXML
    private void backButtonHoverOn() {
        backButton.setStyle(
                "-fx-background-color: #e5e7eb;" + // светлый фон при наведении
                        "-fx-text-fill: #374151;" +
                        "-fx-font-weight: 700;" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;" +
                        "-fx-pref-height: 70px;" +
                        "-fx-pref-width: 250px;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 4);"
        );
    }

    @FXML
    private void backButtonHoverOff() {
        backButton.setStyle(
                "-fx-background-color: #f9fafb;" +
                        "-fx-text-fill: #6b7280;" +
                        "-fx-font-weight: 700;" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;" +
                        "-fx-pref-height: 70px;" +
                        "-fx-pref-width: 250px;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 6, 0, 0, 3);"
        );
    }

    @FXML
    private void nextButtonHoverOn() {
        nextButton.setStyle(
                "-fx-background-color: #1f2937;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: 700;" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;" +
                        "-fx-pref-height: 70px;" +
                        "-fx-pref-width: 250px;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 6);"
        );
    }

    @FXML
    private void nextButtonHoverOff() {
        nextButton.setStyle(
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: 700;" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;" +
                        "-fx-pref-height: 70px;" +
                        "-fx-pref-width: 250px;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 5);"
        );
    }
}
