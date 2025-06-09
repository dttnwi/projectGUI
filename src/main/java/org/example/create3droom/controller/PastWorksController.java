package org.example.create3droom.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.create3droom.RoomState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            showAlert("Выберите сохранённую работу для загрузки.");
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
            showAlert("Выберите сохранённую работу для удаления.");
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

    // --- НОВЫЙ метод: возвращаемся в главное меню ---
    @FXML
    private void onBackButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/create3droom/view/menu.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) savedWorksListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Главное меню");
        } catch (IOException e) {
            showAlert("Не удалось вернуться в главное меню: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openFurnitureSelectionWithRoomState(RoomState roomState) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/create3droom/view/furniture_selection.fxml")
            );
            Parent root = loader.load();

            FurnitureSelectionController controller = loader.getController();
            controller.applyRoomState(roomState);

            Stage stage = (Stage) savedWorksListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Редактор комнаты");
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
