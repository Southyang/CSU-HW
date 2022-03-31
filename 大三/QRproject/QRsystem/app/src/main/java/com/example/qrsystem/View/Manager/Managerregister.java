package com.example.qrsystem.View.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrsystem.R;
import com.example.qrsystem.Tool.ToastUtil;
import com.example.qrsystem.Valuetouse;
import com.example.qrsystem.View.User.UserLoginActivity;
import com.example.qrsystem.View.User.Userregister;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Managerregister extends AppCompatActivity {
    private Button registerbtn;
    private EditText username , password;
    private TextView registerinfor;
    private String TAG = "Managerregister";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().setTitle("管理员注册");

        initView();
        buttondone();
    }

    void initView(){
        registerinfor = (TextView) findViewById(R.id.registerinfor);
        username = (EditText) findViewById(R.id.registername);
        password = (EditText) findViewById(R.id.registerpassword);
        registerbtn = (Button) findViewById(R.id.registerbtn);
    }

    void buttondone(){
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernamemsg = username.getText().toString();
                final String passwordmsg = password.getText().toString();

                //非空检验
                if ("".equals(usernamemsg) || "".equals(passwordmsg)){
                    ToastUtil toastUtil = new ToastUtil(Managerregister.this,"信息不能为空");
                    toastUtil.show(500);
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        register(usernamemsg , passwordmsg);
                    }
                }).start();
            }
        });
    }

    void register(String usernamemsg , String passwordmsg){
        //注册功能
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/manager/register?username=" + usernamemsg + "&password=" + passwordmsg;
        try {
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                if(result.equals("success")){    //登陆成功，跳转到登录界面
                    registerinfor.setText("注册成功");
                    Log.i(TAG,"注册成功");
                    System.out.println("管理员注册成功");
                    Intent intent = new Intent(Managerregister.this, UserLoginActivity.class);
                    startActivity(intent); //跳转
                    finish();
                }else{
                    //若登录失败则展示失败信息
                    registerinfor.setText("注册失败");
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

