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

@WebServlet(name = "Modifypassword" , value = "/Modifypassword")
public class Modifypassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String newpassword = request.getParameter("newpassword");
        //查找用户
        UserDao userDao = new UserDaoImpl();
        userDao.updatepassword(username , password , newpassword);

        System.out.println("查询用户:" + username);
        String msg = "";
        /*if(user == null){
            msg = "无此用户";
        }else {
            msg = user.getPassword();
        }*/
        msg = "修改成功";
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(msg);
        System.out.println("修改密码为:" + newpassword);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
