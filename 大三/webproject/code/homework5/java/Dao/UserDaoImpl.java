package Dao;

import User.User;
import Utils.ConnectionUtils;
import com.mysql.cj.conf.ConnectionUrl;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;
        //JDBC:获取连接 编写SQL 预编译 设置参数 执行SQL 封装结果 关闭连接
        //获取连接
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            
            //sql操作
            String sql = "select id , username , password from test_user where username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , username);
            ps.setString(2 , password);
            //System.out.println("输入信息：username:" + username + " password:" + password);
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
    public User getUserByUsername(String username) {
        User user = null;
        //JDBC:获取连接 编写SQL 预编译 设置参数 执行SQL 封装结果 关闭连接
        //获取连接
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");

            //sql操作
            String sql = "select id , username , password from test_user where username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , username);

            //执行sql
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
    public void insertUser(String username, String password, String email) {
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");

            //sql操作
            String sql = "insert into test_user (username,password,email) value(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , username);
            ps.setString(2,password);
            ps.setString(3,email);

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;
        //JDBC:获取连接 编写SQL 预编译 设置参数 执行SQL 封装结果 关闭连接
        //获取连接
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");

            //sql操作
            String sql = "select id , username , password from test_user where email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , email);

            //执行sql
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
    public List<User> selectAllUser() {
        List<User> users = new ArrayList<User>();
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql
            String sql = "select * from test_user";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
            return users;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void updatepassword(String username, String password, String newpassword) {
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");

            //sql操作
            String sql = "update test_user set password=? where username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , newpassword);
            ps.setString(2 , username);
            ps.setString(3 , password);

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteUser(String username, String password, String email) {
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");

            //sql操作
            String sql = "delete from test_user where username = ? and password = ? and email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , username);
            ps.setString(2 , password);
            ps.setString(3 , email);

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                ConnectionUtils.closeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
