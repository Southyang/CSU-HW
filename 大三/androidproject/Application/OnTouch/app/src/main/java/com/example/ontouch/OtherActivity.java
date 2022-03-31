package com.example.ontouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;

public class OtherActivity extends Activity {
    private int num = 0;
    float postionX;
    float postionY;
    float distance;
    TextView action1;
    TextView content;
    ImageView picture1;
    Button change1;
    String[] bookcontent={"测试内容" , "还是测试内容" , "又是测试内容" , "只有测试内容" , "全都是测试内容" , "123465789" , "adasdasd" , "qwrqeggsdg"};
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        action1 = (TextView)findViewById(R.id.action1);
        content = (TextView)findViewById(R.id.content);
        content.setText(bookcontent[flag]);

        picture1 = (ImageView)findViewById(R.id.picture1);

        change1 = (Button)findViewById(R.id.change1);
        change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (x * x + y * y);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                num = 1;
                postionX = event.getX();
                postionY = event.getY();
                action1.setText("单击");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                num = num + 1;
                distance = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if(num >= 2){
                    float newdis = spacing(event);
                    if((newdis - distance) > 900){
                        action1.setText("放大");
                        ViewGroup.LayoutParams params = picture1.getLayoutParams();
                        if(params.width <= 600){
                            params.width = params.width + 10;
                            params.height = params.height + 10;
                            //Log.i("test:","wid:" + params.width + " hei:" + params.height);
                            picture1.setLayoutParams(params);
                        }
                        else{
                            action1.setText("已放大至最大");
                        }
                    }
                    else if((distance - newdis) > 900){
                        action1.setText("缩小");
                        ViewGroup.LayoutParams params = picture1.getLayoutParams();
                        if(params.width >= 50){
                            params.width = params.width - 10;
                            params.height = params.height - 10;
                            //Log.i("test:","wid:" + params.width + " hei:" + params.height);
                            picture1.setLayoutParams(params);
                        }
                        else{
                            action1.setText("已缩小至最小");
                        }
                    }
                }
                else {
                    if((postionX - event.getX()) > 200){ //左划
                        if(flag == bookcontent.length - 1){
                            action1.setText("当前是最后一页");
                            content.setText(bookcontent[flag]);
                        }
                        else{
                            flag = flag + 1;
                            content.setText(bookcontent[flag]);
                            try{
                                Thread.sleep(100);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                    else if((event.getX() - postionX) > 200){ //右划
                        if(flag == 0){
                            action1.setText("当前是第一页");
                            content.setText(bookcontent[flag]);
                        }
                        else{
                            flag = flag - 1;
                            content.setText(bookcontent[flag]);
                            try{
                                Thread.sleep(100);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                    else if(Math.abs(event.getX() - postionX) > 30 || Math.abs(event.getY() - postionY) > 30){
                        action1.setText("移动");
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                num = num - 1;
                action1.setText("抬起一指");
                break;
            case MotionEvent.ACTION_UP:
                num = 0;
                action1.setText("全部抬起");
                break;
        }
        return true;
    }
}
