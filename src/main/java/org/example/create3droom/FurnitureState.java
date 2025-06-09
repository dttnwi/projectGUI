package org.example.create3droom;

import java.io.Serializable;

public class FurnitureState implements Serializable {

    private static final long serialVersionUID = 1L;

    public String modelName;
    public double translateX;
    public double translateY;
    public double translateZ;
    public double scale;

    public FurnitureState(String modelName, double translateX, double translateY, double translateZ, double scale) {
        this.modelName = modelName;
        this.translateX = translateX;
        this.translateY = translateY;
        this.translateZ = translateZ;
        this.scale = scale;
    }
}

