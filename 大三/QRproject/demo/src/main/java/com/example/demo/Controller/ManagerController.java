package com.example.demo.Controller;

import com.example.demo.Dao.QrDao;
import com.example.demo.Dao.UserDao;
import com.example.demo.DaoImpl.ManagerDaoImpl;
import com.example.demo.DaoImpl.QrDaoImpl;
import com.example.demo.DaoImpl.UserDaoImpl;
import com.example.demo.User.Qr;
import com.example.demo.User.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.*;

@RestController
@RequestMapping(path = "/manager")
@CrossOrigin
public class ManagerController {
    private Map<String,Object> params = new HashMap<>();

    //无参方式
    @GetMapping(path = "selectall")
    public Object selectall() {
        //查找用户
        UserDao userDao = new ManagerDaoImpl();
        User user = userDao.selectAll();

        params.clear();
        params.put("id",user.getId());
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());
        System.out.println("管理员查找成功");
        return params;
    }

    @GetMapping(path = "/login")
    public Object login(@RequestParam(required = true, value = "username", defaultValue = "") String username ,
                        @RequestParam(required = true, value = "password", defaultValue = "") String password) {
        //判断用户密码是否正确
        UserDao userDao = new ManagerDaoImpl();
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

    @GetMapping(path = "/selectAllqrid")
    public Object selectAllqrid() {
        //获取所有id
        QrDao qrDao = new QrDaoImpl();
        List<Qr> qrs = qrDao.selectAllid();

        System.out.println("查找所有快递信息");
        params.clear();

        for(int i = 0; i < qrs.size(); i++){
            params.put("id"+i,qrs.get(i).getId());
        }
        params.put("length",qrs.size());
        return params;
    }

    @GetMapping(path = "/selectAllqr")
    public Object selectAllqr() {
        //获取所有id
        List<JSONObject> res = new ArrayList<JSONObject>();
        QrDao qrDao = new QrDaoImpl();
        List<Qr> qrs = qrDao.selectAllid();

        System.out.println("查找所有快递信息");
        params.clear();

        for(int i = 0; i < qrs.size(); i++){
            JSONObject params1 = new JSONObject();
            params1.put("id" , qrs.get(i).getId());
            params1.put("name" , qrs.get(i).getName());
            params1.put("tel" , qrs.get(i).getTel());
            params1.put("address" , qrs.get(i).getAddress());
            params1.put("kinds" , qrs.get(i).getKinds());
            params1.put("premission" , qrs.get(i).getPermission());
            params1.put("state" , qrs.get(i).getState());

            System.out.println(params1);
            res.add(params1);
        }
        System.out.println(res);
        params.put("list",res);
        params.put("length",qrs.size());
        return params;
    }

    @GetMapping(path = "/getqrinfor")
    public Object getqrinfor(@RequestParam(required = true, value = "id", defaultValue = "") int id) {
        //获取对应id的信息
        QrDao qrDao = new QrDaoImpl();
        Qr qr = qrDao.getqrinforByid(id);

        System.out.println("查找所有快递信息");
        params.clear();
        if(qr == null){
            System.out.println("无此信息");
            return "0";
        }
        else{
            params.put("id",qr.getId());
            params.put("name",qr.getName());
            params.put("tel",qr.getTel());
            params.put("address",qr.getAddress());
            params.put("kinds",qr.getKinds());
            params.put("permission",qr.getPermission());
            params.put("state",qr.getState());
            System.out.println(params);
            return params;
        }
    }

    @GetMapping(path = "/register")
    public Object register(@RequestParam(required = true, value = "username", defaultValue = "") String username ,
                           @RequestParam(required = true, value = "password", defaultValue = "") String password) {
        UserDao userDao = new ManagerDaoImpl();
        userDao.register(username , password);

        System.out.println("用户信息:" + username + " " + password);
        return "success";
    }

    @GetMapping(path = "/forgetpassword")
    public Object forgetpassword(@RequestParam(required = true, value = "username", defaultValue = "") String username) {
        UserDao userDao = new ManagerDaoImpl();
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

    @GetMapping(path = "/updatepermission")
    public Object updatepermission(@RequestParam(required = true, value = "id", defaultValue = "") int id,
                                   @RequestParam(required = true, value = "permission", defaultValue = "") String permission) {
        QrDao qrDao = new QrDaoImpl();
        qrDao.updateQrpermission(id , permission);

        System.out.println("权限信息:" + id + " " + permission);
        System.out.println("权限修改结束");
        return "success";
    }
}
