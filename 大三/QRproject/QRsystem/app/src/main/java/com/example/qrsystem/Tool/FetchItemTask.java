package com.example.qrsystem.Tool;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchItemTask extends AsyncTask<String, Void, Void> {
    public static final String TAG = "QRsystem";
    public String baseUrl;
    @Override
    protected Void doInBackground(String...params){
        try{
            getUrlBytes(params[0]);
        }catch (Exception e){
        e.printStackTrace();
        }
        return null;
    }

    public byte[] getUrlBytes(String urlSpec) throws Exception{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + " :with "+urlSpec);
            }

            int bytesRead = 0;//记录长度
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            if(out.toString().equals("1")){
                System.out.println(" 登录成功 ");
            }
            else {
                System.out.println(" 登录失败 ");
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
