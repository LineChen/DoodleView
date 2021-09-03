package com.line.doodleview.drawtext;

import android.content.Context;
import android.graphics.*;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import androidx.core.view.ScaleGestureDetectorCompat;

import com.line.doodleview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.graphics.text.LineBreaker.BREAK_STRATEGY_SIMPLE;

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
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                int pointerCount = e.getPointerCount();
                Log.d(TAG, "onDown: x,y=" + e.getX() + "," + e.getY());
                return touchPointCount == 1;
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
                        preSolveEditText(contentView);
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

            private void preSolveEditText(EditText editText) {
                String text = editText.getText().toString();
                int lineCount = editText.getLayout().getLineCount();
                List<String> lines = new ArrayList<>();
                for (int i = 0; i < lineCount; i++) {
                    lines.add(text.substring(editText.getLayout().getLineStart(i),
                            editText.getLayout().getLineEnd(i)));
                }
                Log.d(TAG, "preSolveEditText: lineCount = " + lineCount + "=>\n" + Arrays.toString(lines.toArray()));
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


        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                boolean findText = false;
                for (int i = textModelList.size() - 1; i >= 0; i--) {
                    TextModel textModel = textModelList.get(i);
                    if (textModel.isSelected()) {
                        findText = true;
                        break;
                    }
                }
                Log.d(TAG, "onScaleBegin: findText=" + findText);
                return touchPointCount > 1;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (detector == null) return false;
                float scaleFactor = detector.getScaleFactor();
                TextModel selectedText = null;
                for (int i = textModelList.size() - 1; i >= 0; i--) {
                    TextModel textModel = textModelList.get(i);
                    if (textModel.isSelected()) {
                        selectedText = textModel;
                        break;
                    }
                }
                if (selectedText != null) {
                    selectedText.setScale(scaleFactor * selectedText.getScale());
                } else {
                    canvasScale *= scaleFactor;
//                    for (TextModel textModel : textModelList) {
//                        RectF rect = textModel.getRect();
//                        scaleRect(rect, scaleFactor, detector.getFocusX(), detector.getFocusY());
//                    }
                }
                invalidate();
                Log.d(TAG, "onScale: focus= " + detector.getFocusX() + "," + detector.getFocusY());
                return true;
            }


            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
    }

    private final List<TextModel> textModelList = new ArrayList<>();

    private GestureDetectorCompat gestureDetectorCompat;
    private ScaleGestureDetector scaleGestureDetector;
    private int touchPointCount;
    private float canvasScale = 1.0f;

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
            canvas.scale(canvasScale, canvasScale);
            canvas.translate(rect.left, rect.top);
            textPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * 12 * textModel.getScale());
//            textPaint.setTextScaleX(textModel.getScale());
            StaticLayout textLayout = StaticLayout.Builder.obtain(
                    textModel.getText(),
                    0,
                    textModel.getText().length(),
                    textPaint,
                    (int) rect.width())
                    .setBreakStrategy(BREAK_STRATEGY_SIMPLE)
                    .build();
            textLayout.draw(canvas);
            rect.bottom = rect.top + textLayout.getHeight();
            canvas.restore();
            if (textModel.isSelected()) {
                canvas.save();
                canvas.scale(canvasScale, canvasScale);
                canvas.drawRect(rect, paint);
                canvas.restore();
            }
//            Log.d(TAG, "onDraw: " + textModel);
        }
        canvas.save();
        canvas.scale(canvasScale, canvasScale);
        canvas.drawRect(50, 50, 150, 150, paint);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchPointCount = event.getPointerCount();
//        Log.d(TAG, "onTouchEvent: touchPointCount=" + touchPointCount);
        return gestureDetectorCompat.onTouchEvent(event) || scaleGestureDetector.onTouchEvent(event);
    }

}
