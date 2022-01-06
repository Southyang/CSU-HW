package com.example.demo.Dao;

import com.example.demo.User.Qr;

import java.util.List;

public interface QrDao {
    public Qr createQR(String name , String tel , String address , String kinds);

    public Qr getQrinforpermission(int id , String username);

    public Qr getQrinfor(int id);

    public void updateQrpermission(int id , String username);

    public List<Qr> selectAllid();

    public Qr getqrinforByid(int id);

    public void updateQrstate(int id , String newstate);
}
