package Server;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
public class Server extends JFrame implements Runnable{
	private Socket s = null;
	private ServerSocket ss = null;
	private ArrayList<ChatThread> clients = new ArrayList<ChatThread>();//����ͻ����߳�
	
	public Server() throws Exception{
		this.setTitle("��������");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,500);
		this.setVisible(true);
		ss = new ServerSocket(9999);//���������ٶ˿�
		new Thread(this).start();//���ܿͻ����ӵ���ѭ����ʼ����
	}
	
	public void run() {
		try {
			while(true){
				s = ss.accept();//s���ǵ�ǰ��Ӧ���ӵ�socket����Ӧһ���ͻ���
				//�ÿͻ���ʱ���ܷ�����Ϣ����ʱ����
				//���⿪���߳�Ϊs��������������Ϣ
				ChatThread ct = new ChatThread(s);
				clients.add(ct);
				ct.start();
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this,"��Ϸ��¼�쳣�����˳�");
			System.exit(0);
		}
	}
	
	class ChatThread extends Thread{//Ϊĳ��Socket������Ϣ
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
					String str = br.readLine();// ��ȡ�ÿͻ��˷�������Ϣ
					sendMessage(str);// ����Ϣ���͸����пͻ���
				}
			}catch(Exception ex) {//�˴����Խ���ͻ��쳣���ߵ�����
				canRun = false;
				clients.remove(this);
			}
		}
	}
	
	public void sendMessage(String msg)//������Ϣ�����еĿͻ���
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