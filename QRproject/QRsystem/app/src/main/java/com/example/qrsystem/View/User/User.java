package com.example.qrsystem.View.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RenderNode;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.example.qrsystem.Tool.ToastUtil;
import com.example.qrsystem.Tool.ZxingUtils;
import com.example.qrsystem.Valuetouse;
import com.example.qrsystem.View.Delivery.Delivery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrsystem.R;

import com.alibaba.fastjson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class User extends AppCompatActivity {
    private Button sendthing , findqrbtn;
    public String id , flag;
    private TextView idinfor;
    private TextView nameinfor, telinfor , addressinfor , kindsinfor , stateinfor;
    private ImageView qrimage;
    private EditText findqr;
    public String username;
    public String TAG = "User";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        id = intent.getStringExtra("id");
        flag = intent.getStringExtra("flag");
        this.getSupportActionBar().setTitle("用户 " + username);

        initView();
        buttondone();

        if("success".equals(flag)){
            idinfor.setText("您的快递单号为:"+id + "\n您的快递已出库\n快递二维码信息如下");
            //进行生成操作
            ToastUtil toastUtil = new ToastUtil(User.this,"请及时截图保存您的寄件码");
            toastUtil.show(2000);
            Bitmap qrCode = ZxingUtils.createQRCode(String.valueOf(id));
            qrimage.setImageBitmap(qrCode);
        }
    }

    void initView(){
        //ImageView
        qrimage = (ImageView) findViewById(R.id.qrimage);
        //TextView
        idinfor = (TextView) findViewById(R.id.idinfor);
        telinfor = (TextView) findViewById(R.id.telinfor);
        nameinfor = (TextView) findViewById(R.id.nameinfor);
        qrimage = (ImageView) findViewById(R.id.qrimage);
        telinfor = (TextView) findViewById(R.id.telinfor);
        addressinfor = (TextView) findViewById(R.id.addressinfor);
        kindsinfor = (TextView) findViewById(R.id.kindsinfor);
        stateinfor = (TextView) findViewById(R.id.stateinfor);
        //EditView
        findqr = (EditText) findViewById(R.id.findqr);
    }

    void buttondone(){
        //button绑定
        sendthing = (Button) findViewById(R.id.send);
        sendthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户填写寄件信息");
                changevalue();
                findqr.setText("");
                Intent intent = new Intent(User.this, InputInfor.class);
                intent.putExtra("username" , username);
                startActivity(intent); //跳转
            }
        });
        findqrbtn = (Button) findViewById(R.id.findqrbtn);
        findqrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(findqr.getText().toString())){
                    ToastUtil toastUtil = new ToastUtil(User.this,"请输入查询单号");
                    toastUtil.show(1000);
                    return;
                }

                final int id = Integer.parseInt(findqr.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getqrinfor(id);
                    }
                }).start();
            }
        });
    }

    void changevalue(){
        idinfor.setText("");
        nameinfor.setText("");
        telinfor.setText("");
        addressinfor.setText("");
        kindsinfor.setText("");
        stateinfor.setText("");
        qrimage.setImageDrawable(null);
    }

    void getqrinfor(int id){
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/user/getqrinfor?id=" + id;
        try {
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                changevalue();
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                if(result.equals("0")){
                    //若登录失败则展示失败信息
                    idinfor.setText("无快递信息");
                }
                else{ //登陆成功
                    JSONObject jo = JSON.parseObject(result);

                    nameinfor.setText("姓名:" + jo.getString("name"));
                    telinfor.setText("电话:" + jo.getString("tel"));
                    addressinfor.setText("住址:" + jo.getString("address"));
                    kindsinfor.setText("快递种类:" + jo.getString("kinds"));
                    stateinfor.setText("快递状态:" + jo.getString("state"));
                    System.out.println(jo);

                    Log.i(TAG,"查询成功");
                    System.out.println("用户查询成功");
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
