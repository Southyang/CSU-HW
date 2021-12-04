package Services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.Random;

public class mailsend {
    //写邮件内容
    public String sendmail(String email) throws Exception{ //发送邮件
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.163.com"); //我用的163邮箱，可以改成其他的，把163换掉就行
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        //创建session，session是一次浏览器和服务器的交互的会话
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //通过session得到transport对象，用于发送消息
        Transport ts = session.getTransport();
        //连上邮件服务器
        ts.connect("smtp.163.com", "Your's email(不要加@和@以后的内容)", "Your email's SMTP(所用邮箱的SMTP码)");
        //创建邮件
        MimeMessage message1 = new MimeMessage(session);
        //设置邮件的基本信息
        //发件人
        message1.setFrom(new InternetAddress("Your's email"));//这里写你的邮箱，所有验证码邮件用这个发
        //收件人
        message1.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        //邮件标题
        message1.setSubject("登录验证");

        //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
        MimeBodyPart text = new MimeBodyPart();

        String vcode = getcode();

        text.setContent("【Southyang】验证码："+ vcode + "（15分钟内有效）。该验证码用于登录southyang.cn门户，请勿泄露该验证码。"
                , "text/html;charset=UTF-8");

        //创建容器描述数据关系
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);

        message1.setContent(mp);
        //发送邮件
        ts.sendMessage(message1, message1.getAllRecipients());
        ts.close();

        return vcode;
    }

    public String getcode(){ //获取随机动态码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            vcode += String.valueOf(c);
        }
        return vcode;
    }

    public static int randomInt(int from, int to) { //随机数
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    public static void main(String[] args) throws Exception {
        new mailsend();
    }
}

