package Services;

import Dao.UserDao;
import Dao.UserDaoImpl;
import User.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManagerServlet", value = "/ManagerServlet")
public class ManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //管理员登录实现
        System.out.println("管理员登录");
        //获取登录账号信息，进行账号登录业务处理
        //获取账号
        String zhanghao = request.getParameter("zhanghao");
        //获取密码
        String mima = request.getParameter("mima");
        System.out.println(zhanghao + " " + mima);

        if (zhanghao.equals("8208190213") && mima.equals("123456")){ //写死，管理员只有一个
            //登录成功
            System.out.println("管理员登录成功");
            //设置用户到session中
            HttpSession session = request.getSession();
            session.setAttribute("loginUser" , zhanghao);
            //查所有用户数据
            UserDao userDao = new UserDaoImpl();
            List<User> users = userDao.selectAllUser();
            //绑定数据
            request.setAttribute("users",users);
            //转发至管理员登录页面
            request.getRequestDispatcher("./pages/userinfor.jsp").forward(request , response);
        }else {
            //登录失败
            System.out.println("管理员登录失败");
            System.out.println("用户名或密码错误");
            //重定向至管理员登录页面
            response.sendRedirect("./pages/manager.jsp");
        }
    }
}
