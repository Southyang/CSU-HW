package com.example.qrsystem.View.User;

import android.content.Intent;
import android.os.Bundle;

import com.example.qrsystem.MainActivity;
import com.example.qrsystem.Tool.ToastUtil;
import com.example.qrsystem.Valuetouse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrsystem.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InputInfor extends AppCompatActivity {
    public Button commit , exit;
    private EditText name , tel , address , kinds;
    private String TAG = "InputInfor";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;
    private TextView printfinfor;
    public String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_infor);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        this.getSupportActionBar().setTitle(username + " - 寄件中");
        //EditText
        name = (EditText)findViewById(R.id.receiver_input);
        tel = (EditText)findViewById(R.id.tel_input);
        address = (EditText)findViewById(R.id.house_input);
        kinds = (EditText)findViewById(R.id.kinds_input);
        //TextView
        printfinfor = (TextView) findViewById(R.id.printfinfor);
        //button绑定
        commit = (Button) findViewById(R.id.usercommit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户寄件");
                //获取输入信息，与后端交互，保存二维码
                final String msg1 = name.getText().toString();
                final String msg2 = tel.getText().toString();
                final String msg3 = address.getText().toString();
                final String msg4 = kinds.getText().toString();

                //非空检验
                if ("".equals(msg1) || "".equals(msg2) || "".equals(msg3) || "".equals(msg4)){
                    ToastUtil toastUtil = new ToastUtil(InputInfor.this,"信息不能为空");
                    toastUtil.show(500);
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createqrinfor(msg1 , msg2 , msg3 , msg4);
                    }
                }).start();
            }
        });
        exit = (Button) findViewById(R.id.userexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户取消寄件");
                ToastUtil toastUtil = new ToastUtil(InputInfor.this,"取消寄件!");
                toastUtil.show(500);
                Intent intent = new Intent(InputInfor.this, User.class);
                intent.putExtra("username",username);
                startActivity(intent); //跳转
                finish();
            }
        });
    }

    void createqrinfor(String name , String tel , String address , String kinds){
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/user/input?name=" + name + "&tel=" + tel + "&address=" + address + "&kinds=" + kinds;
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
                if(result.equals("0")){ //若登录失败则展示失败信息
                    Log.i(TAG,"寄件失败");
                    printfinfor.setText("寄件失败");
                }
                else{ //登陆成功，跳转到首页
                    printfinfor.setText("寄件成功");
                    Log.i(TAG,"寄件成功");
                    System.out.println("用户寄件成功");
                    Intent intent = new Intent(InputInfor.this, User.class);
                    intent.putExtra("username",username);
                    intent.putExtra("id",result);
                    intent.putExtra("flag","success");
                    startActivity(intent); //跳转
                    finish();
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
