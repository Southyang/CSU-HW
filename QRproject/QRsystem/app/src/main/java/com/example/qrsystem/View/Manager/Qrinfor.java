package com.example.qrsystem.View.Manager;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrsystem.R;
import com.example.qrsystem.Tool.ToastUtil;
import com.example.qrsystem.Tool.ZxingUtils;
import com.example.qrsystem.Valuetouse;
import com.example.qrsystem.View.User.UserLoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Qrinfor extends AppCompatActivity {
    public String qrinformsg;
    private TextView qrid , qrname , qrtel , qraddress , qrkinds , updateinfor , qrstate;
    private EditText qrpermission;
    private Button qrbtn;
    private ImageView qrimage;
    public String TAG = "Qrinfor";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrinfor);
        this.getSupportActionBar().setTitle("快递详细信息");

        Intent intent = getIntent();
        qrinformsg = intent.getStringExtra("qrinformsg");

        initView();

        textinit(qrinformsg);

        buttondone();
    }

    void initView(){
        qrimage = (ImageView) findViewById(R.id.qrimage);
        updateinfor = (TextView) findViewById(R.id.updateinfor);
        qrid = (TextView) findViewById(R.id.qrid);
        qrname = (TextView) findViewById(R.id.qrname);
        qrtel = (TextView) findViewById(R.id.qrtel);
        qraddress = (TextView) findViewById(R.id.qraddress);
        qrkinds = (TextView) findViewById(R.id.qrkinds);
        qrpermission = (EditText) findViewById(R.id.qrpermission);
        qrstate = (TextView) findViewById(R.id.qrstate);
        qrbtn = (Button) findViewById(R.id.qrbtn);
    }

    void textinit(String qrinformsg){
        try{
            System.out.println("qrinfor string:" + qrinformsg);
            JSONObject jo = new JSONObject(qrinformsg);
            Log.i(TAG, "" + jo);
            System.out.println("qrinfor json:" + jo);
            qrid.setText(jo.getString("id"));
            qrname.setText(jo.getString("name"));
            qrtel.setText(jo.getString("tel"));
            qraddress.setText(jo.getString("address"));
            qrkinds.setText(jo.getString("kinds"));
            qrpermission.setText(jo.getString("permission"));
            qrstate.setText(jo.getString("state"));
            Bitmap qrCode = ZxingUtils.createQRCode(jo.getString("id"));
            qrimage.setImageBitmap(qrCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void buttondone(){
        qrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil toastUtil = new ToastUtil(Qrinfor.this,"修改权限");
                toastUtil.show(500);

                final int id = Integer.parseInt(qrid.getText().toString());
                final String permission = qrpermission.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updatepermission(id , permission);
                    }
                }).start();
            }
        });
    }

    void updatepermission(int id , String permission){
        //权限修改功能
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/manager/updatepermission?id=" + id + "&permission=" + permission;
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
                    updateinfor.setText("修改成功");
                    Log.i(TAG,"修改成功");
                    System.out.println("权限修改成功");
                }else{
                    //若登录失败则展示失败信息
                    updateinfor.setText("修改失败");
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
