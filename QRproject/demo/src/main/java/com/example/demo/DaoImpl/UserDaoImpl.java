package com.example.demo.DaoImpl;

import com.example.demo.Dao.UserDao;
import com.example.demo.User.User;
import com.example.demo.Utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class UserDaoImpl implements UserDao {
    @Override
    public User selectAll(){
        User user = null;
        //JDBC:获取连接 编写SQL 预编译 设置参数 执行SQL 封装结果 关闭连接
        //获取连接
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = "select * from qr_user";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User getpasswordByusername(String username) {
        User user = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = "select password from qr_user where username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user = new User();
                user.setPassword(rs.getString("password"));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Void register(String username, String password) {
        User user = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = "insert into qr_user (username , password) value(?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , username);
            ps.setString(2 , password);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
