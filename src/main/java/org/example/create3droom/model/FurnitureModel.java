package org.example.create3droom.model;

import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import org.example.create3droom.utils.SimpleOBJLoader;

import java.net.URI;
import java.nio.file.Paths;

public class FurnitureModel {
    private String name;
    private String category;
    private String filePath;  // строка с URI

    public FurnitureModel(String name, String category, String filePath) {
        this.name = name;
        this.category = category;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * Загружает OBJ модель с помощью SimpleOBJLoader
     */
    public Group getModel3D() {
        try {
            // Получаем абсолютный путь к файлу из URI
            String path = Paths.get(new URI(filePath)).toString();

            MeshView meshView = SimpleOBJLoader.loadMesh(path);

            Group group = new Group(meshView);
            return group;
        } catch (Exception e) {
            e.printStackTrace();
            return new Group();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
