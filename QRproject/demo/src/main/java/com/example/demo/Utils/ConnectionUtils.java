package com.example.demo.Utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//连接的工具类
public class ConnectionUtils {
    private static String driver = null;
    private static String url = null;
    private static String dbuser = null;
    private static String dbpassword = null;
    private static Properties props = new Properties();

    //ThreadLocal 保证一个线程中只有一个连接
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    //读取配置文件
    static {
        //使用类加载器读取
        try{
            InputStream in = ConnectionUtils.class.getClassLoader().getResourceAsStream("db.properties");
            props.load(in);

            driver = props.getProperty("jdbc.driver");
            url = props.getProperty("jdbc.url");
            dbuser = props.getProperty("jdbc.dbuser");
            dbpassword = props.getProperty("jdbc.dbpassword");

            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取连接的方法
    public static Connection getConn() throws Exception {
        //从tl中获取
        Connection conn = tl.get();
        if (conn == null){
            conn = DriverManager.getConnection(url , dbuser , dbpassword);
            tl.set(conn);
        }
        return conn;
    }

    //关闭连接的方法
    public static void closeConn() throws Exception {
        //从tl中获取
        Connection conn = tl.get();
        if(conn != null){
            conn.close();
        }
        tl.set(null);
    }

    public static void main(String[] args) throws Exception{ //没啥用，测试代码，懒得删了
        Connection conn = getConn();

        Connection conn2 = getConn();

        System.out.println(conn != conn2);
    }
}
