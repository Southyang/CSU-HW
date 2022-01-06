package com.example.qrsystem.View.User;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Userpassword extends AppCompatActivity {
    private Button forgetbtn;
    private EditText username;
    private TextView forgetinfor;
    private String TAG = "Userpassword";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        this.getSupportActionBar().setTitle("用户-找回密码");

        initView();
        buttondone();
    }

    void initView(){
        forgetinfor = (TextView) findViewById(R.id.forgetinfor);
        username = (EditText) findViewById(R.id.forgetname);
        forgetbtn = (Button) findViewById(R.id.forgetbtn);
    }

    void buttondone(){
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernamemsg = username.getText().toString();

                //非空检验
                if ("".equals(usernamemsg)){
                    ToastUtil toastUtil = new ToastUtil(Userpassword.this,"信息不能为空");
                    toastUtil.show(500);
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        forgetpassword(usernamemsg);
                    }
                }).start();
            }
        });
    }

    void forgetpassword(String usernamemsg){
        //注册功能
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/user/forgetpassword?username=" + usernamemsg;
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
                if(!result.equals("0")){    //登陆成功，跳转到登录界面
                    forgetinfor.setText("您的密码为:" + result);
                    Log.i(TAG,"密码找回成功");
                    System.out.println("用户密码找回成功");
                }else{
                    //若登录失败则展示失败信息
                    forgetinfor.setText("无此账号信息");
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

