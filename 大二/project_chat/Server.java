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
	
	//�����ֲ�
	private JLayeredPane layeredPane = new JLayeredPane();;
	private JPanel jp = new JPanel();
	ImageIcon image = new ImageIcon("C:\\Users\\����\\Desktop\\photo.jpg");
	private JLabel jl = new JLabel(image);
	
	//�����������
	private JLabel jlinfor = new JLabel("�û���Ϣ");
	private JTextArea cname = new JTextArea(); //�ͻ���Ϣ�ı�
	private JScrollPane scrollpane = new JScrollPane(); //ing�Ĺ�����
	private JButton forcedoffline  = new JButton("ǿ������");//ǿ�����߰�ť
	private JButton systeminformation = new JButton("����ϵͳ��Ϣ");//ǿ�����߰�ť
	private JLabel infor = new JLabel("�ǳ� / ���� / ״̬"); //���߿ͻ���Ϣ
	private JLabel now = new JLabel("�����û���"); //���߿ͻ���Ϣ
	private JFrame offline = new JFrame(); //ǿ�����߽���
	private String nickname = null; //���߿ͻ�����
	private JFrame system = new JFrame(); //ϵͳ��Ϣ����
	private String information = null; //ϵͳ��Ϣ����
	
	private ArrayList clients = new ArrayList();//����ͻ��˵��߳�
	private ArrayList clientname = new ArrayList();//����ͻ�����
	private static HashMap clientlist =new HashMap<String, ChatThread>(); //�û����ƣ��û��߳�
	public HashMap clientstatus =new HashMap<String, String>(); //�û����ƣ��û�״̬
	private static HashMap clientgroup =new HashMap<String, String>(); //�û����ƣ��û����
	private static int flag = 0;
	//���嶨ʱ������ˢ�¿ͻ���Ϣ
	private Timer clienttimer = new Timer(1000, new timeraction());
	public Server() throws Exception{
		init();
		forcedoffline.addActionListener(new ActionListener() {
			//ǿ�����߰�ť����
			public void actionPerformed(ActionEvent e) {
				offlineinfor();
			}
		});
		systeminformation.addActionListener(new ActionListener() {
			//ϵͳ��Ϣ��ť����
			public void actionPerformed(ActionEvent e) {
				systeminfor();
			}
		});
		//����8888�˿�
		ss = new ServerSocket(8888); //�������˿��ٶ˿ڣ���������
		new Thread(this).start();//���ܿͻ����ӵ���ѭ����ʼ����
		clienttimer.start();
	}
	
	public void init() {//��ʼ��frame
		jp.setBounds(0, 0, 450,300);
		jp.add(jl);
		jl.setBounds(0, 0, 450,300);
		layeredPane.add(jp,JLayeredPane.DEFAULT_LAYER);
		//layeredPane.add(jb,JLayeredPane.MODAL_LAYER);
		layeredPane.add(jlinfor,JLayeredPane.MODAL_LAYER);
		layeredPane.add(infor,JLayeredPane.MODAL_LAYER);
		//����frame��������
		this.setLayeredPane(layeredPane);
		this.setBounds(500,500,450,300);//���ô��ڴ�С
		this.setTitle("��������");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);//����Ϊ���Բ���
		//��ǩ����
		
		
		jlinfor.setBounds(0, 0, 320, 25);
		infor.setBounds(0, 30, 320, 25);
		//this.add(jlinfor);
		//this.add(infor);
		//���ÿͻ���Ϣ�ı���
		cname.setEditable(false);//����Ϊ���ɱ༭
		scrollpane.setViewportView(cname); //���ı�������ڹ��������
		scrollpane.setBounds(0, 50, 320, 250);
		layeredPane.add(scrollpane,JLayeredPane.MODAL_LAYER);
		//this.add(scrollpane); //���������
		//���ð�ť
		forcedoffline.setBounds(320,0,130,150);
		systeminformation.setBounds(320,150,130,150);
		layeredPane.add(forcedoffline,JLayeredPane.MODAL_LAYER);
		layeredPane.add(systeminformation,JLayeredPane.MODAL_LAYER);
		//this.add(forcedoffline);
		//this.add(systeminformation);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void systeminfor() { //ϵͳ��Ϣ����
		information = JOptionPane.showInputDialog("����ϵͳ��Ϣ");
		//д�뷢�Ͳ���
		information = "ϵͳ��Ϣ��"+information;
		sendMessage(information);
		JOptionPane.showMessageDialog(null,"ϵͳ��Ϣ�ѷ���");
		information = "";
	}
	
	public void offlineinfor () { //ǿ�����߽���
		nickname = JOptionPane.showInputDialog("����Ҫ���ߵĿͻ�����");
		//ǿ������
		clientstatus.replace(nickname, "Y", "N"); //�����û�״̬
		sendMessageone("dead",nickname);
		JOptionPane.showMessageDialog(null,"���û��ѱ�ǿ������");
		nickname = "";
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			while(true) {
				flag = 0;
				s = ss.accept();
				//System.out.println("���ӳɹ�0");
				//s���ǵ�ǰ�����Ӷ�Ӧ��Socket����Ӧһ���ͻ���
				//�ÿͻ�����ʱ���ܷ���Ϣ��������ʱ����
				//���⿪��һ���̣߳�ר��Ϊ���s���񣬸��������Ϣ
				ChatThread ct = new ChatThread(s);
				clients.add(ct);
				
				BufferedReader bcname = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String str = bcname.readLine();//��ȡ��Socket������������Ϣ
				String[] arr = str.split(" ");//�ո�ָ�str
				
				//�ж��Ƿ����̱߳��桢�Ƿ�����
				if(clientstatus.get(arr[1])==null) { //��������ڸ��ǳ�
					clientlist.put(arr[1],ct); //�����ǳƺ��߳�
					clientgroup.put(arr[1], arr[0]); //�����ǳƺ����
					clientstatus.put(arr[1], "Y"); //�����ǳƺ�״̬
					clientname.add(arr[1]); //������û��ǳ�
				}
				else { //�и��ǳ�
					if(clientstatus.get(arr[1])=="Y") { //���ǳ�����
						JOptionPane.showMessageDialog(null,"���û������ߣ������ظ���¼");
						flag = 1;
					}
					else { //���ǳƲ�����
						clientstatus.replace(arr[1], "N","Y"); //�ı���ǳƵ�״̬,���Ϊ����
						ChatThread ctold = (ChatThread) clientlist.get(arr[1]);
						clientlist.replace(arr[1],ctold,ct);
					}
				}
				//update(); //���¿ͻ���Ϣ
				//cname.append(arr[1]+"\t����\n");//�������
				if(flag == 0)
					ct.start();
			}
		}catch(Exception ex) {}
	}
	
	public void update() {
		cname.setText("");
		for(int i = 0;i<clientname.size();i++) {
			if(clientstatus.get(clientname.get(i))=="Y") {
				//System.out.println("���");
				cname.append(clientname.get(i)+"    "+clientgroup.get(clientname.get(i))+"\t"+"����\n");//�������
			}
			else
				cname.append(clientname.get(i)+"    "+clientgroup.get(clientname.get(i))+"\t"+"����\n");//�������
		}
		//System.out.println(cname.getText());
		sendMessage("�б�"+clientname.size()+cname.getText()); //�б���������Ϣ
	}
	
	class timeraction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			update();
		}
	}
	
	class ChatThread extends Thread{//Ϊĳ��Socket���������Ϣ
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
					String str = br.readLine();//��ȡ��Socket��������Ϣ
					String[] arr = str.split(" ");//�ÿո�ָ��ַ���str����arr[0]�ж��յ�����Ϣ����
					if(arr[0].equals("dead")) {
						clientstatus.replace(arr[1], "Y", "N"); //�����û�״̬
					}
					if(arr[0].equals("0")) {//�㲥
						sendMessage(arr[1]); //��arr[1]ת�������пͻ���
					}
					else {//�鲥�͵���
						if(arr[0].substring(0,1).equals("1")) {//�鲥
							sendMessagegroup(arr[1],arr[0].substring(1));
						}
						else //����
							sendMessageone(arr[1],arr[0].substring(1));
					}
					
				}
			}catch(Exception ex) {}
		}
	}
	
	public void sendMessage(String msg) {//����Ϣ���͸����пͻ���
		for(int i = 0;i<clientname.size();i++) {
			ChatThread ct = (ChatThread) clientlist.get(clientname.get(i));
			//System.out.println(msg);
			//��ct�ڵ�Socketдmsg
			ct.ps.print(msg);
		}
	}

	public void sendMessagegroup(String msg,String group) {//����Ϣ���͸����ڳ�Ա
		for(int i = 0;i<clientname.size();i++) {
			if(clientgroup.get((clientname).get(i)).equals(group)) { //�����ڳ�Ա
				ChatThread ct = (ChatThread) clientlist.get(clientname.get(i));
				//System.out.println(msg);
				//��ct�ڵ�Socketдmsg
				ct.ps.print(msg);
			}
		}
	}
	
	public void sendMessageone(String msg,String name) {//����Ϣ���͸�˽�Ķ���
		ChatThread ct = (ChatThread) clientlist.get(name);
		//System.out.println(msg);
		//��ct�ڵ�Socketдmsg
		ct.ps.print(msg);
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		Server server = new Server();
	}
}