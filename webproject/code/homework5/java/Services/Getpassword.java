package Services;

import Dao.UserDao;
import Dao.UserDaoImpl;
import User.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Getpassword" , value = "/Getpassword")
public class Getpassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username = request.getParameter("username");
        //获取动态码
        String dongtaima = request.getParameter("dongtaima");
        System.out.println("用户信息为:" + username + " " + dongtaima);
        //获取正确验证码
        String ecode = (String)request.getSession().getAttribute("emailcode");
        System.out.println("验证码为:" + ecode);
        //查找用户
        UserDao userDao = new UserDaoImpl();
        User user = userDao.getUserByUsername(username);
        System.out.println("查询用户:" + username);
        String msg = "";
        if(user == null || !dongtaima.equals(ecode)){
            msg = "无此用户或验证码错误";
        }else {
            msg = user.getPassword();
        }

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(msg);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
