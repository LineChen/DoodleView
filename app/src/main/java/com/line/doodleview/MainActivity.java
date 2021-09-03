package com.line.doodleview;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.*;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private EditText editText;
    private TextView tvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        tvDisplay = findViewById(R.id.tvDisplay);
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
        calculateWidthFromFontSize(editText.getText().toString(), (int) (getResources().getDisplayMetrics().density * 12));
    }

    private int calculateWidthFromFontSize(String testString, int currentSize) {
        Rect bounds = new Rect();
        TextPaint paint = new TextPaint();
        paint.setTextSize(currentSize);
        paint.getTextBounds(testString, 0, testString.length(), bounds);
        Log.d(TAG, "calculateWidthFromFontSize: " + bounds);
        Log.d(TAG, "calculateWidthFromFontSize: bounds尺寸=" + bounds.width() + "," + bounds.height());
        Log.d(TAG, "calculateWidthFromFontSize: textView尺寸=" + tvDisplay.getWidth() + "," + tvDisplay.getHeight());

        float desiredWidth = StaticLayout.getDesiredWidth(testString, paint);
        Log.d(TAG, "StaticLayout.getDesiredWidth===" + desiredWidth);
        return (int) Math.ceil(bounds.width());
    }


}
