package com.example.demo.Controller;

import com.example.demo.Dao.QrDao;
import com.example.demo.Dao.UserDao;
import com.example.demo.DaoImpl.QrDaoImpl;
import com.example.demo.DaoImpl.UserDaoImpl;
import com.example.demo.User.Qr;
import com.example.demo.User.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private Map<String,Object> params = new HashMap<>();

    //无参方式
    @GetMapping(path = "selectall")
    public Object selectall() {
        //查找用户
        UserDao userDao = new UserDaoImpl();
        User user = userDao.selectAll();

        params.clear();
        params.put("id",user.getId());
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());
        System.out.println("用户查找成功");
        return params;
    }

    @GetMapping(path = "/login")
    public Object login(@RequestParam(required = true, value = "username", defaultValue = "") String username ,
                        @RequestParam(required = true, value = "password", defaultValue = "") String password) {
        //判断用户密码是否正确
        UserDao userDao = new UserDaoImpl();
        User user = userDao.getpasswordByusername(username);

        System.out.println("用户信息:" + username + " " + password);
        params.clear();
        System.out.println("用户密码查找成功");
        if(user == null){
            return "0";
        }
        if(password.equals(user.getPassword())){
            return "1";
        }
        else{
            return "0";
        }
    }

    @GetMapping(path = "/input")
    public Object input(@RequestParam(required = true, value = "name", defaultValue = "") String name ,
                        @RequestParam(required = true, value = "tel", defaultValue = "") String tel ,
                        @RequestParam(required = true, value = "address", defaultValue = "") String address ,
                        @RequestParam(required = true, value = "kinds", defaultValue = "") String kinds) {
        QrDao qrDao = new QrDaoImpl();
        Qr qr = qrDao.createQR(name , tel , address , kinds);

        System.out.println("二维码信息:" + name + " " + tel + " " + address + " " + kinds);
        params.clear();
        System.out.println("二维码创建成功");
        if(qr == null){ //未创建成功
            return "0";
        }
        else{ //返回id
            return qr.getId();
        }
    }

    @GetMapping(path = "/register")
    public Object register(@RequestParam(required = true, value = "username", defaultValue = "") String username ,
                        @RequestParam(required = true, value = "password", defaultValue = "") String password) {
        UserDao userDao = new UserDaoImpl();
        userDao.register(username , password);

        System.out.println("用户信息:" + username + " " + password);
        return "success";
    }

    @GetMapping(path = "/forgetpassword")
    public Object forgetpassword(@RequestParam(required = true, value = "username", defaultValue = "") String username) {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.getpasswordByusername(username);

        System.out.println("用户信息:" + username);
        params.clear();
        System.out.println("密码查询结束");
        if(user == null){ //无查询结果
            return "0";
        }
        else{ //返回password
            return user.getPassword();
        }
    }

    @GetMapping(path = "/getqrinfor")
    public Object getqrinfor(@RequestParam(required = true, value = "id", defaultValue = "") int id) {
        QrDao qrDao = new QrDaoImpl();
        Qr qr = qrDao.getQrinfor(id);

        System.out.println("查询信息:" + id);
        params.clear();
        System.out.println("用户查询快递信息");
        if(qr == null){
            System.out.println("无此快递");
            return "0";
        }
        else{
            System.out.println("查找成功");
            params.put("name",qr.getName());
            params.put("tel",qr.getTel());
            params.put("address",qr.getAddress());
            params.put("kinds",qr.getKinds());
            params.put("state",qr.getState());
            System.out.println(params);
            return params;
        }
    }
}
