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
import java.net.URLEncoder;

@WebServlet(name = "CheckUsername" , value = "/CheckUsername")
public class CheckUsername extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username = request.getParameter("username");
        UserDao userDao = new UserDaoImpl();
        User user = userDao.getUserByUsername(username);

        String msg = "";
        if(user == null){
            msg = "用户名可用";
        }else {
            msg = "用户名已存在";
        }
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(msg);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
