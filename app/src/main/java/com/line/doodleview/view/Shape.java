package com.line.doodleview.view;

/**
 * Created by chenliu on 2020-02-15.
 */
public class Shape {
    private float startX, startY, endX, endY;

    public void setStartPoint(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void setEndPoint(float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public Shape copy() {
        Shape shape = new Shape();
        shape.setStartPoint(startX, startY);
        shape.setEndPoint(endX, endY);
        return shape;
    }
}
