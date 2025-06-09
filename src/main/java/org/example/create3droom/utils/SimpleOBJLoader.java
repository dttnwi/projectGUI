package org.example.create3droom.utils;

import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SimpleOBJLoader {

    public static MeshView loadMesh(String path) throws Exception {
        List<Float> vertices = new ArrayList<>();
        List<Integer> faces = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] tokens = line.split("\\s+");
                    vertices.add(Float.parseFloat(tokens[1]));
                    vertices.add(Float.parseFloat(tokens[2]));
                    vertices.add(Float.parseFloat(tokens[3]));
                } else if (line.startsWith("f ")) {
                    String[] tokens = line.split("\\s+");
                    for (int i = 1; i <= 3; i++) {
                        String[] parts = tokens[i].split("/");
                        int vertexIndex = Integer.parseInt(parts[0]) - 1;
                        faces.add(vertexIndex);
                        faces.add(0); // заглушка для текстурных координат
                    }
                }
            }
        }

        TriangleMesh mesh = new TriangleMesh();

        // Массивы для данных
        float[] pointsArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) pointsArray[i] = vertices.get(i);

        float[] texCoords = new float[]{0, 0};

        int[] facesArray = new int[faces.size()];
        for (int i = 0; i < faces.size(); i++) facesArray[i] = faces.get(i);

        mesh.getPoints().setAll(pointsArray);
        mesh.getTexCoords().setAll(texCoords);
        mesh.getFaces().setAll(facesArray);

        return new MeshView(mesh);
    }
}
