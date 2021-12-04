package Dao;

import User.User;

import java.util.List;

public interface UserDao {
    public User getUserByUsernameAndPassword(String username , String password);

    public User getUserByUsername(String username);

    public void insertUser(String username , String password , String email);

    public User getUserByEmail(String email);

    public List<User> selectAllUser();

    public void updatepassword(String username , String password , String newpassword);

    public void deleteUser(String username , String password , String email);
}
