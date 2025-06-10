package org.example.create3droom.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.example.create3droom.RoomState;
import org.example.create3droom.utils.SceneTransitionUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PastWorksController {

    @FXML private ListView<String> savedWorksListView;
    private List<File> savedFiles = new ArrayList<>();

    @FXML
    public void initialize() {
        loadSavedWorks();
    }

    private void loadSavedWorks() {
        File saveDir = new File("saved_projects");
        if (!saveDir.exists() || !saveDir.isDirectory()) return;

        savedFiles.clear();
        savedWorksListView.getItems().clear();

        File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files != null) {
            for (File f : files) {
                savedFiles.add(f);
                savedWorksListView.getItems().add(f.getName());
            }
        }
    }

    @FXML
    private void onLoadSelectedClicked() {
        int idx = savedWorksListView.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            showAlert("Выберите сохранённую работу для загрузки");
            return;
        }

        File file = savedFiles.get(idx);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            RoomState roomState = (RoomState) ois.readObject();
            openFurnitureSelectionWithRoomState(roomState);
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка при загрузке работы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteSelectedClicked() {
        int idx = savedWorksListView.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            showAlert("Выберите сохранённую работу для удаления");
            return;
        }

        File toDelete = savedFiles.get(idx);
        if (toDelete.delete()) {
            showAlert("Файл успешно удалён: " + toDelete.getName());
            loadSavedWorks();
        } else {
            showAlert("Не удалось удалить файл.");
        }
    }

    @FXML
    private void onBackButtonClicked() {
        try {
            Parent menuRoot = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/view/menu.fxml"))
            );
            Stage stage = (Stage) savedWorksListView.getScene().getWindow();
            stage.setTitle("Главное меню");
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            SceneTransitionUtil.fadeToScene(stage, menuRoot);
        } catch (IOException e) {
            showAlert("Не удалось вернуться в главное меню: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openFurnitureSelectionWithRoomState(RoomState roomState) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("/view/furniture_selection.fxml"))
            );
            Parent fsRoot = loader.load();
            FurnitureSelectionController ctrl = loader.getController();
            ctrl.applyRoomState(roomState);

            Stage stage = (Stage) savedWorksListView.getScene().getWindow();
            stage.setTitle("Редактор комнаты");
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            SceneTransitionUtil.fadeToScene(stage, fsRoot);
        } catch (IOException e) {
            showAlert("Ошибка при открытии редактора комнаты.");
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
