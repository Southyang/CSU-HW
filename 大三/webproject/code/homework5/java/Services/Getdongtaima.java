package Services;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Getdongtaima" , value = "/Getdongtaima")
public class Getdongtaima extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String youxiang = request.getParameter("youxiang");

        //邮件发送
        String msg = "";
        try {
            mailsend send = new mailsend();
            msg = send.sendmail(youxiang);
            System.out.println("邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置用户到session中
        HttpSession session = request.getSession();
        session.setAttribute("emailcode" , msg);

        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(msg);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
