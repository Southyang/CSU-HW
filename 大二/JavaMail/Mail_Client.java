package JavaMail;

import JavaMail.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mail_Client {
    private JFrame client = new JFrame("");

    private JButton receive = new JButton("收件箱");
    private JLabel information = new JLabel();
    private JButton send = new JButton("写邮件");
    private JButton exit = new JButton("退出");
    public Mail_Client(){
        //client.setLayout(new BorderLayout(5,5));
        //布局方式
        client.setLayout(new GridLayout(4,1));
        information.setIcon(new javax.swing.ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\information.png"));
        client.add(information);
        client.add(receive);
        client.add(send);
        client.add(exit);

        client.setVisible(true);
        client.setResizable(false);
        client.setBounds(100,200,150,300);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Button事件注册
        ButtonListenerclient btnListener = new ButtonListenerclient();
        receive.addActionListener(btnListener); //添加指定的动作侦听器,以接收发自此按钮的动作事件。
        send.addActionListener(btnListener);
        exit.addActionListener(btnListener);
    }

    class ButtonListenerclient implements ActionListener {
        @SuppressWarnings("unchecked")
        public synchronized void actionPerformed(ActionEvent ae) {
            JButton source = (JButton) ae.getSource();  //返回事件源
            if(source == receive){ //收件箱
                new POP_Mail();
            }
            else if (source == send){ //发邮件
                new SMTP_Mail();
            }
            else if (source == exit){ //退出
                System.exit(1);
            }
        }
    }

    public static void main (String[] args)throws Exception{
        //使用Windows风格
        String win = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(win);
        new Mail_Client();
    }
}
