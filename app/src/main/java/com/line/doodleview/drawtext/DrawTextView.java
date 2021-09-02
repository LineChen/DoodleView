package com.line.doodleview.drawtext;

import android.content.Context;
import android.graphics.*;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

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
                final float leftTopX = e.getX();
                final float leftToyY = e.getY();

                boolean findText = false;
                for (int i = textModelList.size() - 1; i >= 0; i--) {
                    TextModel textModel = textModelList.get(i);
                    if (textModel.getRect().contains(leftTopX, leftToyY)) {
                        if (!findText) {
                            textModel.setSelected(true);
                            findText = true;
                        } else {
                            textModel.setSelected(false);
                        }
                    } else {
                        textModel.setSelected(false);
                    }
                }
                if (findText) {
                    invalidate();
                    return true;
                } else {
                    for (TextModel textModel : textModelList) {
                        textModel.setSelected(false);
                    }
                    invalidate();
                }

                final EditText contentView = new EditText(getContext());
                contentView.setHint("输入");
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

                popupWindow.showAtLocation(DrawTextView.this, Gravity.TOP | Gravity.LEFT, (int) leftTopX, (int) leftToyY);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        String result = contentView.getText().toString();
                        if (TextUtils.isEmpty(result)) {
                            return;
                        }
                        text = result;
                        textWidth = contentView.getWidth();
                        TextModel textModel = new TextModel();
                        textModel.setText(text);
                        RectF rect = textModel.getRect();
                        rect.left = (int) leftTopX;
                        rect.top = (int) leftToyY;
                        rect.right = (int) (leftTopX + textWidth);
                        textModelList.add(textModel);
                        invalidate();
                    }
                });
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                boolean findText = false;
                TextModel selectText = null;
                for (int i = textModelList.size() - 1; i >= 0; i--) {
                    TextModel textModel = textModelList.get(i);
                    if (textModel.isSelected()) {
                        selectText = textModel;
                        findText = true;
                        break;
                    }
                }

                if (findText) {
                    RectF rect = selectText.getRect();
                    rect.top -= distanceY;
                    rect.bottom -= distanceY;
                    rect.left -= distanceX;
                    rect.right -= distanceX;
                    invalidate();
                }
                return findText;
            }
        });

    }

    private final List<TextModel> textModelList = new ArrayList<>();

    GestureDetectorCompat gestureDetectorCompat;

    String text = "";
    float textWidth;
    TextPaint textPaint;
    Paint paint;

    {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 12);
        textPaint.setColor(Color.DKGRAY);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (TextModel textModel : textModelList) {
            RectF rect = textModel.getRect();
            canvas.save();
            canvas.translate(rect.left, rect.top);
            StaticLayout textLayout = StaticLayout.Builder.obtain(
                    textModel.getText(),
                    0,
                    textModel.getText().length(),
                    textPaint,
                    (int) rect.width())
                    .build();
            textLayout.draw(canvas);
            rect.bottom = rect.top + textLayout.getHeight();
            canvas.restore();
            if (textModel.isSelected()) {
                canvas.drawRect(rect, paint);
            }
            Log.d(TAG, "onDraw: " + textModel);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

}