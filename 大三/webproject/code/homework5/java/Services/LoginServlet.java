package Services;

import Dao.UserDao;
import Dao.UserDaoImpl;
import User.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

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
        //获取图形验证码
        String yanzhengma = request.getParameter("yanzhengma");
        System.out.println(zhanghao + " " + mima);
        //获取session中存放的图形码
        String token = (String)request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        //获取Dao对象
        UserDao userDao = new UserDaoImpl();

        User user = userDao.getUserByUsernameAndPassword(zhanghao , mima);

        if(yanzhengma.equals(token)){ //图形码正确
            if (user == null){
                //登录失败
                System.out.println("登录失败");
                System.out.println("用户名或密码错误");
                //重定向至登录页面
                //response.sendRedirect("./pages/login.jsp");

                //转发前绑定数据，将数据交给下一个JSP
                request.setAttribute("login_msg","用户名或密码错误");
                //获取转发器
                RequestDispatcher rd = request.getRequestDispatcher("./login.jsp");
                //开始转发
                rd.forward(request , response);
            }else {
                //登录成功
                System.out.println("登录成功");
                response.sendRedirect("http://southyang.cn");
            }
        }else {
            System.out.println("登录失败");
            System.out.println("验证码错误");
            //转发前绑定数据，将数据交给下一个JSP
            request.setAttribute("login_msg","验证码错误");
            //获取转发器
            RequestDispatcher rd = request.getRequestDispatcher("./login.jsp");
            //开始转发
            rd.forward(request , response);
        }
    }
}
