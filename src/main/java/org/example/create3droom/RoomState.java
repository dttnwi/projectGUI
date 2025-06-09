package org.example.create3droom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomState implements Serializable {
    private static final long serialVersionUID = 1L;

    public double roomScale;
    public double roomWidth;
    public double roomLength;
    public double roomHeight;

    public List<FurnitureState> furnitures = new ArrayList<>();
}
