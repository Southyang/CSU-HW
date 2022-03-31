package JavaMail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class Mail_Login {
    //设置登录界面
    private JFrame Mail_Login = new JFrame("想变成网易的山寨mail");
    //设置登录窗口
    private JPanel Logininfor = new JPanel();
    private JPanel LogininforS = new JPanel();
    private JPanel LogininforT = new JPanel();
     //设置标签
    private JLabel Login = new JLabel("       邮箱登录");
     //设置账号密码栏
    private JLabel lblAcc = new JLabel("      账号");
    private JTextField tfAcc = new JTextField(10);
    private JLabel lblPass = new JLabel("      密码");
    private JPasswordField pfPass = new JPasswordField(10);
     //设置按钮
    private JButton btLogin = new JButton("登录");
    private JButton btExit = new JButton("取消");
    private JButton logininfor = new JButton("注册账号");
    private JButton passinfor = new JButton("忘记密码？");
    //设置图层
    private JLayeredPane layeredPane = new JLayeredPane();;
    private JPanel jp = new JPanel();
    ImageIcon image = new ImageIcon("F:\\语言类文档\\java\\shiyan_mail\\src\\main\\java\\picture.jpg");
    private JLabel jl = new JLabel(image);
    //设置布局
    private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,10,10);

    private static HashMap clientname =new HashMap<String, String>(); //用户名称，用户密码
    private static HashMap clientkey =new HashMap<String, String>(); //用户名称，用户口令

    public Mail_Login(){
        //保存账号信息
        setclient();
        //设置登录窗口2
        LogininforS.setLayout(new GridLayout(1,1,5,5));
        //Login.setSize(350,50);
        Login.setFont(new Font("楷体", Font.BOLD, 30));
        //Login.setHorizontalTextPosition(JLabel.CENTER);
        LogininforS.add(Login,JLabel.CENTER); //文字水平对齐方式居中
        LogininforS.setBackground(Color.green);

        //设置登录窗口3
        LogininforT.setLayout(new GridLayout(4,2,10,10));
        lblAcc.setFont(new Font("楷体", Font.BOLD, 20));
        lblPass.setFont(new Font("楷体", Font.BOLD, 20));
        LogininforT.add(lblAcc);
        LogininforT.add(tfAcc);
        LogininforT.add(lblPass);
        LogininforT.add(pfPass);
        LogininforT.add(logininfor);
        LogininforT.add(passinfor);
        LogininforT.add(btLogin);
        LogininforT.add(btExit);
        LogininforT.setBackground(Color.yellow);

        //设置登录窗口1的布局方式
        Logininfor.setLayout(new GridLayout(2,1,10,10)); //空布局
        Logininfor.add(LogininforS);
        Logininfor.add(LogininforT);
        Logininfor.setBounds(400,50,350,500);

        //设置图层
        jp.setBounds(0, 0, 800,600);
        jp.add(jl);
        jl.setBounds(0, 0, 800,600);
        layeredPane.add(jp,JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(Logininfor,JLayeredPane.MODAL_LAYER);

        //设置登录界面基本定义
        Mail_Login.setLayeredPane(layeredPane);
        Mail_Login.setSize(800,600);
        Mail_Login.setVisible(true);
        Mail_Login.setResizable(false);
        Mail_Login.setLocationRelativeTo(null); //居中
        Mail_Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Button事件注册
        ButtonListener btnListener = new ButtonListener();
        btLogin.addActionListener(btnListener); //添加指定的动作侦听器,以接收发自此按钮的动作事件。
        btExit.addActionListener(btnListener);
        logininfor.addActionListener(btnListener);
        passinfor.addActionListener(btnListener);
    }

    /***按钮监听***/
    class ButtonListener implements ActionListener {
        @SuppressWarnings("unchecked")
        public synchronized void actionPerformed(ActionEvent ae) {
            JButton source = (JButton) ae.getSource();  //返回事件源
            if (source == btLogin){//登录
                String name = tfAcc.getText();
                String passw = new String(pfPass.getPassword());
                if(clientname.get(name)==null||!clientname.get(name).equals(passw)){ //不存在该用户或者密码错误
                    System.out.println(passw);
                    System.out.println(clientname.get(name));
                    JOptionPane.showMessageDialog(null,"用户名或密码错误，请重新输入");
                    pfPass.setText("");
                }
                else{
                    Mail_Login.dispose(); //关闭登录界面
                    new Mail_Client();
                }
            }
            else if(source == btExit){ //退出
                JOptionPane.showMessageDialog(null,"已退出登录");
                System.exit(0);
            }
            else if (source == logininfor){ //注册
                String tempn = JOptionPane.showInputDialog("邮箱账号");
                String tempp = JOptionPane.showInputDialog("邮箱密码");
                String tempk = JOptionPane.showInputDialog("服务器口令");
                clientname.put(tempn,tempp);
                clientkey.put(tempn,tempk);
                JOptionPane.showMessageDialog(null,"注册完毕，请重新登录");
            }
            else if (source == passinfor){
                String tempn = JOptionPane.showInputDialog("您的邮箱账号是：");
                String tempp = (String)clientname.get(tempn);
                if(tempp!=null)
                    JOptionPane.showMessageDialog(null,"您的密码是"+tempp);
                else
                    JOptionPane.showMessageDialog(null,"无此账号信息");
            }
        }
    }
    //初始化用户列表
    public static void setclient(){
        clientname.put("邮箱账号","登录密码);
        clientkey.put("邮箱账号","POP3/SMTP授权码");
    }

    public static void main(String[] args) throws Exception{
        //使用Windows风格
        String win = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(win);
        new Mail_Login();
    }
}
