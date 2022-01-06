package com.example.qrreceive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private Button receive;
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //2核2G
    public String basepath = "http://47.242.148.245:27905"; //2核4G
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("快递签收");

        receive = (Button) findViewById(R.id.receive);
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进行扫码操作ToastUtil toastUtil = new ToastUtil(Delivery.this,"扫码");
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                // 开始扫描
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents() + "已签收", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "扫描内容:" + result.getContents(), Toast.LENGTH_LONG).show();
                System.out.println(result.getContents());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Integer.parseInt(result.getContents()) + " 已签收");
                        updatestate(Integer.parseInt(result.getContents()));
                    }
                }).start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void updatestate(int id){
//状态修改功能
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/delivery/updatestate?id=" + id + "&state=" + "已签收";
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
                    Log.i(TAG,"修改成功");
                    System.out.println("状态修改成功");
                }else{
                    //若登录失败则展示失败信息
                    System.out.println("状态修改失败");
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
