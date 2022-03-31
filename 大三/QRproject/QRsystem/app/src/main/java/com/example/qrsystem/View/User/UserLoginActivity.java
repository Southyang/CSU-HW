package com.example.qrsystem.View.User;

import android.content.Intent;
import android.os.Bundle;
import com.example.qrsystem.MainActivity;
import com.example.qrsystem.Tool.ToastUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.qrsystem.R;
import com.example.qrsystem.Valuetouse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserLoginActivity extends AppCompatActivity {
    private Button Login , Exit , Getpass , Register;
    private EditText username , password;
    private String TAG = "UserLoginActivity";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;

    private TextView printfinfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().setTitle("用户登录");

        initView();
        settingbtn();
    }

    void initView(){
        //EditText绑定
        username = (EditText)findViewById(R.id.username_input);
        password = (EditText)findViewById(R.id.password_input);
        //button绑定
        Login = (Button) findViewById(R.id.userlogin);
        Exit = (Button) findViewById(R.id.userexit);
        Getpass = (Button) findViewById(R.id.usergetpassword);
        Register = (Button) findViewById(R.id.userregister);
        //TextView
        printfinfor = (TextView)findViewById(R.id.printfinfor);
    }

    void settingbtn(){
        //按钮设置
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户登录开始");
                //获取输入信息，与后端交互，判断信息是否正确
                System.out.println("登录信息为：" + username.getText().toString() + " " + password.getText().toString());
                //请求后台，进行密码检查
                final String usernamemsg = username.getText().toString();
                final String passwordmsg = password.getText().toString();

                //非空检验
                if ("".equals(usernamemsg) || "".equals(passwordmsg)){
                    ToastUtil toastUtil = new ToastUtil(UserLoginActivity.this,"信息不能为空");
                    toastUtil.show(500);
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkinfor(usernamemsg , passwordmsg);
                    }
                }).start();
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户退出登录");
                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                startActivity(intent); //跳转
                finish();
            }
        });

        Getpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户忘记密码");
                //查询密码，返回密码
                Intent intent = new Intent(UserLoginActivity.this , Userpassword.class);
                startActivity(intent);
                ToastUtil toastUtil = new ToastUtil(UserLoginActivity.this,"忘记密码");
                toastUtil.show(500);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("用户注册");
                //用户注册
                Intent intent = new Intent(UserLoginActivity.this, Userregister.class);
                startActivity(intent); //跳转
                ToastUtil toastUtil = new ToastUtil(UserLoginActivity.this,"用户注册");
                toastUtil.show(500);
            }
        });
    }

    //检查账号密码是否正确
    void checkinfor(String usernamemsg , String passwordmsg){
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/user/login?username=" + usernamemsg + "&password=" + passwordmsg;
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
                if(result.equals("1")){    //登陆成功，跳转到首页
                    printfinfor.setText("登录成功");
                    Log.i(TAG,"登录成功");
                    System.out.println("用户登录成功");
                    Intent intent = new Intent(UserLoginActivity.this, User.class);
                    intent.putExtra("username" , usernamemsg);
                    startActivity(intent); //跳转
                    finish();
                }else{
                    //若登录失败则展示失败信息
                    printfinfor.setText("用户名或密码错误");
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
