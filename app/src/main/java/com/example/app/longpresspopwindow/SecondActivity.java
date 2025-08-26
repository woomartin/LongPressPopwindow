package com.example.app.longpresspopwindow;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        View aView = findViewById(R.id.aView);
        View cView = findViewById(R.id.cView);
        cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "C onClick: ");
            }
        });
        View bView = findViewById(R.id.bView);

        aView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AView消费点击事件
                Log.e("TAG", "A onClick: ");
            }
        });

        bView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("TAG", "B setOnLongClickListener: ");
                return true;
            }
        });

        bView.setOnTouchListener(new View.OnTouchListener() {

            private long downTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downTime = SystemClock.uptimeMillis();
                        forwardMotionEvent(bView, event);
                        break;

                    case MotionEvent.ACTION_UP:
                        long duration = SystemClock.uptimeMillis() - downTime;
                        if (duration < ViewConfiguration.getLongPressTimeout()) {
                            forwardMotionEvent(bView, event);
                        }
                        break;
                }
                return false;
            }

        });

    }


    private void forwardMotionEvent(View bView, MotionEvent event) {
        ViewParent viewParent = bView.getParent();
        if (viewParent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) viewParent;
            int childCount = viewGroup.getChildCount();
            if (childCount > 0) {
                View view = viewGroup.getChildAt(0);
                if (view != bView) {
                    view.dispatchTouchEvent(event);
                }
            }
        }
    }

    private void forwardTouchToBView(View view, MotionEvent originalEvent) {

        float x = originalEvent.getX();
        float y = originalEvent.getY();

        long downTime = SystemClock.uptimeMillis();

        // 模拟点击事件（down -> up）
        MotionEvent down = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        MotionEvent up = MotionEvent.obtain(downTime, downTime + 50, MotionEvent.ACTION_UP, x, y, 0);

        view.dispatchTouchEvent(down);
        view.dispatchTouchEvent(up);

        down.recycle();
        up.recycle();
    }
}