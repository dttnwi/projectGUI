package org.example.create3droom.controller;
import org.example.create3droom.utils.SceneTransitionUtil;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import org.example.create3droom.FurnitureState;
import org.example.create3droom.RoomState;
import org.example.create3droom.model.FurnitureModel;
import org.example.create3droom.model.RoomParams;
import org.example.create3droom.utils.ModelLoader;
import org.example.create3droom.utils.RoomBuilder;
import java.io.*;
import java.util.*;

public class FurnitureSelectionController {

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ListView<FurnitureModel> furnitureListView;
    @FXML private Group root3DContainer;

    private Map<String, List<FurnitureModel>> modelsByCategory;
    private SubScene subScene;
    private Group roomGroup;
    private Group furnitureGroup;
    private Group selectedFurniture;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    private static final double ROTATION_ANGLE = 15; // градусов за нажатие

    private double roomScale = 1.0;

    @FXML
    public void initialize() {
        modelsByCategory = ModelLoader.loadAllFurnitureModels();
        categoryComboBox.getItems().addAll(modelsByCategory.keySet());

        categoryComboBox.setOnAction(event -> {
            String selectedCategory = categoryComboBox.getValue();
            furnitureListView.getItems().setAll(modelsByCategory.getOrDefault(selectedCategory, List.of()));
        });

        roomGroup = RoomBuilder.buildRoom();
        furnitureGroup = new Group();

        Group root3D = new Group(roomGroup, furnitureGroup);
        root3D.getTransforms().addAll(rotateX, rotateY);

        subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(3000);
        subScene.setCamera(camera);
        subScene.setFill(Color.LIGHTBLUE);

        root3DContainer.getChildren().add(subScene);

        setupCameraControls();
        showHelpOverlay();
        setupKeyboardControls();

        subScene.setFocusTraversable(true);
    }

