package com.line.doodleview.drawtext;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * created by chenliu on  2021/9/14 4:55 下午.
 */
public class MyEditText extends AppCompatEditText {

    private int downX, downY;

    public MyEditText(Context context) {
        super(context);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int upX = (int) event.getRawX() - downX;
                int upY = (int) event.getRawY() - downY;
                if (Math.abs(upX) <= ViewConfiguration.get(getContext()).getScaledTouchSlop() && Math.abs(upY) <= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if (selStart == selEnd) {//防止不能多选
            setSelection(getText().length());
        }
    }


}
