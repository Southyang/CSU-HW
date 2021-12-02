package Dao;

import User.User;

public interface UserDao {
    public User getUserByUsernameAndPassword(String username , String password);
}
