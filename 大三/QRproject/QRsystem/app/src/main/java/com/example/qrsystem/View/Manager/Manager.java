package com.example.qrsystem.View.Manager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.qrsystem.R;
import com.example.qrsystem.Tool.ToastUtil;
import com.example.qrsystem.Valuetouse;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Manager extends AppCompatActivity {
    private ListView mListView;
    private TextView qrinfor;
    public ArrayList<String> obj;
    public String TAG = "Manager";
    //public String basepath = "http://192.168.31.214:27905"; //寝室WiFi
    //public String basepath = "http://192.168.43.227:27905"; //手机热点
    //public String basepath = "http://southyang.cn:27905"; //服务器 http://southyang.cn:27905
    public String basepath = new Valuetouse().basepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Intent intent = getIntent();
        this.getSupportActionBar().setTitle("管理员 " + intent.getStringExtra("username"));
        obj = intent.getStringArrayListExtra("list");
        System.out.println("obj:" + obj);
        initview();
    }

    void initview(){
        qrinfor = (TextView) findViewById(R.id.qrinfor);
        mListView = (ListView) findViewById(R.id.list_view);
        //第二个参数，也可以新建一个布局文件，在这个布局文件的TextView当中设置其他属性。因为每个Item本身就是一个TextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, obj);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //ListView的列表项的单击事件
            @Override
            //第一个参数：指的是这个ListView；第二个参数：当前单击的那个item
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("parent="+parent.getClass());
                System.out.println("view="+view.getClass());

                //既然当前点击的那个item是一个TextView，那我们可以将其强制转型为TextView类型，然后通过getText()方法取出它的内容,紧接着以吐司的方式显示出来
                TextView tv = (TextView)view;
                final String qrid = tv.getText().toString().substring(tv.getText().toString().length() - 6);
                ToastUtil toastUtil = new ToastUtil(Manager.this,qrid);
                toastUtil.show(500);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String qrinformsg = getqridinfor(Integer.parseInt(qrid));
                        System.out.println(qrinformsg);
                        Intent intent = new Intent(Manager.this, Qrinfor.class);
                        intent.putExtra("qrinformsg",qrinformsg);
                        startActivity(intent); //跳转
                    }
                }).start();

                System.out.println("position="+position);
                System.out.println("id="+id);
            }
        });
    }

    String getqridinfor(int id){
        //通过网页来进行前后端数据交互，以json类型传输.
        String path = basepath + "/manager/getqrinfor?id=" + id;
        try {
            URL url = new URL(path);

            System.out.println("Manager :" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                if(result.equals("0")){
                    //若查询失败则展示失败信息
                    System.out.println("无此快递信息");
                    return "无此快递";
                }
                else{ //查询成功
                    //qrinfor.setText(result);
                    Log.i(TAG,"查询成功");
                    System.out.println("管理员查询成功");
                    return result;
                }
            }else{
                Log.i(TAG,"访问服务器失败");
                return "访问服务器失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Manager " + e);
        }
        return "查询失败";
    }
}
