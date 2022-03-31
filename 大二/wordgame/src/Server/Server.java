package Server;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
public class Server extends JFrame implements Runnable{
	private Socket s = null;
	private ServerSocket ss = null;
	private ArrayList<ChatThread> clients = new ArrayList<ChatThread>();//保存客户端线程
	
	public Server() throws Exception{
		this.setTitle("服务器端");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,500);
		this.setVisible(true);
		ss = new ServerSocket(9999);//服务器开辟端口
		new Thread(this).start();//接受客户连接的死循环开始运行
	}
	
	public void run() {
		try {
			while(true){
				s = ss.accept();//s就是当前对应连接的socket，对应一个客户端
				//该客户随时可能发送信息，随时接受
				//另外开辟线程为s服务，用来接收信息
				ChatThread ct = new ChatThread(s);
				clients.add(ct);
				ct.start();
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this,"游戏登录异常，已退出");
			System.exit(0);
		}
	}
	
	class ChatThread extends Thread{//为某个Socket接受信息
		private Socket s = null;
		private BufferedReader br = null;
		public PrintStream ps = null;
		private boolean canRun = true;
		public ChatThread(Socket s) throws Exception{
			this.s = s;
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ps = new PrintStream(s.getOutputStream());
		}
		public void run() {
			try {
				while(canRun) {
					String str = br.readLine();// 读取该客户端发来的信息
					sendMessage(str);// 将信息发送给所有客户端
				}
			}catch(Exception ex) {//此处可以解决客户异常下线的问题
				canRun = false;
				clients.remove(this);
			}
		}
	}
	
	public void sendMessage(String msg)//发送信息给所有的客户端
	{
		for(ChatThread ct:clients)
		{
			ct.ps.println(msg);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Server server = new Server();
	}

}