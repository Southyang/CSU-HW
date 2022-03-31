package project_chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

@SuppressWarnings("serial")

public class Client extends JFrame implements ActionListener,Runnable{
	//定义面板
	private JPanel way = new JPanel();
	  //定义发送方式
	private JLabel infor = new JLabel("发送方式");
	private JComboBox sendway = new JComboBox();
	private JButton send = new JButton("发送"); //按钮send
	private String temp = null; //存放下拉框内容
	  //定义退出按钮
	private JButton exit = new JButton("关闭");
	//定义登录框
	private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,10,10);
	private JFrame Loginframe = new JFrame();
	private JLabel lblAcc = new JLabel("输入账号");
	private JTextField tfAcc = new JTextField(10);
	private JLabel lblNum = new JLabel("输入组别");
	private JTextField tfNum = new JTextField(10);
	private JLabel lblPass = new JLabel("输入密码");
	private JPasswordField pfPass = new JPasswordField(10);
	private JLabel lblimfor = new JLabel("提示：组别格式为1/11");
	private String number = null; //两位，代表组号
	private String nickname = null; //客户账号
	private String password = null; //输入的密码
	private String flag = "123456"; //聊天室密码
	  //定义登录界面按钮
	private JButton btLogin = new JButton("登录");
	private JButton btExit = new JButton("取消");
	private JPanel jpl = new JPanel();
	//定义聊天及好友框
	private JLabel friendinfor = new JLabel("昵称 / 分组 / 状态"); //好友信息
	private JTextArea taMsg = new JTextArea("");
	private JTextArea friendlist = new JTextArea("");
	private JTextField tfMsg = new JTextField();
	private Socket s = null;
	private JScrollPane scrollpaneta = new JScrollPane(); //滚动窗口ta
	private JScrollPane scrollpanefri = new JScrollPane(); //滚动窗口fri
	@SuppressWarnings("unused")
	private JLabel chatimformation = new JLabel("以下是聊天记录"); //聊天信息
	@SuppressWarnings("unused")
	private JLabel friedimformation = new JLabel("在线好友"); //好友信息
	
	public Client() {
		initlogin();	
	}
	
	public void initlogin() { //初始化登录界面
		jpl.setLayout(flowLayout);//设置布局方式
		jpl.add(lblAcc);
		jpl.add(tfAcc);
		jpl.add(lblNum);
		jpl.add(tfNum);
		jpl.add(lblPass);
		jpl.add(pfPass);
		jpl.add(btLogin);
		jpl.add(btExit);
		jpl.add(lblimfor);
		Loginframe.add(jpl);
		Loginframe.setSize(200,200);
		Loginframe.setResizable(false);
		Loginframe.setVisible(true);
		Loginframe.setLocationRelativeTo(null); //放置在屏幕中央
		Loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btLogin.addActionListener(new ActionListener() {
			//登录按钮处理
			public void actionPerformed(ActionEvent e) {
				password = pfPass.getText();
				initchat();
				/*if(password.equals(flag)) {
					initchat();
				}
				else {
					JOptionPane.showMessageDialog(null,"密码有误,请重新登录");
					initlogin();
				}*/
			}
		});
		btExit.addActionListener(new ActionListener() {
			//取消按钮处理
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"已退出登录");
				System.exit(0);
			}
		});
	}
	
	public void initchat() { //初始化用户界面
		//关闭登录界面
		Loginframe.dispose();		
		//frame基本定义设置
		this.setBounds(100,100,450,600);//设置窗口大小
		this.setTitle("客户端");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(null);//设置为绝对布局
		//设置标签
		chatimformation.setBounds(0,0,300,50);
		//chatimformation.setForeground(Color.BLUE);
		this.add(chatimformation);
		friedimformation.setBounds(300,0,150,25);
		friendinfor.setBounds(300,25,150,35);
		//friedimformation.setForeground(Color.BLUE);
		this.add(friedimformation);
		this.add(friendinfor);
		//设置聊天内容显示框
		taMsg.setEditable(false);//设置为不可编辑
		scrollpaneta.setViewportView(taMsg); //将文本组件放在滚动面板中
		scrollpaneta.setBounds(0, 50, 300, 450);
		this.add(scrollpaneta);
		//设置在线好友显示框
		friendlist.setEditable(false);//设置为不可编辑
		scrollpanefri.setViewportView(friendlist); //将文本组件放在滚动面板中
		scrollpanefri.setBounds(300, 50, 150, 450);
		scrollpanefri.setViewportView(friendlist); //将文本组件放在滚动面板中
		this.add(scrollpanefri);
	    //设置输入框
		tfMsg.setBackground(Color.yellow);
		tfMsg.setBounds(0,500,300,100);
		this.add(tfMsg,BorderLayout.SOUTH);
		//设置发送方式下拉框
		sendway.addItem("广播");
		sendway.addItem("组播");
		sendway.addItem("单播");
		way.add(infor);
		way.add(sendway);
		//设置按钮
		way.add(send);
		way.add(exit);
		//设置操作面板
		way.setBounds(300,500,150,100);
		this.add(way);
		//设置事件监听
		tfMsg.addActionListener(this);
		send.addActionListener(this);
		exit.addActionListener(new ActionListener() {
			//关闭按钮处理
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"您已退出");
				try {
					OutputStream os = s.getOutputStream();
					PrintStream ps = new PrintStream(os);
					ps.println("dead"+" "+nickname);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		//frame框属性设置
		this.setResizable(false);
		this.setVisible(true); //设置为可见
		this.setLocationRelativeTo(null); //放置在屏幕中央
		
		link();
	}
	
	public void link() {		
		number = tfNum.getText();
		nickname = tfAcc.getText();
		try {
			s = new Socket("127.0.0.1",8888);
			OutputStream os = s.getOutputStream();//向服务器传入组别和昵称
			PrintStream ps = new PrintStream(os);
			ps.println(number+" "+nickname);
			
			JOptionPane.showMessageDialog(this,"连接成功");
			this.setTitle("客户端："+nickname);
			new Thread(this).start();
		}catch(Exception ex) {}
	}
	
	public void run() {
		try {
			while(true) {
				InputStream is = s.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String str = br.readLine();//读
				
				if((str.substring(0,4)).equals("dead")) {
					JOptionPane.showMessageDialog(this,"您已被强制下线");
					System.exit(0);
				}
				if(str.substring(0, 2).equals("列表")) {
					friendlist.setText("");
					int sum = Integer.parseInt(str.substring(2,3))-1;
					str = str.substring(3);
					friendlist.append(str+"\n");
					while(sum>0) {
						str = br.readLine();
						friendlist.append(str+"\n");
						sum--;
					}
				}
				else{
					str = str.substring(0,str.indexOf("列表"));
					taMsg.append(str+"\n");//添加内容
				}
			}
		}catch(Exception ex) {}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			temp = sendway.getSelectedItem().toString();
			OutputStream os = s.getOutputStream();
			PrintStream ps = new PrintStream(os);
			if(temp.equals("广播")) {//0代表广播
				ps.println("0"+" "+nickname+":"+tfMsg.getText());
			}
			else if(temp.equals("组播")) {//1代表组播
				String recievenum = JOptionPane.showInputDialog("请输入组播发送的组别");
				ps.println("1"+recievenum+" "+nickname+"在第"+recievenum+"组内说:"+tfMsg.getText());
				//taMsg.append(nickname+"在第"+recievenum+"组内说:"+tfMsg.getText()+"\n");//添加内容
			}
			else {//2代表单播
				String recievename = JOptionPane.showInputDialog("请输入私聊发送的客户名称");
				ps.println("2"+recievename+" "+nickname+"单独对您说："+tfMsg.getText());
				taMsg.append(nickname+"单独对"+recievename+"说:"+tfMsg.getText()+"\n");//添加内容
			}
			tfMsg.setText("");
		}catch(Exception ex) {}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Client client = new Client();
	}
}