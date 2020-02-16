package com.line.doodleview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.line.doodleview.gestrue.TouchGestureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2020-02-16.
 */
public class DrawShapeView extends View {

    public static final String TAG = "DrawShapeView";

    public DrawShapeView(Context context) {
        super(context);
    }

    TouchGestureDetector gestureDetectorCompat;

    public DrawShapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetectorCompat = new TouchGestureDetector(context, new TouchGestureDetector.OnTouchGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                currentShape = new Shape(currentShapeType);
                currentShape.setStartPoint(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "onScroll: e1:" + e1.getActionMasked());
                Log.d(TAG, "onScroll: e2:" + e2.getActionMasked());
                currentShape.setEndPoint(e2.getX(), e2.getY());
                invalidate();
                return true;
            }

            @Override
            public void onScrollEnd(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: " + e.getActionMasked());
                shapeList.add(currentShape.copy());
                currentShape = null;
                invalidate();
            }
        });
    }

    Paint paint;

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.GREEN);

        shapeList = new ArrayList<>();
    }

    List<Shape> shapeList;

    Shape currentShape;

    private Shape.ShapeType currentShapeType = Shape.ShapeType.LINE;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape l : shapeList) {
            drawShape(l, canvas);
        }
        if (currentShape != null) {
            drawShape(currentShape, canvas);
        }
    }

    private void drawShape(Shape l, Canvas canvas) {
        switch (l.getType()) {
            case LINE:
                canvas.drawLine(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), paint);
                break;
            case RECT:
                canvas.drawRect(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), paint);
                break;
            case OVAL:
                canvas.drawOval(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), paint);
                break;
        }
    }

    public void setDrawShape(Shape.ShapeType currentShape) {
        this.currentShapeType = currentShape;
    }

    public void clear(){
        shapeList.clear();
        invalidate();
    }
}
