package JavaMail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledEditorKit;
import java.awt.event.*;
import java.awt.*;

public class SMTP_Mail extends JFrame{
    private JButton sendButton = new JButton("Send Message");
    private JLabel fromLabel = new JLabel("From:");
    private JLabel toLabel = new JLabel("To:");
    private JLabel hostLabel = new JLabel("SMTP Server");
    private JLabel subjectLabel = new JLabel("Subject:");
    private JTextField fromField = new JTextField(40);
    private JTextField toField = new JTextField(40);
    private JTextField hostField = new JTextField(40);
    private JTextField subjectField = new JTextField(40);
    private JLabel fileLabel = new JLabel("Filename:");
    private JButton filebutton = new JButton("选择");
    private JTextArea message = new JTextArea(40,40);
    private JScrollPane jsp = new JScrollPane(message);

    private String filename = "";

    public SMTP_Mail(){
        super();
        this.setTitle("发件箱");
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        //设置标签面板和输入框面板
        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(5,1));
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(5,1));
        //设置主机地址
        labels.add(hostLabel);
        String host = "smtp.163.com";
        hostField.setText(host);
        fields.add(hostField);
        //设置收件人
        labels.add(toLabel);
        fields.add(toField);
        //设置发件人
        String from = "邮箱账号";
        fromField.setText(from);
        labels.add(fromLabel);
        fields.add(fromField);
        //设置邮件主题
        labels.add(subjectLabel);
        fields.add(subjectField);
        //设置附件填写地址
        labels.add(fileLabel);
        fields.add(filebutton);
        //设置容器存放面板
        Box north = Box.createHorizontalBox();
        north.add(labels);
        north.add(fields);
        contentPane.add(north,BorderLayout.NORTH);

        //邮件消息字体设置
        message.setFont(new Font("宋体",Font.BOLD,13));
        contentPane.add(jsp,BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new FlowLayout(FlowLayout.CENTER));
        south.add(sendButton);
        sendButton.addActionListener(new SendAction());
        filebutton.addActionListener(new SendAction());
        contentPane.add(south,BorderLayout.SOUTH);
        this.setSize(400,500);
        this.setLocationRelativeTo(null); //放置在屏幕中央
        this.setVisible(true);
        //this.pack(); //自动调整窗口大小使其可显示
    }
    //按键监听类
    class SendAction implements ActionListener{
        public synchronized void actionPerformed(ActionEvent evt){
            JButton source = (JButton) evt.getSource();  //返回事件源
            if(source==sendButton){
                try {
                    Properties prop = new Properties();
                    prop.setProperty("mail.host", hostField.getText());
                    prop.setProperty("mail.transport.protocol", "smtp");
                    prop.setProperty("mail.smtp.auth", "true");
                    //创建session，session是一次浏览器和服务器的交互的会话
                    Session session = Session.getInstance(prop);
                    //开启Session的debug模式，可以查看到程序发送Email的运行状态
                    session.setDebug(true);
                    //通过session得到transport对象，用于发送消息
                    Transport ts = session.getTransport();
                    //连上邮件服务器
                    ts.connect(hostField.getText(), "邮箱账户名", "授权码");
                    //创建邮件
                    Message message1 = createAttachMail(session);
                    //发送邮件
                    ts.sendMessage(message1, message1.getAllRecipients());
                    ts.close();
                    JOptionPane.showMessageDialog(null,"邮件已发送");
                    //清空输入栏
                    toField.setText("");
                    subjectField.setText("");
                    message.setText("");
                    filename = "";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if (source==filebutton){
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(chooser);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    filename = chooser.getSelectedFile().toString();//clooser.getSelectedFile().getName()为点击的文件名和路径
                }
            }
        }
        //写邮件内容
        public MimeMessage createAttachMail(Session session) throws Exception{
            MimeMessage message1 = new MimeMessage(session);
            //设置邮件的基本信息
            //发件人
            message1.setFrom(new InternetAddress(fromField.getText()));
            //收件人
            message1.setRecipient(Message.RecipientType.TO, new InternetAddress(toField.getText()));
            //邮件标题
            message1.setSubject(subjectField.getText());

            //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(message.getText(), "text/html;charset=UTF-8");

            //创建容器描述数据关系
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(text);

            if(!filename.equals("")){
                //创建一个包含附件内容的MimeBodyPart
                MimeBodyPart attach = new MimeBodyPart();
                //dh用于上传文件
                DataHandler dh = new DataHandler(new FileDataSource(filename));
                attach.setDataHandler(dh);
                attach.setFileName(dh.getName());

                mp.addBodyPart(attach);
                mp.setSubType("mixed");

            }
            message1.setContent(mp);
            //返回生成的邮件
            return message1;
        }
    }

    public static void main(String[] args) throws Exception {
        String win = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(win);
        new SMTP_Mail();
    }
}
