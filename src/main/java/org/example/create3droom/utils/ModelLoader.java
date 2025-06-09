package org.example.create3droom.utils;

import org.example.create3droom.model.FurnitureModel;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ModelLoader {

    private static Map<String, FurnitureModel> modelsByName = new HashMap<>();

    public static Map<String, List<FurnitureModel>> loadAllFurnitureModels() {
        Map<String, List<FurnitureModel>> modelsByCategory = new HashMap<>();
        modelsByName.clear();  // Очистить перед загрузкой

        URL modelsDirUrl = ModelLoader.class.getResource("/models/");
        if (modelsDirUrl == null) {
            System.err.println("❌ Папка /models не найдена. Убедись, что она лежит в src/main/resources.");
            return modelsByCategory;
        }

        File modelsDir;
        try {
            modelsDir = new File(modelsDirUrl.toURI());
        } catch (URISyntaxException e) {
            System.err.println("❌ Ошибка при преобразовании URI: " + e.getMessage());
            return modelsByCategory;
        }

        File[] categoryDirs = modelsDir.listFiles(File::isDirectory);
        if (categoryDirs == null) return modelsByCategory;

        for (File categoryDir : categoryDirs) {
            String category = categoryDir.getName();
            File[] modelFiles = categoryDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".obj"));
            if (modelFiles == null) continue;

            List<FurnitureModel> modelList = new ArrayList<>();
            for (File modelFile : modelFiles) {
                String modelName = modelFile.getName().replace(".obj", "");
                FurnitureModel model = new FurnitureModel(
                        modelName,
                        category,
                        modelFile.toURI().toString()
                );
                modelList.add(model);
                modelsByName.put(modelName, model);
            }

            modelsByCategory.put(category, modelList);
        }

        return modelsByCategory;
    }

    public static FurnitureModel getModelByName(String name) {
        if (modelsByName.isEmpty()) {
            loadAllFurnitureModels();
        }
        return modelsByName.get(name);
    }
}
