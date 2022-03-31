package com.example.demo.Controller;

import com.example.demo.Dao.QrDao;
import com.example.demo.Dao.UserDao;
import com.example.demo.DaoImpl.DeliveryDaoImpl;
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
@RequestMapping(path = "/delivery")
public class DeliveryController {
    private Map<String,Object> params = new HashMap<>();

    @GetMapping(path = "/selectall")
    public Object selectall() {
        //查找用户
        UserDao userDao = new DeliveryDaoImpl();
        User user = userDao.selectAll();

        params.clear();
        params.put("id",user.getId());
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());
        System.out.println("快递员查找成功");
        return params;
    }

    @GetMapping(path = "/login")
    public Object login(@RequestParam(required = true, value = "username", defaultValue = "") String username ,
                        @RequestParam(required = true, value = "password", defaultValue = "") String password) {
        //判断用户密码是否正确
        UserDao userDao = new DeliveryDaoImpl();
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

    @GetMapping(path = "/getqrinfor")
    public Object getqrinfor(@RequestParam(required = true, value = "id", defaultValue = "") int id ,
                            @RequestParam(required = true, value = "username", defaultValue = "") String username) {
        QrDao qrDao = new QrDaoImpl();
        Qr qr = qrDao.getQrinforpermission(id , username);

        System.out.println("查询信息:" + id + " " + username);
        params.clear();
        System.out.println("快递员权限查询");
        if(qr == null){
            System.out.println("无权限查看");
            return "0";
        }
        if(username.equals(qr.getPermission())){
            qr = qrDao.getQrinfor(id);
            params.put("name",qr.getName());
            params.put("tel",qr.getTel());
            params.put("address",qr.getAddress());
            params.put("kinds",qr.getKinds());
            params.put("state",qr.getState());
            System.out.println(params);
            return params;
        }
        else{
            System.out.println("无权限查看");
            return "0";
        }
    }

    @GetMapping(path = "/register")
    public Object register(@RequestParam(required = true, value = "username", defaultValue = "") String username ,
                           @RequestParam(required = true, value = "password", defaultValue = "") String password) {
        UserDao userDao = new DeliveryDaoImpl();
        userDao.register(username , password);

        System.out.println("用户信息:" + username + " " + password);
        return "success";
    }

    @GetMapping(path = "/forgetpassword")
    public Object forgetpassword(@RequestParam(required = true, value = "username", defaultValue = "") String username) {
        UserDao userDao = new DeliveryDaoImpl();
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

    @GetMapping(path = "/updatestate")
    public Object updatestate(@RequestParam(required = true, value = "id", defaultValue = "") int id,
                                   @RequestParam(required = true, value = "state", defaultValue = "") String state) {
        QrDao qrDao = new QrDaoImpl();
        qrDao.updateQrstate(id , state);

        System.out.println("状态信息:" + id + " " + state);
        System.out.println("状态修改结束");
        return "success";
    }
}
