package project_chat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
@SuppressWarnings("serial")
public class Server extends JFrame implements Runnable{
	private Socket s =null;
	private ServerSocket ss = null;
	
	//创建分层
	private JLayeredPane layeredPane = new JLayeredPane();;
	private JPanel jp = new JPanel();
	ImageIcon image = new ImageIcon("C:\\Users\\杨昊楠\\Desktop\\photo.jpg");
	private JLabel jl = new JLabel(image);
	
	//定义服务器端
	private JLabel jlinfor = new JLabel("用户信息");
	private JTextArea cname = new JTextArea(); //客户信息文本
	private JScrollPane scrollpane = new JScrollPane(); //ing的滚动框
	private JButton forcedoffline  = new JButton("强制下线");//强制下线按钮
	private JButton systeminformation = new JButton("发送系统消息");//强制下线按钮
	private JLabel infor = new JLabel("昵称 / 分组 / 状态"); //在线客户信息
	private JLabel now = new JLabel("在线用户："); //下线客户信息
	private JFrame offline = new JFrame(); //强制下线界面
	private String nickname = null; //下线客户名称
	private JFrame system = new JFrame(); //系统消息界面
	private String information = null; //系统消息内容
	
	private ArrayList clients = new ArrayList();//保存客户端的线程
	private ArrayList clientname = new ArrayList();//保存客户名称
	private static HashMap clientlist =new HashMap<String, ChatThread>(); //用户名称，用户线程
	public HashMap clientstatus =new HashMap<String, String>(); //用户名称，用户状态
	private static HashMap clientgroup =new HashMap<String, String>(); //用户名称，用户组别
	private static int flag = 0;
	//定义定时器控制刷新客户信息
	private Timer clienttimer = new Timer(1000, new timeraction());
	public Server() throws Exception{
		init();
		forcedoffline.addActionListener(new ActionListener() {
			//强制下线按钮处理
			public void actionPerformed(ActionEvent e) {
				offlineinfor();
			}
		});
		systeminformation.addActionListener(new ActionListener() {
			//系统消息按钮处理
			public void actionPerformed(ActionEvent e) {
				systeminfor();
			}
		});
		//监听8888端口
		ss = new ServerSocket(8888); //服务器端开辟端口，接受连接
		new Thread(this).start();//接受客户连接的死循环开始运行
		clienttimer.start();
	}
	
