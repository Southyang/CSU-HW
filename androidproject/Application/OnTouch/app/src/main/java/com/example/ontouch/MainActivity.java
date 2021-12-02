package com.example.ontouch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnTouchListener{

    private GestureDetector mGestureDetector;
    ImageView tv;
    TextView action;
    ImageView picture;
    Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGestureDetector = new GestureDetector(new simpleGestureListener());

        tv = (ImageView)findViewById(R.id.tv);
        action = (TextView)findViewById(R.id.action);
        picture = (ImageView)findViewById(R.id.picture);


        tv.setOnTouchListener(this);
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setLongClickable(true);

        change = (Button)findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OtherActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }

    private class simpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;

        /*****OnGestureListener的函数*****/
        public boolean onDown(MotionEvent e) {
            Log.i("MyGesture", "onDown");
            return false;
        }

        public void onShowPress(MotionEvent e) {
            Log.i("MyGesture", "onShowPress");
            action.setText("较长点击");
        }

        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapUp");
            action.setText("单击");
            tv.setBackgroundColor(Color.rgb(202, 221, 239));
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("MyGesture", "onScroll:" + (e2.getX() - e1.getX()) + "   "
                    + distanceX);
            action.setText("滚动");
            float lengthX = (float)((e2.getX() - e1.getX()) * 0.1);
            float lengthY = (float)((e2.getY() - e1.getY()) * 0.1);
            picture.setX(picture.getX() + lengthX);
            picture.setY(picture.getY() + lengthY);
            return true;
        }

        public void onLongPress(MotionEvent e) {
            Log.i("MyGesture", "onLongPress");
            action.setText("长按");
            tv.setBackgroundColor(Color.rgb(225, 131, 87));
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            Log.i("MyGesture", "onFling");
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling left
                Log.i("MyGesture", "Fling left");
                action.setText("左划");
                picture.setX(picture.getX() - 200);
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling right
                Log.i("MyGesture", "Fling right");
                action.setText("右划");
                picture.setX(picture.getX() + 200);
            } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                    &&Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                Log.i("MyGesture", "Fling up");
                action.setText("下划");
                picture.setY(picture.getY() + 200);
            } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                    &&Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                Log.i("MyGesture", "Fling up");
                action.setText("上划");
                picture.setY(picture.getY() - 200);
            }
            return true;
        }

        /*****OnDoubleTapListener的函数*****/
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapConfirmed");
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            Log.i("MyGesture", "onDoubleTap");
            action.setText("双击");
            picture.setX(e.getX() - 150);
            picture.setY(e.getY() - 150);
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("MyGesture", "onDoubleTapEvent");
            return true;
        }

    }
}
