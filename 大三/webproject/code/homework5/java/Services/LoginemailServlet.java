package Services;

import Dao.UserDao;
import Dao.UserDaoImpl;
import User.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

@WebServlet(name = "LoginemailServlet", value = "/LoginemailServlet")
public class LoginemailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("邮箱登录");
        //获取登录邮箱信息，进行邮箱登录业务处理
        //获取账号
        String youxiang = request.getParameter("youxiang");
        //获取动态码
        String dongtaima = request.getParameter("dongtaima");
        //获取正确动态码
        String dongtaimacontent = request.getParameter("dongtaimacontent");
        //获取图形码
        String yanzhengma = request.getParameter("yanzhengma1");
        //获取session中存放的图形码
        String token = (String)request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        System.out.println(youxiang + " " + dongtaima + " " + dongtaimacontent);

        //获取Dao对象
        UserDao userDao = new UserDaoImpl();

        User user = userDao.getUserByEmail(youxiang);

        if(yanzhengma.equals(token)){ //图形码正确
            if (user == null || !dongtaima.equals(dongtaimacontent)){
                //登录失败
                System.out.println("登录失败");
                System.out.println("动态码错误");

                //转发前绑定数据，将数据交给下一个JSP
                request.setAttribute("login_msg1","动态码错误");
                //获取转发器
                RequestDispatcher rd = request.getRequestDispatcher("./login.jsp");
                //开始转发
                rd.forward(request , response);
            }else {
                //登录成功
                System.out.println("登录成功");
                response.sendRedirect("http://southyang.cn");
            }
        }else{
            //登录失败
            System.out.println("登录失败");
            System.out.println("图形码错误");

            //转发前绑定数据，将数据交给下一个JSP
            request.setAttribute("login_msg1","图形码错误");
            //获取转发器
            RequestDispatcher rd = request.getRequestDispatcher("./login.jsp");
            //开始转发
            rd.forward(request , response);
        }
    }
}
