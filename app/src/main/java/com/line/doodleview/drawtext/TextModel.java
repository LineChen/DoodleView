package com.line.doodleview.drawtext;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * created by chenliu on  2021/9/2 4:45 下午.
 */
public class TextModel {

    private final RectF rect = new RectF();

    private String text;

    private boolean selected;


    public void setRect(RectF rect) {
        this.rect.set(rect);
    }

    public RectF getRect() {
        return rect;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "TextModel{" +
                "rect=" + rect +
                ", text='" + text + '\'' +
                ", selected=" + selected +
                '}';
    }
}
