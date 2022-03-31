package project_chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

@SuppressWarnings("serial")

public class Client extends JFrame implements ActionListener,Runnable{
	//�������
	private JPanel way = new JPanel();
	  //���巢�ͷ�ʽ
	private JLabel infor = new JLabel("���ͷ�ʽ");
	private JComboBox sendway = new JComboBox();
	private JButton send = new JButton("����"); //��ťsend
	private String temp = null; //�������������
	  //�����˳���ť
	private JButton exit = new JButton("�ر�");
	//�����¼��
	private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,10,10);
	private JFrame Loginframe = new JFrame();
	private JLabel lblAcc = new JLabel("�����˺�");
	private JTextField tfAcc = new JTextField(10);
	private JLabel lblNum = new JLabel("�������");
	private JTextField tfNum = new JTextField(10);
	private JLabel lblPass = new JLabel("��������");
	private JPasswordField pfPass = new JPasswordField(10);
	private JLabel lblimfor = new JLabel("��ʾ������ʽΪ1/11");
	private String number = null; //��λ���������
	private String nickname = null; //�ͻ��˺�
	private String password = null; //���������
	private String flag = "123456"; //����������
	  //�����¼���水ť
	private JButton btLogin = new JButton("��¼");
	private JButton btExit = new JButton("ȡ��");
	private JPanel jpl = new JPanel();
	//�������켰���ѿ�
	private JLabel friendinfor = new JLabel("�ǳ� / ���� / ״̬"); //������Ϣ
	private JTextArea taMsg = new JTextArea("");
	private JTextArea friendlist = new JTextArea("");
	private JTextField tfMsg = new JTextField();
	private Socket s = null;
	private JScrollPane scrollpaneta = new JScrollPane(); //��������ta
	private JScrollPane scrollpanefri = new JScrollPane(); //��������fri
	@SuppressWarnings("unused")
	private JLabel chatimformation = new JLabel("�����������¼"); //������Ϣ
	@SuppressWarnings("unused")
	private JLabel friedimformation = new JLabel("���ߺ���"); //������Ϣ
	
	public Client() {
		initlogin();	
	}
	
	public void initlogin() { //��ʼ����¼����
		jpl.setLayout(flowLayout);//���ò��ַ�ʽ
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
		Loginframe.setLocationRelativeTo(null); //��������Ļ����
		Loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btLogin.addActionListener(new ActionListener() {
			//��¼��ť����
			public void actionPerformed(ActionEvent e) {
				password = pfPass.getText();
				initchat();
				/*if(password.equals(flag)) {
					initchat();
				}
				else {
					JOptionPane.showMessageDialog(null,"��������,�����µ�¼");
					initlogin();
				}*/
			}
		});
		btExit.addActionListener(new ActionListener() {
			//ȡ����ť����
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"���˳���¼");
				System.exit(0);
			}
		});
	}
	
	public void initchat() { //��ʼ���û�����
		//�رյ�¼����
		Loginframe.dispose();		
		//frame������������
		this.setBounds(100,100,450,600);//���ô��ڴ�С
		this.setTitle("�ͻ���");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(null);//����Ϊ���Բ���
		//���ñ�ǩ
		chatimformation.setBounds(0,0,300,50);
		//chatimformation.setForeground(Color.BLUE);
		this.add(chatimformation);
		friedimformation.setBounds(300,0,150,25);
		friendinfor.setBounds(300,25,150,35);
		//friedimformation.setForeground(Color.BLUE);
		this.add(friedimformation);
		this.add(friendinfor);
		//��������������ʾ��
		taMsg.setEditable(false);//����Ϊ���ɱ༭
		scrollpaneta.setViewportView(taMsg); //���ı�������ڹ��������
		scrollpaneta.setBounds(0, 50, 300, 450);
		this.add(scrollpaneta);
		//�������ߺ�����ʾ��
		friendlist.setEditable(false);//����Ϊ���ɱ༭
		scrollpanefri.setViewportView(friendlist); //���ı�������ڹ��������
		scrollpanefri.setBounds(300, 50, 150, 450);
		scrollpanefri.setViewportView(friendlist); //���ı�������ڹ��������
		this.add(scrollpanefri);
	    //���������
		tfMsg.setBackground(Color.yellow);
		tfMsg.setBounds(0,500,300,100);
		this.add(tfMsg,BorderLayout.SOUTH);
		//���÷��ͷ�ʽ������
		sendway.addItem("�㲥");
		sendway.addItem("�鲥");
		sendway.addItem("����");
		way.add(infor);
		way.add(sendway);
		//���ð�ť
		way.add(send);
		way.add(exit);
		//���ò������
		way.setBounds(300,500,150,100);
		this.add(way);
		//�����¼�����
		tfMsg.addActionListener(this);
		send.addActionListener(this);
		exit.addActionListener(new ActionListener() {
			//�رհ�ť����
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"�����˳�");
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
		//frame����������
		this.setResizable(false);
		this.setVisible(true); //����Ϊ�ɼ�
		this.setLocationRelativeTo(null); //��������Ļ����
		
		link();
	}
	
	public void link() {		
		number = tfNum.getText();
		nickname = tfAcc.getText();
		try {
			s = new Socket("127.0.0.1",8888);
			OutputStream os = s.getOutputStream();//����������������ǳ�
			PrintStream ps = new PrintStream(os);
			ps.println(number+" "+nickname);
			
			JOptionPane.showMessageDialog(this,"���ӳɹ�");
			this.setTitle("�ͻ��ˣ�"+nickname);
			new Thread(this).start();
		}catch(Exception ex) {}
	}
	
	public void run() {
		try {
			while(true) {
				InputStream is = s.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String str = br.readLine();//��
				
				if((str.substring(0,4)).equals("dead")) {
					JOptionPane.showMessageDialog(this,"���ѱ�ǿ������");
					System.exit(0);
				}
				if(str.substring(0, 2).equals("�б�")) {
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
					str = str.substring(0,str.indexOf("�б�"));
					taMsg.append(str+"\n");//�������
				}
			}
		}catch(Exception ex) {}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			temp = sendway.getSelectedItem().toString();
			OutputStream os = s.getOutputStream();
			PrintStream ps = new PrintStream(os);
			if(temp.equals("�㲥")) {//0����㲥
				ps.println("0"+" "+nickname+":"+tfMsg.getText());
			}
			else if(temp.equals("�鲥")) {//1�����鲥
				String recievenum = JOptionPane.showInputDialog("�������鲥���͵����");
				ps.println("1"+recievenum+" "+nickname+"�ڵ�"+recievenum+"����˵:"+tfMsg.getText());
				//taMsg.append(nickname+"�ڵ�"+recievenum+"����˵:"+tfMsg.getText()+"\n");//�������
			}
			else {//2������
				String recievename = JOptionPane.showInputDialog("������˽�ķ��͵Ŀͻ�����");
				ps.println("2"+recievename+" "+nickname+"��������˵��"+tfMsg.getText());
				taMsg.append(nickname+"������"+recievename+"˵:"+tfMsg.getText()+"\n");//�������
			}
			tfMsg.setText("");
		}catch(Exception ex) {}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Client client = new Client();
	}
}