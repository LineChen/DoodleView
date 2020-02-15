package com.line.doodleview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showEdit(View view) {
        TextView contentView = new TextView(this);
        contentView.setText("what !!!!!");
        contentView.setTextColor(getResources().getColor(R.color.colorPrimary));
        contentView.setTextSize(12F);
        contentView.setBackgroundColor(Color.BLUE);
        PopupWindow popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(0);
        popupWindow.showAsDropDown(view, 0, 0);
    }
}
