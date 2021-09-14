package com.line.doodleview.drawtext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import com.line.doodleview.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenliu on 2020-02-14.
 */
public class DrawTextViewGroup extends FrameLayout {

    public static final String TAG = "DrawTextView";

    public DrawTextViewGroup(Context context) {
        super(context);
    }

    public DrawTextViewGroup(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics());

        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                int pointerCount = e.getPointerCount();
                Log.d(TAG, "onDown: x,y=" + e.getX() + "," + e.getY());
                return touchPointCount == 1;
            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "onSingleTapConfirmed: ");
                final float leftTopX = e.getX();
                final float leftTopY = e.getY();

                boolean findText = false;
                for (int i = textModelList.size() - 1; i >= 0; i--) {
                    TextModel textModel = textModelList.get(i);
                    if (textModel.getRect().contains(leftTopX, leftTopY)) {
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

                int childCount = getChildCount();
                if (childCount > 0) {
                    EditText childAt = (EditText) getChildAt(0);
                    childAt.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int actionMasked = event.getActionMasked();
                            if (actionMasked == MotionEvent.ACTION_MOVE) {

                            }
                            return true;
                        }
                    });
                    Rect editRect = new Rect();
                    childAt.getHitRect(editRect);
                    Log.d(TAG, "onSingleTapConfirmed: 输入框rect=" + editRect);
                    if (editRect.contains((int) leftTopX, (int) leftTopY)) {
                        childAt.requestFocus();
                    } else {
                        String result = childAt.getText().toString();
                        TextPaint textPaint = new TextPaint();
                        textPaint.setTextSize(textSize);
                        float desiredWidth = StaticLayout.getDesiredWidth(result, textPaint) + 5;
                        StaticLayout st = new StaticLayout(result, textPaint, (int) desiredWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0F, true);
                        int height = st.getHeight();
                        Log.d(TAG, "解析文字：width=" + desiredWidth + ",height=" + height);


                        TextModel textModel = new TextModel();
                        textModel.setText(result);
                        RectF rect = textModel.getRect();
                        rect.left = editRect.left;
                        rect.top = editRect.top;
                        rect.right = editRect.right;
                        rect.bottom = editRect.bottom;
                        textModelList.add(textModel);
                        invalidate();
                        removeAllViews();
                    }
                    return true;
                }

                final MyEditText contentView = new MyEditText(getContext());
                contentView.setHint("输入文字");
                contentView.setSelection(contentView.getText().length());
                contentView.setGravity(Gravity.TOP | Gravity.LEFT);
                contentView.setTextColor(Color.DKGRAY);
                contentView.setTextSize(12F);
                contentView.setPadding(20, 20, 20, 20);
                contentView.setBackgroundResource(R.drawable.edit);
                FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                flp.topMargin = (int) leftTopY;
                flp.leftMargin = (int) leftTopX;
                removeAllViews();
                addView(contentView, flp);

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
                Log.d(TAG, "onScroll: distanceX= " + distanceX + ",distanceY= " + distanceY);
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

                int childCount = getChildCount();
                if (childCount > 0 && Math.abs(distanceX) < 20 && Math.abs(distanceY) < 20) {
                    View childAt = getChildAt(0);
                    FrameLayout.LayoutParams lp = (LayoutParams) childAt.getLayoutParams();
                    lp.topMargin -= distanceY;
                    lp.leftMargin -= distanceX;
                    childAt.setLayoutParams(lp);
                }

                return findText || childCount > 0;
            }
        });


    }

    private final List<TextModel> textModelList = new ArrayList<>();

    private GestureDetectorCompat gestureDetectorCompat;
    private int touchPointCount;
    private float canvasScale = 1.0f;

    private float textSize;

    String text = "";
    float textWidth;
    TextPaint textPaint;
    Paint paint;


    {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.DKGRAY);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

    }

    private int lastX, lastY;


    @Override
    protected void onDraw(Canvas canvas) {

        for (TextModel textModel : textModelList) {
            RectF rect = textModel.getRect();
            String text = textModel.getText();
            canvas.save();
            canvas.scale(canvasScale, canvasScale);
            canvas.translate(rect.left + 20, rect.top + 20);
            textPaint.setTextSize(textSize);
//            textPaint.setTextScaleX(textModel.getScale());
//            float desiredWidth = StaticLayout.getDesiredWidth(text, textPaint);
            StaticLayout textLayout = StaticLayout.Builder.obtain(
                    text,
                    0,
                    text.length(),
                    textPaint,
                    (int) rect.width())
                    .build();
            textLayout.draw(canvas);
//            rect.bottom = rect.top + textLayout.getHeight();
//            rect.right = rect.left + desiredWidth;
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
        return gestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return getChildCount() == 0;
        }
        return true;
    }

}
