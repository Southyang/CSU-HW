package com.example.qrsystem.View.Delivery;

import android.content.Intent;
import android.os.Bundle;

import com.example.qrsystem.Tool.ToastUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrsystem.R;
import com.example.qrsystem.Valuetouse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.alibaba.fastjson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Delivery extends AppCompatActivity {
    private String TAG = "Delivery";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;
    private final String LOGTAG = "QRCScanner-MainActivity";
    public Button scan, create;
    private ImageView qrimage;
    private TextView nameinfor, qrid , telinfor , addressinfor , kindsinfor , stateinfor;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        //this.getSupportActionBar().hide();//去掉顶部绿色栏
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        System.out.println("快递员" + username + "已登录");
        this.getSupportActionBar().setTitle("快递员 " + username);

        initView();

        buttondone();
    }

    void initView(){
        telinfor = (TextView) findViewById(R.id.telinfor);
        qrid = (TextView) findViewById(R.id.qrid);
        nameinfor = (TextView) findViewById(R.id.nameinfor);
        qrimage = (ImageView) findViewById(R.id.qrimage);
        telinfor = (TextView) findViewById(R.id.telinfor);
        addressinfor = (TextView) findViewById(R.id.addressinfor);
        kindsinfor = (TextView) findViewById(R.id.kindsinfor);
        stateinfor = (TextView) findViewById(R.id.stateinfor);
    }

    void changevalue(){
        qrid.setText("");
        nameinfor.setText("");
        telinfor.setText("");
        addressinfor.setText("");
        kindsinfor.setText("");
        stateinfor.setText("");
    }

    void buttondone(){
        //button绑定
        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("快递员准备扫码");
                //进行扫码操作
                ToastUtil toastUtil = new ToastUtil(Delivery.this, "扫码");
                toastUtil.show(500);
                changevalue();
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(Delivery.this);
                // 开始扫描
                intentIntegrator.initiateScan();
            }
        });
        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("快递员确认派件");
                //进行派件操作
                ToastUtil toastUtil;
                if ("".equals(qrid.getText().toString())){
                    toastUtil = new ToastUtil(Delivery.this, "无快递单可派件");
                    toastUtil.show(500);
                    return;
                }
                toastUtil = new ToastUtil(Delivery.this, "当前快递已派送");
                toastUtil.show(500);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        confirm(qrid.getText().toString().substring(qrid.getText().toString().length() - 6));
                    }
                }).start();
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
                final String temp = username;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("输出内容:" + Integer.parseInt(result.getContents()) + " " + temp);
                        getqrinfor(Integer.parseInt(result.getContents()), temp);
                    }
                }).start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void getqrinfor(int id, String username) {
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/delivery/getqrinfor?id=" + id + "&username=" + username;
        try {
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                if (result.equals("0")) {
                    //若登录失败则展示失败信息
                    nameinfor.setText("无查看权限");
                } else { //登陆成功
                    //scanresult.setText(result);
                    JSONObject jo = JSON.parseObject(result);

                    qrid.setText("快递单号:" + id);
                    nameinfor.setText("姓名:" + jo.getString("name"));
                    telinfor.setText("电话:" + jo.getString("tel"));
                    addressinfor.setText("住址:" + jo.getString("address"));
                    kindsinfor.setText("快递种类:" + jo.getString("kinds"));
                    stateinfor.setText("快递状态:" + jo.getString("state"));
                    System.out.println(jo);

                    Log.i(TAG, "扫码成功");
                    System.out.println("快递员扫码成功");
                }
            } else {
                Log.i(TAG, "访问服务器失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    void confirm(String strid) { //确认派件
        int id = Integer.parseInt(strid);
        System.out.println("id:" + id);
        //状态修改功能
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/delivery/updatestate?id=" + id + "&state=" + "派件中";
        try {
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                if (result.equals("success")) {    //登陆成功，跳转到登录界面
                    Log.i(TAG, "修改成功");
                    System.out.println("状态修改成功");
                } else {
                    //若登录失败则展示失败信息
                    System.out.println("状态修改失败");
                }
            } else {
                Log.i(TAG, "访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
