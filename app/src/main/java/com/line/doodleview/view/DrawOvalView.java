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
 * Created by chenliu on 2020-02-15.
 */
public class DrawOvalView extends View {
    public static final String TAG = "DrawLineView";

    TouchGestureDetector gestureDetectorCompat;

    public DrawOvalView(Context context) {
        super(context);
    }


    public DrawOvalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetectorCompat = new TouchGestureDetector(context, new TouchGestureDetector.OnTouchGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                currentLine = new Shape();
                currentLine.setType(Shape.ShapeType.OVAL);
                currentLine.setStartPoint(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "onScroll: e1:" + e1.getActionMasked());
                Log.d(TAG, "onScroll: e2:" + e2.getActionMasked());
                currentLine.setEndPoint(e2.getX(), e2.getY());
                invalidate();
                return true;
            }

            @Override
            public void onScrollEnd(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: " + e.getActionMasked());
                lineList.add(currentLine.copy());
                currentLine = null;
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

        lineList = new ArrayList<>();
    }

    List<Shape> lineList;

    Shape currentLine;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape l : lineList) {
            drawRound(l, canvas);
        }
        if (currentLine != null) {
            drawRound(currentLine, canvas);
        }
    }

    private void drawRound(Shape l, Canvas canvas) {
        canvas.drawOval(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), paint);
    }
}
