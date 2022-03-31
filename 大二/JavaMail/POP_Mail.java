package JavaMail;

import javax.lang.model.element.NestingKind;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

public class POP_Mail extends JFrame{
    JFrame jFrame = new JFrame("收件箱");
    private JPanel west = new JPanel();
    private JPanel center = new JPanel();//north+center

    private JButton download = new JButton();
    private JButton delete = new JButton();
    private JButton check = new JButton();
    private JButton refresh = new JButton();
    private JButton exit = new JButton();

    private JLabel tip = new JLabel();
    private JComboBox maillist = new JComboBox();

    private JLabel jlfrom = new JLabel("From:");
    private JLabel jlsubject = new JLabel("Subject:");
    private JLabel jlfile= new JLabel("File:");
    private JLabel jlbody= new JLabel();
    private JTextField jtfrom = new JTextField(40);
    private JTextField jtsubject = new JTextField(40);
    private JTextField jtfile = new JTextField(40);
    private JTextArea jtbody = new JTextArea(40,40);
    private JScrollPane jsp = new JScrollPane(jtbody);
    //邮件内容
    private Message[] messages;
    private int mailCount = 0;

    //设置邮箱会话
    private Properties props = new Properties();
    private String host = "邮箱主机";
    private String username = "邮箱账户名";
    private String password = "授权码";
    private String provider = "协议名";
    private Store store;
    private Folder folder;
    public POP_Mail(){
        getmessages();
        Container contentPane = jFrame.getContentPane();
        //定义面板布局
        contentPane.setLayout(new BorderLayout());
        //定义按钮
        download.setIcon(new javax.swing.ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\download.jpg"));
        delete.setIcon(new javax.swing.ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\delete.jpg"));
        check.setIcon(new javax.swing.ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\check.jpg"));
        refresh.setIcon(new javax.swing.ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\refresh.jpg"));
        exit.setIcon(new javax.swing.ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\exit.jpg"));
        //定义west
        west.setLayout(new GridLayout(5,1));
        west.add(download);
        west.add(delete);
        west.add(check);
        west.add(refresh);
        west.add(exit);
        contentPane.add(west,BorderLayout.WEST);
        //定义east
        Box center_north = Box.createHorizontalBox();
        tip.setText("Totally,mail number is "+mailCount+" .");
        maillist.setMaximumRowCount(5); //最多显示5个
        for(int i=0;i<mailCount;++i)
        {
            maillist.addItem("Mail-No."+(i+1));
        }
        maillist.setSelectedIndex(0); //从第0个开始显示
        center_north.add(tip);
        center_north.add(maillist);
        center.add(center_north,BorderLayout.NORTH);

        JPanel center_center = new JPanel();
        //设置标签面板和输入框面板
        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(3,1));
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(3,1));
        //设置发件人
        labels.add(jlfrom);
        jtfrom.setText("");
        jtfrom.setEditable(false);
        fields.add(jtfrom);
        //设置邮件主题
        labels.add(jlsubject);
        jtsubject.setText("");
        jtsubject.setEditable(false);
        fields.add(jtsubject);
        //设置附件
        labels.add(jlfile);
        jtfile.setText("");
        jtfile.setEditable(false);
        fields.add(jtfile);
        //设置容器存放面板
        Box center_center_north = Box.createHorizontalBox();
        center_center_north.add(labels);
        center_center_north.add(fields);
        center_center.add(center_center_north,BorderLayout.NORTH);
        //邮件消息字体设置
        jtbody.setFont(new Font("宋体",Font.BOLD,13));
        jtbody.setEditable(false);
        center_center.add(jsp,BorderLayout.CENTER);
        center.add(center_center,BorderLayout.CENTER);

        contentPane.add(center,BorderLayout.CENTER);
        jFrame.setSize(800,800);
        jFrame.setLocationRelativeTo(null); //放置在屏幕中央
        jFrame.setVisible(true);
        //jFrame.pack(); //自动调整窗口大小使其可显示

        //Button事件注册
        ButtonListenerpop btnListener = new ButtonListenerpop();
        download.addActionListener(btnListener); //添加指定的动作侦听器,以接收发自此按钮的动作事件。
        delete.addActionListener(btnListener);
        check.addActionListener(btnListener);
        refresh.addActionListener(btnListener);

        exit.addActionListener(new ActionListener() {
            //退出按钮处理
            public void actionPerformed(ActionEvent e) {
                try {
                    store.close();
                    jFrame.dispose();
                } catch (Exception Exception) {
                    Exception.printStackTrace();
                }
            }
        });
    }
    //按键监听
    class ButtonListenerpop implements ActionListener {
        @SuppressWarnings("unchecked")
        public synchronized void actionPerformed(ActionEvent ae) {
            JButton source = (JButton) ae.getSource();  //返回事件源
            if (source == download) { //下载
                try {
                    //Open floder with 'READ_WRITE' right.
                    folder = null;
                    folder = store.getFolder("inbox");
                    folder.open(Folder.READ_WRITE);
                    //Create MimeMessage object.
                    int Mail_Index = maillist.getSelectedIndex();
                    MimeMessage ThisMessage = (MimeMessage)((folder.getMessages())[Mail_Index]);
                    Object content = ThisMessage.getContent();
                    if (content instanceof MimeMultipart) {
                        MimeMultipart multipart = (MimeMultipart) content;
                        parseMultipart(multipart);
                    }
                    folder.close(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (source == delete){//删除
                try {
                    int Mail_Index = maillist.getSelectedIndex();
                    DeleteMail(Mail_Index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(source == check){ //查询
                try {
                    //Open floder with 'READ_WRITE' right.
                    folder = null;
                    folder = store.getFolder("inbox");
                    folder.open(Folder.READ_WRITE);
                    //Create MimeMessage object.
                    int Mail_Index = maillist.getSelectedIndex();
                    MimeMessage ThisMessage = (MimeMessage)((folder.getMessages())[Mail_Index]);
                    //Set sender.
                    String sender = String.valueOf((ThisMessage.getFrom())[0]);
                    jtfrom.setText(sender);
                    //Set topic.
                    jtsubject.setText(ThisMessage.getSubject());
                    //Set text.
                    if(hasAttachment(ThisMessage)) { //存在附件
                        StringBuffer textbody = new StringBuffer();
                        //Get text content.
                        GetTextBody(ThisMessage,textbody); //获取文本
                        jtbody.setText(textbody.toString()+"\n\nNOTE:This mail has ATTACHMENT.");
                        //获取文件名
                        Object content = ThisMessage.getContent();
                        MimeMultipart multipart = (MimeMultipart) content;
                        BodyPart bodyPart = multipart.getBodyPart(1);
                        String filename = bodyPart.getFileName();
                        jtfile.setText(filename);
                    }
                    else { //不存在附件，只有文本
                        StringBuffer textbody = new StringBuffer();
                        //Get text content.
                        GetTextBody(ThisMessage,textbody); //获取文本
                        jtbody.setText(textbody.toString());
                        jtfile.setText("");
                    }
                    //Close floder.
                    folder.close(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(source == refresh){ //更新收件箱
                try {
                    store.close();
                    getmessages();
                    tip.setText("Totally,mail number is "+mailCount+" .");
                    maillist.removeAllItems();
                    maillist.setMaximumRowCount(5); //最多显示5个
                    for(int i=0;i<mailCount;++i)
                    {
                        maillist.addItem("Mail-No."+(i+1));
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    //获取内容
    private StringBuffer GetTextBody(Part part,StringBuffer textbody) throws Exception { //获取文本
        boolean hasTextAttach = part.getContentType().indexOf("name")>0;
        //text:Append directly.
        if(part.isMimeType("text/*")&&!hasTextAttach)
        {
            textbody.append(part.getContent().toString());
        }
        //message:getContent().
        else if(part.isMimeType("message/rfc822"))
        {
            GetTextBody((Part)part.getContent(),textbody);
        }
        //multipart:get every part.
        else if(part.isMimeType("multipart/*"))
        {
            Multipart multipart = (Multipart)part.getContent();
            int partCount = multipart.getCount();
            for(int i=0;i<partCount;++i)
            {
                BodyPart bodypart = multipart.getBodyPart(i);
                GetTextBody(bodypart, textbody);
            }
        }

        return textbody;
    }
    //判断有无附件
    private boolean hasAttachment(Part part)throws Exception { //判断是否含有附件
        boolean has = false;
        if(part.isMimeType("multipart/*"))
        {
            MimeMultipart multipart = (MimeMultipart)part.getContent();
            int partCount = multipart.getCount();
            for(int i=0;i<partCount;++i)
            {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if(disp!=null&&
                        (disp.equalsIgnoreCase(Part.ATTACHMENT)||
                                disp.equalsIgnoreCase(Part.INLINE)))
                {
                    has = true;
                }
                else if(bodyPart.isMimeType("multipart/*"))
                {
                    has = hasAttachment(bodyPart);
                }
                else
                {
                    String contentType = bodyPart.getContentType();
                    if(contentType.indexOf("application")!=-1)
                    {
                        has = true;
                    }
                    if(contentType.indexOf("name")!=-1)
                    {
                        has = true;
                    }
                }
                if(has)
                {
                    break;
                }
            }
        }
        else if(part.isMimeType("message/rfc822"))
        {
            has = hasAttachment((Part)part.getContent());
        }
        return has;
    }
    //下载附件
    private static void SaveFile(InputStream is, String fileName) throws Exception { //下载文件
        BufferedInputStream bis = new BufferedInputStream(is);

        String filelocation = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //只打开文件夹
        int returnVal = chooser.showOpenDialog(chooser);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            filelocation = chooser.getSelectedFile().toString();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                    new File(filelocation+"\\"+fileName)));
            int index=-1;
            while((index=bis.read())!=-1)
            {
                bos.write(index);
                bos.flush();
            }
            bos.close();
            JOptionPane.showMessageDialog(null,"已下载到指定文件夹");
        }
        else{
            JOptionPane.showMessageDialog(null,"取消下载");
        }
        bis.close();
    }
    //区分邮件层级：文本，附件等
    public static void parseMultipart(Multipart multipart) throws Exception {
        int count = multipart.getCount();
        System.out.println("count =  "+count);
        for (int idx=0;idx<count;idx++) {
            BodyPart bodyPart = multipart.getBodyPart(idx);
            System.out.println(bodyPart.getContentType());
            if (bodyPart.isMimeType("text/plain")) {
                System.out.println("plain................."+bodyPart.getContent());
            } else if(bodyPart.isMimeType("text/html")) {
                System.out.println("html..................."+bodyPart.getContent());
            } else if (true) {
                String fileName = bodyPart.getFileName();
                InputStream is = bodyPart.getInputStream();
                SaveFile(is, fileName);
            }
        }
    }
    //删除邮件
    private void DeleteMail(int index) throws Exception
    {
        Folder folder = store.getFolder("inbox");
        if(folder==null)
        {
            throw new Exception("inbox"+" does not exist.");
        }
        folder.open(Folder.READ_WRITE);
        int inquire = JOptionPane.showConfirmDialog(null,
                "确认删除Mail-No."+(index+1)+" ?","Delete Mail.", JOptionPane.YES_NO_OPTION);
        if(inquire==JOptionPane.YES_OPTION)
        {
            Message DeleteMessage = (folder.getMessages())[index];
            DeleteMessage.setFlag(Flags.Flag.DELETED, true);
            JOptionPane.showMessageDialog(null, "Mail-No."+(index+1)+ "已删除");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "取消删除");
        }
        folder.close(true);
    }
    //获取store
    void getmessages(){
        try{
            //创建一个有具体连接信息的Properties对象
            props.setProperty("mail.store.protocol",provider);
            props.setProperty("mail.pop3.host",host);

            //使用Properties对象获得Session对象
            Session session = Session.getInstance(props);
            session.setDebug(true);

            //利用Session对象获得Store对象，并连接pop3服务器
            store = session.getStore("pop3");
            store.connect(host,username,password);

            //获得邮箱内邮件夹Floder对象，以“只读”打开
            folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
            //获得邮件夹Folder内的所有邮件Message对象
            messages = folder.getMessages();
            mailCount = messages.length;

            folder.close(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String win = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(win);
        new POP_Mail();
    }
}