    private void setupCameraControls() {
        subScene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleY = rotateY.getAngle();
        });

        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - anchorX;
            double deltaY = event.getSceneY() - anchorY;
            rotateY.setAngle(anchorAngleY + deltaX);
            rotateX.setAngle(anchorAngleX - deltaY);
        });

        subScene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY() > 0 ? 1.1 : 0.9;
            roomScale *= delta;

            roomGroup.setScaleX(roomScale);
            roomGroup.setScaleY(roomScale);
            roomGroup.setScaleZ(roomScale);

            furnitureGroup.setScaleX(roomScale);
            furnitureGroup.setScaleY(roomScale);
            furnitureGroup.setScaleZ(roomScale);

            // Сохраняем положение мебели, чтобы она оставалась "на полу"
            for (Node furniture : furnitureGroup.getChildren()) {
                Double baseTranslateY = (Double) furniture.getProperties().get("baseTranslateY");
                if (baseTranslateY == null) {
                    baseTranslateY = furniture.getTranslateY() * roomScale;
                    furniture.getProperties().put("baseTranslateY", baseTranslateY);
                }
                furniture.setTranslateY(baseTranslateY / roomScale);
            }
            event.consume();
        });
    }

    private void setupKeyboardControls() {
        subScene.setOnKeyPressed(event -> {
            if (selectedFurniture != null) {
                Rotate xRotate = getRotate(selectedFurniture, Rotate.X_AXIS);
                Rotate yRotate = getRotate(selectedFurniture, Rotate.Y_AXIS);
                Rotate zRotate = getRotate(selectedFurniture, Rotate.Z_AXIS);

                switch (event.getCode()) {
                    case DELETE -> {
                        furnitureGroup.getChildren().remove(selectedFurniture);
                        selectedFurniture = null;
                    }
                    case LEFT -> yRotate.setAngle(yRotate.getAngle() - ROTATION_ANGLE);
                    case RIGHT -> yRotate.setAngle(yRotate.getAngle() + ROTATION_ANGLE);
                    case UP -> {
                        if (event.isShiftDown()) xRotate.setAngle(xRotate.getAngle() - ROTATION_ANGLE);
                        else if (event.isControlDown()) zRotate.setAngle(zRotate.getAngle() - ROTATION_ANGLE);
                        else yRotate.setAngle(yRotate.getAngle() - ROTATION_ANGLE);
                    }
                    case DOWN -> {
                        if (event.isShiftDown()) xRotate.setAngle(xRotate.getAngle() + ROTATION_ANGLE);
                        else if (event.isControlDown()) zRotate.setAngle(zRotate.getAngle() + ROTATION_ANGLE);
                        else yRotate.setAngle(yRotate.getAngle() + ROTATION_ANGLE);
                    }
                }
            }
        });
    }

    private Rotate getRotate(Node node, Point3D axis) {
        for (var t : node.getTransforms()) {
            if (t instanceof Rotate r && r.getAxis().equals(axis)) return r;
        }
        Rotate r = new Rotate(0, axis);
        node.getTransforms().add(r);
        return r;
    }

    private void showHelpOverlay() {
        Label helpLabel = new Label("""
                Управление:
                - Выбор мебели: ЛКМ
                - Перемещение: ЛКМ + перетаскивание (Shift — движение вперед/назад, Ctrl — вверх/вниз)
                - Вращение: стрелки (← → — Y), + Shift (X), + Ctrl (Z)
                - Удаление: Delete
                - Масштаб комнаты: колесо мыши
                - Масштаб мебели: кнопка "Масштаб мебели"
                """);
        helpLabel.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-padding: 8; -fx-border-color: black;");
        helpLabel.setLayoutX(10);
        helpLabel.setLayoutY(520);
        root3DContainer.getChildren().add(helpLabel);
    }

    @FXML
    private void onAddFurnitureClicked() {
        FurnitureModel selectedModel = furnitureListView.getSelectionModel().getSelectedItem();
        if (selectedModel == null) {
            showAlert("Выберите объект мебели.");
            return;
        }

        Group model3D = selectedModel.getModel3D();
        if (model3D == null) {
            showAlert("Не удалось загрузить модель.");
            return;
        }

        Group wrapper = new Group(model3D);

        wrapper.setScaleX(0.01);
        wrapper.setScaleY(0.01);
        wrapper.setScaleZ(0.01);

        wrapper.setTranslateX(0);
        wrapper.setTranslateY(0);
        wrapper.setTranslateZ(0);

        // Сохраняем имя модели для последующего сохранения
        wrapper.getProperties().put("modelName", selectedModel.getName());

        furnitureGroup.getChildren().add(wrapper);

        enableModelInteraction(wrapper);

        selectedFurniture = wrapper;

        showInfo("Добавлена мебель: " + selectedModel.getName());
    }

    @FXML
    private void onScaleFurnitureClicked() {
        if (selectedFurniture == null) {
            showAlert("Сначала выберите мебель.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.format("%.2f", selectedFurniture.getScaleX()));
        dialog.setTitle("Масштаб мебели");
        dialog.setHeaderText("Введите коэффициент масштаба (например, 0.02):");
        dialog.setContentText("Масштаб:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double scale = Double.parseDouble(input);
                if (scale <= 0) throw new NumberFormatException();

                selectedFurniture.setScaleX(scale);
                selectedFurniture.setScaleY(scale);
                selectedFurniture.setScaleZ(scale);
            } catch (NumberFormatException e) {
                showAlert("Некорректный формат масштаба.");
            }
        });
    }

    private static final double MOVE_SPEED = 0.5; // настрой скорость здесь

    private void enableModelInteraction(Group model) {
        final double[] prevMouseX = new double[1];
        final double[] prevMouseY = new double[1];

        model.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                selectedFurniture = model;
                model.requestFocus();

                prevMouseX[0] = e.getSceneX();
                prevMouseY[0] = e.getSceneY();

                e.consume();
            }
        });

        model.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double deltaX = e.getSceneX() - prevMouseX[0];
                double deltaY = e.getSceneY() - prevMouseY[0];

                prevMouseX[0] = e.getSceneX();
                prevMouseY[0] = e.getSceneY();

                // Убираем деление на roomScale и добавляем MOVE_SPEED
                double moveX = deltaX * MOVE_SPEED;
                double moveZ = deltaY * MOVE_SPEED;
                double moveY = -deltaY * MOVE_SPEED;

                if (e.isControlDown()) {
                    model.setTranslateY(model.getTranslateY() + moveY);
                } else if (e.isShiftDown()) {
                    model.setTranslateZ(model.getTranslateZ() + moveZ);
                } else {
                    model.setTranslateX(model.getTranslateX() + moveX);
                    model.setTranslateZ(model.getTranslateZ() + moveZ);
                }

                model.getProperties().put("baseTranslateX", model.getTranslateX() / roomScale);
                model.getProperties().put("baseTranslateY", model.getTranslateY() / roomScale);
                model.getProperties().put("baseTranslateZ", model.getTranslateZ() / roomScale);

                e.consume();
            }
        });

        model.setOnScroll(event -> event.consume());
    }

    // Сохранение состояния комнаты
    @FXML
    private void onSaveRoomClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Сохранить работу");
        dialog.setHeaderText("Введите название работы:");
        dialog.setContentText("Имя:");

        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert("Имя не может быть пустым");
                return;
            }

            RoomState roomState = createRoomState();

            File saveDir = new File("saved_projects");
            if (!saveDir.exists()) saveDir.mkdir();

            File file = new File(saveDir, name + ".dat");

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(roomState);
                showInfo("Работа сохранена как " + name);
            } catch (IOException e) {
                showAlert("Ошибка при сохранении: " + e.getMessage());
            }
        });
    }

    private RoomState createRoomState() {
        RoomState roomState = new RoomState();
        roomState.roomScale = roomScale;
        roomState.roomWidth = RoomParams.width;
        roomState.roomLength = RoomParams.length;
        roomState.roomHeight = RoomParams.height;

        for (Node node : furnitureGroup.getChildren()) {
            if (node instanceof Group furniture) {
                String modelName = (String) furniture.getProperties().get("modelName");
                if (modelName == null) modelName = "unknown";

                FurnitureState fState = new FurnitureState(
                        modelName,
                        furniture.getTranslateX(),
                        furniture.getTranslateY(),
                        furniture.getTranslateZ(),
                        furniture.getScaleX()
                );
                roomState.furnitures.add(fState);
            }
        }
        return roomState;
    }

    // Вспомогательные методы для алертов
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            Parent menuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/menu.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneTransitionUtil.fadeToScene(stage, menuRoot);
            stage.setMaximized(true);
            stage.setTitle("Главное меню");
            stage.setFullScreen(true);
        } catch (IOException e) {
            showAlert("Не удалось загрузить главное меню.");
            e.printStackTrace();
        }
    }

    public void applyRoomState(RoomState roomState) {
        roomScale = roomState.roomScale;

        roomGroup.setScaleX(roomScale);
        roomGroup.setScaleY(roomScale);
        roomGroup.setScaleZ(roomScale);

        RoomParams.width = roomState.roomWidth;
        RoomParams.length = roomState.roomLength;
        RoomParams.height = roomState.roomHeight;

        furnitureGroup.getChildren().clear();

        for (FurnitureState fState : roomState.furnitures) {
            FurnitureModel model = ModelLoader.getModelByName(fState.modelName);
            if (model == null) continue;

            Group model3D = model.getModel3D();
            if (model3D == null) continue;

            Group wrapper = new Group(model3D);
            wrapper.setScaleX(fState.scale);
            wrapper.setScaleY(fState.scale);
            wrapper.setScaleZ(fState.scale);

            wrapper.setTranslateX(fState.translateX);
            wrapper.setTranslateY(fState.translateY);
            wrapper.setTranslateZ(fState.translateZ);

            wrapper.getProperties().put("modelName", fState.modelName);

            furnitureGroup.getChildren().add(wrapper);
            enableModelInteraction(wrapper);
        }
    }


}
