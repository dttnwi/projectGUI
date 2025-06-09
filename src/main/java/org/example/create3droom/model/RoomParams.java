package org.example.create3droom.model;

public class RoomParams {
    public static double width;
    public static double length;
    public static double height;
    public static String type;

    public static void set(double w, double l, double h, String t) {
        width = w;
        length = l;
        height = h;
        type = t;
    }
}
