package com.example.demo.DaoImpl;

import com.example.demo.Dao.QrDao;
import com.example.demo.User.Qr;
import com.example.demo.User.User;
import com.example.demo.Utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QrDaoImpl implements QrDao {
    @Override
    public Qr createQR(String name, String tel, String address, String kinds) {
        Qr qr = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = "insert into qr_qrinfor (name,tel,address,kinds,state) value(?,?,?,?,'运输中')";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1 , name);
            ps.setString(2 , tel);
            ps.setString(3 , address);
            ps.setString(4 , kinds);
            ps.executeUpdate(); //插入数据

            //获取对应的id
            sql = "select id from qr_qrinfor where name = ? and tel = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql);
            ps1.setString(1 , name);
            ps1.setString(2 , tel);
            ResultSet rs = ps1.executeQuery();
            if(rs.next()){
                qr = new Qr();
                qr.setId(rs.getInt("id"));
            }
            return qr;
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
    public Qr getQrinforpermission(int id , String username) {
        Qr qr = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = "select permission from qr_qrinfor where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                qr = new Qr();
                qr.setPermission(rs.getString("permission"));
            }
            /*sql = "select name , tel , address , kinds from qr_qrinfor where id = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql);
            ps1.setInt(1,id);*/
            return qr;
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
    public Qr getQrinfor(int id) {
        Qr qr = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = sql = "select name , tel , address , kinds ,state from qr_qrinfor where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                qr = new Qr();
                qr.setName(rs.getString("name"));
                qr.setTel(rs.getString("tel"));
                qr.setAddress(rs.getString("address"));
                qr.setKinds(rs.getString("kinds"));
                qr.setState(rs.getString("state"));
            }
            return qr;
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
    public void updateQrpermission(int id , String username) {
        Qr qr = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = sql = "update qr_qrinfor set permission = ? where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setInt(2,id);
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
    }

    @Override
    public List<Qr> selectAllid() {
        List<Qr> qrs = new ArrayList<Qr>();
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql
            String sql = "select * from qr_qrinfor";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Qr qr = new Qr();
                qr.setId(rs.getInt("id"));
                qr.setName(rs.getString("name"));
                qr.setTel(rs.getString("tel"));
                qr.setAddress(rs.getString("address"));
                qr.setKinds(rs.getString("kinds"));
                qr.setPermission(rs.getString("permission"));
                qr.setState(rs.getString("state"));
                qrs.add(qr);
            }
            return qrs;
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
    public Qr getqrinforByid(int id) {
        Qr qr = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = sql = "select * from qr_qrinfor where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                qr = new Qr();
                qr.setId(rs.getInt("id"));
                qr.setName(rs.getString("name"));
                qr.setTel(rs.getString("tel"));
                qr.setAddress(rs.getString("address"));
                qr.setKinds(rs.getString("kinds"));
                qr.setPermission(rs.getString("permission"));
                qr.setState(rs.getString("state"));
            }
            return qr;
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
    public void updateQrstate(int id, String newstate) {
        Qr qr = null;
        try{
            Connection connection = ConnectionUtils.getConn();
            System.out.println(connection);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+" [server]:数据库已连接");
            //sql操作
            String sql = sql = "update qr_qrinfor set state = ? where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,newstate);
            ps.setInt(2,id);
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
    }
}
