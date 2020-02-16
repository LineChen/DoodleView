package com.line.doodleview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.line.doodleview.view.DrawShapeView;
import com.line.doodleview.view.Shape;

public class DrawShapeActivity extends AppCompatActivity {

    DrawShapeView drawShapeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_shape);
        drawShapeView = findViewById(R.id.draw_shape_view);
    }

    public void drawLine(View view) {
        drawShapeView.setDrawShape(Shape.ShapeType.LINE);
    }

    public void drawRect(View view) {
        drawShapeView.setDrawShape(Shape.ShapeType.RECT);
    }

    public void drawOval(View view) {
        drawShapeView.setDrawShape(Shape.ShapeType.OVAL);
    }

    public void clear(View view) {
        drawShapeView.clear();
    }
}
