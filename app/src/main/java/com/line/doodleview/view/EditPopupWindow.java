package com.line.doodleview.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.line.doodleview.R;

/**
 * Created by chenliu on 2020-02-15.
 */
public class EditPopupWindow extends PopupWindow {

    public EditPopupWindow(Context context) {

        View layout = LayoutInflater.from(context).inflate(R.layout.layout_edit_window, new LinearLayout(context), false);
//        EditText editText = layout.findViewById(R.id.edit_text);
        setContentView(layout);
        //设置宽与高
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new BitmapDrawable());
        /**
         * 设置可以获取集点
         */
        setFocusable(true);
        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(true);
        setAnimationStyle(0);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.d("EditPopupWindow", "onDismiss:");
            }
        });
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT == 24 && anchor != null) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}