	public void init() {//初始化frame
		jp.setBounds(0, 0, 450,300);
		jp.add(jl);
		jl.setBounds(0, 0, 450,300);
		layeredPane.add(jp,JLayeredPane.DEFAULT_LAYER);
		//layeredPane.add(jb,JLayeredPane.MODAL_LAYER);
		layeredPane.add(jlinfor,JLayeredPane.MODAL_LAYER);
		layeredPane.add(infor,JLayeredPane.MODAL_LAYER);
		//设置frame基本定义
		this.setLayeredPane(layeredPane);
		this.setBounds(500,500,450,300);//设置窗口大小
		this.setTitle("服务器端");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);//设置为绝对布局
		//标签设置
		
		
		jlinfor.setBounds(0, 0, 320, 25);
		infor.setBounds(0, 30, 320, 25);
		//this.add(jlinfor);
		//this.add(infor);
		//设置客户信息文本框
		cname.setEditable(false);//设置为不可编辑
		scrollpane.setViewportView(cname); //将文本组件放在滚动面板中
		scrollpane.setBounds(0, 50, 320, 250);
		layeredPane.add(scrollpane,JLayeredPane.MODAL_LAYER);
		//this.add(scrollpane); //放在面板中
		//设置按钮
		forcedoffline.setBounds(320,0,130,150);
		systeminformation.setBounds(320,150,130,150);
		layeredPane.add(forcedoffline,JLayeredPane.MODAL_LAYER);
		layeredPane.add(systeminformation,JLayeredPane.MODAL_LAYER);
		//this.add(forcedoffline);
		//this.add(systeminformation);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void systeminfor() { //系统消息界面
		information = JOptionPane.showInputDialog("输入系统消息");
		//写入发送操作
		information = "系统消息："+information;
		sendMessage(information);
		JOptionPane.showMessageDialog(null,"系统消息已发送");
		information = "";
	}
	
	public void offlineinfor () { //强制下线界面
		nickname = JOptionPane.showInputDialog("输入要下线的客户名称");
		//强制下线
		clientstatus.replace(nickname, "Y", "N"); //更改用户状态
		sendMessageone("dead",nickname);
		JOptionPane.showMessageDialog(null,"该用户已被强制下线");
		nickname = "";
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			while(true) {
				flag = 0;
				s = ss.accept();
				//System.out.println("连接成功0");
				//s就是当前的链接对应的Socket，对应一个客户端
				//该客户端随时可能发信息过来，随时接受
				//另外开辟一个线程，专门为这个s服务，负责接受信息
				ChatThread ct = new ChatThread(s);
				clients.add(ct);
				
				BufferedReader bcname = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String str = bcname.readLine();//读取该Socket传来的名字信息
				String[] arr = str.split(" ");//空格分隔str
				
				//判断是否有线程保存、是否在线
				if(clientstatus.get(arr[1])==null) { //如果不存在该昵称
					clientlist.put(arr[1],ct); //加入昵称和线程
					clientgroup.put(arr[1], arr[0]); //加入昵称和组别
					clientstatus.put(arr[1], "Y"); //加入昵称和状态
					clientname.add(arr[1]); //保存该用户昵称
				}
				else { //有该昵称
					if(clientstatus.get(arr[1])=="Y") { //该昵称在线
						JOptionPane.showMessageDialog(null,"该用户已在线，请勿重复登录");
						flag = 1;
					}
					else { //该昵称不在线
						clientstatus.replace(arr[1], "N","Y"); //改变该昵称的状态,标记为在线
						ChatThread ctold = (ChatThread) clientlist.get(arr[1]);
						clientlist.replace(arr[1],ctold,ct);
					}
				}
				//update(); //更新客户信息
				//cname.append(arr[1]+"\t在线\n");//添加内容
				if(flag == 0)
					ct.start();
			}
		}catch(Exception ex) {}
	}
	
	public void update() {
		cname.setText("");
		for(int i = 0;i<clientname.size();i++) {
			if(clientstatus.get(clientname.get(i))=="Y") {
				//System.out.println("添加");
				cname.append(clientname.get(i)+"    "+clientgroup.get(clientname.get(i))+"\t"+"在线\n");//添加内容
			}
			else
				cname.append(clientname.get(i)+"    "+clientgroup.get(clientname.get(i))+"\t"+"离线\n");//添加内容
		}
		//System.out.println(cname.getText());
		sendMessage("列表"+clientname.size()+cname.getText()); //列表代表好友信息
	}
	
	class timeraction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			update();
		}
	}
	
	class ChatThread extends Thread{//为某个Socket负责接受信息
		@SuppressWarnings("unused")
		private Socket s = null;
		private BufferedReader br = null;
		public PrintStream ps = null;
		public ChatThread(Socket s)throws Exception{
			this.s = s;
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ps = new PrintStream(s.getOutputStream());
		}
		public void run() {
			try {
				while(true) {
					String str = br.readLine();//读取该Socket传来的信息
					String[] arr = str.split(" ");//用空格分隔字符串str，用arr[0]判断收到的消息类型
					if(arr[0].equals("dead")) {
						clientstatus.replace(arr[1], "Y", "N"); //更改用户状态
					}
					if(arr[0].equals("0")) {//广播
						sendMessage(arr[1]); //将arr[1]转发给所有客户端
					}
					else {//组播和单播
						if(arr[0].substring(0,1).equals("1")) {//组播
							sendMessagegroup(arr[1],arr[0].substring(1));
						}
						else //单播
							sendMessageone(arr[1],arr[0].substring(1));
					}
					
				}
			}catch(Exception ex) {}
		}
	}
	
	public void sendMessage(String msg) {//将信息发送给所有客户端
		for(int i = 0;i<clientname.size();i++) {
			ChatThread ct = (ChatThread) clientlist.get(clientname.get(i));
			//System.out.println(msg);
			//向ct内的Socket写msg
			ct.ps.print(msg);
		}
	}

	public void sendMessagegroup(String msg,String group) {//将信息发送给组内成员
		for(int i = 0;i<clientname.size();i++) {
			if(clientgroup.get((clientname).get(i)).equals(group)) { //是组内成员
				ChatThread ct = (ChatThread) clientlist.get(clientname.get(i));
				//System.out.println(msg);
				//向ct内的Socket写msg
				ct.ps.print(msg);
			}
		}
	}
	
	public void sendMessageone(String msg,String name) {//将信息发送给私聊对象
		ChatThread ct = (ChatThread) clientlist.get(name);
		//System.out.println(msg);
		//向ct内的Socket写msg
		ct.ps.print(msg);
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		Server server = new Server();
	}
}