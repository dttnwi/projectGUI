package org.example.create3droom.utils;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import org.example.create3droom.model.RoomParams;

public class RoomBuilder {

    public static Group buildRoom() {
        Group room = new Group();

        double w = RoomParams.width;
        double l = RoomParams.length;
        double h = RoomParams.height;

        double wallThickness = 0.1;  // Тонкие стены
        double floorThickness = 0.1; // Тонкий пол

        // Пол
        Box floor = new Box(w, floorThickness, l);
        floor.setTranslateY(h / 2 - floorThickness / 2);
        floor.setMaterial(new PhongMaterial(Color.SANDYBROWN));

        // Задняя стена
        Box wallBack = new Box(w, h, wallThickness);
        wallBack.setTranslateZ(-l / 2 + wallThickness / 2);
        wallBack.setMaterial(new PhongMaterial(Color.BEIGE));

        // Левая стена
        Box wallLeft = new Box(wallThickness, h, l);
        wallLeft.setTranslateX(-w / 2 + wallThickness / 2);
        wallLeft.setMaterial(new PhongMaterial(Color.LIGHTSTEELBLUE));

        // Правая стена
        Box wallRight = new Box(wallThickness, h, l);
        wallRight.setTranslateX(w / 2 - wallThickness / 2);
        wallRight.setMaterial(new PhongMaterial(Color.MISTYROSE));

        room.getChildren().addAll(floor, wallBack, wallLeft, wallRight);

        return room;
    }
}
