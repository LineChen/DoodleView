package com.line.doodleview.view;

/**
 * Created by chenliu on 2020-02-15.
 */
public class Shape {
    public enum ShapeType {
        /**
         * 直线
         */
        LINE,
        /**
         * 矩形
         */
        RECT,
        /**
         * 圆
         */
        OVAL
    }

    private ShapeType type = ShapeType.LINE;

    private float startX, startY, endX, endY;

    public Shape() {
    }

    public Shape(ShapeType type) {
        this.type = type;
    }

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

    public ShapeType getType() {
        return type;
    }

    public void setType(ShapeType type) {
        this.type = type;
    }

    public Shape copy() {
        Shape shape = new Shape();
        shape.setType(type);
        shape.setStartPoint(startX, startY);
        shape.setEndPoint(endX, endY);
        return shape;
    }
}
