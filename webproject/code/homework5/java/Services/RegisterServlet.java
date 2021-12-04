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

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
        System.out.println("开始注册");
        //注册信息获取
        String zhanghao = request.getParameter("zhanghao");
        String mima = request.getParameter("mima");
        //String remima = request.getParameter("remima");
        String youxiang = request.getParameter("youxiang");
        //判断用户信息是否可用
        UserDao userDao = new UserDaoImpl();
        User user = userDao.getUserByUsername(zhanghao);
        if(user != null){
            //注册失败提示
            System.out.println("注册失败");
            request.setAttribute("register_msg","用户名已经存在");
            request.getRequestDispatcher("./pages/register.jsp").forward(request , response);
        }else {
            //注册成功
            System.out.println("注册成功");
            //注册信息插入
            userDao.insertUser(zhanghao,mima,youxiang);
            //注册成功返回登录
            response.sendRedirect("./login.jsp");
        }
    }
}
