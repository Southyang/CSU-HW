package Dao;

import User.User;
import Utils.ConnectionUtils;
import com.mysql.cj.conf.ConnectionUrl;

import java.sql.*;
import java.text.SimpleDateFormat;

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
}
