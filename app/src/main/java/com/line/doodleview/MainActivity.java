package com.line.doodleview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = findViewById(R.id.editText);
        final TextView tvDisplay = findViewById(R.id.tvDisplay);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvDisplay.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void showEdit(View view) {
//        TextView contentView = new TextView(this);
//        contentView.setText("what !!!!!");
//        contentView.setTextColor(getResources().getColor(R.color.colorPrimary));
//        contentView.setTextSize(12F);
//        contentView.setBackgroundColor(Color.BLUE);
//        PopupWindow popupWindow = new PopupWindow(contentView);
//        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.setAnimationStyle(0);
//        popupWindow.showAsDropDown(view, 0, 0);
    }
}
