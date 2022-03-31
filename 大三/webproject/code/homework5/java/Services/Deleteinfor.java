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

@WebServlet(name = "Deleteinfor" , value = "/Deleteinfor")
public class Deleteinfor extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String youxiang = request.getParameter("youxiang");
        //查找用户
        UserDao userDao = new UserDaoImpl();

        String msg = "";

        User user = userDao.getUserByUsernameAndPassword(username , password);
        if(user == null){ //不存在
            msg = "无此用户";
            System.out.println(msg);
        }else { //存在
            userDao.deleteUser(username , password , youxiang);
            System.out.println("注销用户:" + username);
        }
        msg = "注销成功";
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(msg);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
