package com.line.doodleview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.line.doodleview.R;

/**
 * Created by chenliu on 2020-02-14.
 */
public class DrawTextView extends View {

    public static final String TAG = "DrawTextView";

    public DrawTextView(Context context) {
        super(context);
    }

    public DrawTextView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "onSingleTapConfirmed: ");
                final EditText contentView = new EditText(getContext());
                contentView.setHint("请输入文本");
                contentView.setSelection(contentView.getText().length());
                contentView.setGravity(Gravity.TOP | Gravity.LEFT);
                contentView.setTextColor(Color.DKGRAY);
                contentView.setTextSize(12F);
                contentView.setBackgroundResource(R.drawable.edit);
                PopupWindow popupWindow = new PopupWindow(contentView);
                popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                popupWindow.setAnimationStyle(0);
                popupWindow.showAtLocation(DrawTextView.this, Gravity.TOP | Gravity.LEFT, (int) e.getX(), (int) e.getY());
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        String result = contentView.getText().toString();
                        text = result;
                        invalidate();
                    }
                });
                return true;
            }
        });

    }

    GestureDetectorCompat gestureDetectorCompat;

    String text = "";
    TextPaint textPaint;
    Paint paint;

    {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.DKGRAY);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);

        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        StaticLayout staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, 100).build();
        staticLayout.draw(canvas);
        canvas.drawRect(0, 0, staticLayout.getWidth(), staticLayout.getHeight(), paint);
        canvas.restore();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
//        return super.onTouchEvent(event);
    }
}
