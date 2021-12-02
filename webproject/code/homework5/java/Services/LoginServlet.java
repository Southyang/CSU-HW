package Services;

import Dao.UserDao;
import Dao.UserDaoImpl;
import User.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //账号登录实现
        System.out.println("账号登录");
        //获取登录账号信息，进行账号登录业务处理
        //获取账号
        String zhanghao = request.getParameter("zhanghao");
        //获取密码
        String mima = request.getParameter("mima");
        System.out.println(zhanghao + " " + mima);

        //获取Dao对象
        UserDao userDao = new UserDaoImpl();

        User user = userDao.getUserByUsernameAndPassword(zhanghao , mima);

        if (user == null){
            //登录失败
            System.out.println("登录失败");
            System.out.println("用户名或密码错误");
            //重定向至登录页面
            response.sendRedirect("./pages/login.jsp");
        }else {
            //登录成功
            System.out.println("登录成功");
            response.sendRedirect("http://southyang.cn");
        }
    }
}
