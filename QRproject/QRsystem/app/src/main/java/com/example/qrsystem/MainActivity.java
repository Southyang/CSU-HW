package com.example.qrsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qrsystem.Tool.FetchItemTask;
import com.example.qrsystem.View.Delivery.DeliveryLoginActivity;
import com.example.qrsystem.View.Manager.ManagerLoginActivity;
import com.example.qrsystem.View.User.UserLoginActivity;



public class MainActivity extends AppCompatActivity {
    public static final String TAG = "QRsystem";
    public String baseUrl;
    private Button userbutton , deliverybutton , managerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();//去掉顶部绿色栏

        //button绑定
        userbutton = (Button) findViewById(R.id.userbutton);
        userbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //baseUrl = "http://192.168.31.214:8080/getuser";
                //new FetchItemTask().execute(baseUrl);
                System.out.println("用户登录");
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(intent); //跳转
            }
        });
        deliverybutton = (Button) findViewById(R.id.deliverybutton);
        deliverybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //baseUrl = "http://192.168.31.214:8080/getpackager";
               // new FetchItemTask().execute();
                System.out.println("快递员登录");
                Intent intent = new Intent(MainActivity.this, DeliveryLoginActivity.class);
                startActivity(intent); //跳转
            }
        });
        managerbutton = (Button) findViewById(R.id.managerbutton);
        managerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //baseUrl = "http://192.168.31.214:8080/getmanager";
                //new FetchItemTask().execute(baseUrl);
                System.out.println("管理员登录");
                Intent intent = new Intent(MainActivity.this, ManagerLoginActivity.class);
                startActivity(intent); //跳转
            }
        });
    }
}
