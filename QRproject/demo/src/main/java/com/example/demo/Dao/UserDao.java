package com.example.demo.Dao;

import com.example.demo.User.User;

public interface UserDao {
    public User selectAll();

    public User getpasswordByusername(String username);

    public Void register(String username , String password);
}
