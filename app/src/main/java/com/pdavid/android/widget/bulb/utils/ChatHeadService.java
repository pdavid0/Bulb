package com.pdavid.android.widget.bulb.utils;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.activities.MainActivity;

/**
 * @author Philippe on 2014-07-26.
 */
public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    private WindowManager.LayoutParams params;


    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.bulb);
        chatHead.setOnTouchListener(chatDrag);
        chatHead.setOnLongClickListener(mLongClick);
        chatHead.setOnClickListener(mClick);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(chatHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView();
    }

    private void removeView() {
        if (chatHead != null) windowManager.removeViewImmediate(chatHead);
    }

    View.OnLongClickListener mLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            removeView();
            return true;
        }
    };

    View.OnClickListener mClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            removeView();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    };

    View.OnTouchListener chatDrag = new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(chatHead, params);
                    return true;
            }
            return false;
        }
    };
}
