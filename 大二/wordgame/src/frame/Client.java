package frame;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class Client extends JPanel implements ActionListener,KeyListener,Runnable{
	private int life = 20;//����ֵ
	
	private JLabel lbMoveChar = new JLabel();//�������ĵ���
	private JLabel lbLife = new JLabel();//�ñ�ǩ��ʾ��ǰ����ֵ
	private JLabel lbtranslation = new JLabel();//�ñ�ǩ��ʾ��ǰ��������
	private JLabel keychar1 = new JLabel();//������ĸ1
	private JLabel keychar2 = new JLabel();//������ĸ2	
	
	private Socket s = null;
	private Timer timer = new Timer(20,this);//���Ƶ�������
	private Timer lettertimer = new Timer(2, new timeraction());// ������ĸ����
	private Random rnd = new Random();
	private BufferedReader br = null;
	private PrintStream ps = null;
	private boolean canRun = true;
	private ArrayList words = new ArrayList();
	
	public int flag1;//��ŵ����ϵĿ�ȱλ��
	public int flag2;
	public String flag; //��ŵ���
	public String translation;//��ŵ�������
	public int tmp;
	public String res;//���ȥ���»��ߵĵ���
	public char letter1;//��ű��۵���������ĸ
	public char letter2;
	public char keyletter1;//������ĸ1
	public char keyletter2;//������ĸ2
	
	public Client(){
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setSize(400,500);
		
		creatPW();//��������
		creatPK();
		
	    this.wordfile();//��ȡ���ʿ�
		this.init();
		this.addKeyListener(this);
		try {
			s = new Socket("127.0.0.1",9999);
			JOptionPane.showMessageDialog(this, "���ӳɹ�");
			InputStream is = s.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			OutputStream os = s.getOutputStream();
			ps = new PrintStream(os);
			new Thread(this).start();
		}catch(Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(this, "��Ϸ�쳣�����˳�");
			System.exit(0);
		}
		timer.start();
		lettertimer.start();
	}
	
	public void creatPW(){//��������������
		this.add(lbLife);
		lbLife.setFont(new Font("����",Font.BOLD,20));
		lbLife.setBackground(Color.yellow);
		lbLife.setForeground(Color.PINK);
		lbLife.setBounds(0, 0, this.getWidth(),20);
		
		this.add(lbMoveChar);
		lbMoveChar.setFont(new Font("����",Font.BOLD,20));
		lbMoveChar.setForeground(Color.black);
		
		this.add(lbtranslation);
		lbtranslation.setFont(new Font("����",Font.BOLD,20));
		lbtranslation.setForeground(Color.black);
	}
	
	public void creatPK() {//������ĸ�������
		this.add(keychar1);
		keychar1.setFont(new Font("����",Font.BOLD,20));
		keychar1.setForeground(Color.red);
		
		this.add(keychar2);
		keychar2.setFont(new Font("����",Font.BOLD,20));
		keychar2.setForeground(Color.red);
	}
	
	public void init() {
		tmp = 0;//ÿ�γ�ʼ������Ҫ��tmp��ֵ0
		lbLife.setText("��ǰ����ֵ��"+life);
		//�����������
		randomword();
		keychar1.setBounds(150, 500, this.getWidth(), 20);
		keychar2.setBounds(250, 500, this.getWidth(), 20);
	}
	
	public void randomword() {
		String str;
		while(true) {
			int i = (int)(Math.random()*words.size());
			str = (String) words.get(i);
			if(str.length()>5) {
				break;
			}
		}
		
		String[] ss = str.split(" ");
		String al = ss[0];
		
		flag = al;//����ȷ�ĵ��ʴ���flag
		translation = ss[1];//��������
		
		flag1 = rnd.nextInt(al.length());
		flag2 = rnd.nextInt(al.length());
		while(flag1 == flag2) {
			flag2 = rnd.nextInt(al.length());
		}
		
		if(flag1>flag2) {//��֤flag1С��flag2
			int tmp = flag1;
			flag1 = flag2;
			flag2 = tmp;
		}
		
		letter1 = flag.charAt(flag1);
		letter2 = flag.charAt(flag2);
		//�ڵ������������ո�
		String res = al.substring(0, flag1)+"_"+al.substring(flag1+1, flag2)+"_"+al.substring(flag2+1);
		ss[0] = res;
		
		String check = al.substring(0);
		System.out.print(check);
		System.out.print("\n");
		
		lbMoveChar.setText(ss[0]);
		lbMoveChar.setBounds(rnd.nextInt(this.getWidth()-150),0,this.getWidth(),20);
		lbtranslation.setText(ss[1]);
		lbtranslation.setBounds(0, 430, this.getWidth(), 20);
	}
	
	public void run() {
		try {
			while(canRun) {
				String str = br.readLine();//��
				int score = Integer.parseInt(str);
				life += score;			
				checkFail();
			}
			}catch(Exception ex) {
				canRun = false;
				javax.swing.JOptionPane.showMessageDialog(this, "��Ϸ�쳣�����˳�");
				System.exit(0);
		}
	}

	public void checkFail() {
		if(life<=0) {
			timer.stop();
			lettertimer.stop();
			javax.swing.JOptionPane.showMessageDialog(this,"����ֵ�ľ�����Ϸʧ�ܣ�");
			System.exit(0);
		}
		else {
			init();
		}
	}
	
	//���̲����¼���Ӧ����Ϊ
	public void keyPressed(KeyEvent e) {
		tmp++;
		if(tmp == 1) {
			keyletter1 = e.getKeyChar();
			keychar1.setText(String.valueOf(keyletter1));		
			}
		else  if (tmp == 2){
			keyletter2 = e.getKeyChar();
			keychar2.setText(String.valueOf(keyletter2));	
		}
	}
	
	public void wordfile() {
		try {
		File file = new File("f:\\�������ĵ�\\javaworkfile\\words.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		while(true) {
			String str = br.readLine();
			if(str == null) {
				break;
			}
			words.add(str);
		}
		fr.close();
		br.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "���ʿ⵼���쳣�����˳�");
			System.exit(0);
		}
	}
	
	public void knowword(String word,String translation) {
		try {
			FileWriter fw = new FileWriter("f:\\�������ĵ�\\javaworkfile\\knowwords.txt",true);
			fw.write(word+" "+translation+"\n");
			fw.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "�ѻᵥ�ʿ⵼���쳣�����˳�");
			System.exit(0);
		}
	}
	public void noknowword(String word,String translation) {
		try {
			FileWriter fw = new FileWriter("f:\\�������ĵ�\\javaworkfile\\noknowwords.txt",true);
			fw.write(word+" "+translation+"\n");
			fw.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "��Ƨ���ʿ⵼���쳣�����˳�");
			System.exit(0);
		}
	}
	public void noanswerword(String word,String translation) {
		try {
			FileWriter fw = new FileWriter("f:\\�������ĵ�\\javaworkfile\\noanswerwords.txt",true);
			fw.write(word+" "+translation+"\n");
			fw.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "δ���𵥴ʿ⵼���쳣�����˳�");
			System.exit(0);
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	//Timer�¼���Ӧ��������
	public void actionPerformed(ActionEvent e) {
		if(lbMoveChar.getY()>=this.getHeight()) {
			life--;
			noanswerword(flag,translation);
			checkFail();
		}
		
		lbMoveChar.setLocation(lbMoveChar.getX(),lbMoveChar.getY()+1);	
	}
	
	//Timeraction�¼���Ӧ��ĸ����
	class timeraction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(tmp == 1) {//�����һ����ĸ
				keychar1.setLocation(lbMoveChar.getX(), keychar1.getY()-1);
				
				if( keychar1.getY()<=lbMoveChar.getY() ) {
					//�ж���ĸ�Ϳ�ȱ���Ƿ���ȣ�����ȣ����¿�ʼ����ȣ�����
					
					if(letter1 == keyletter1) {
						res = flag.substring(0, flag1)+keyletter1+flag.substring(flag1+1, flag2)+"_"+flag.substring(flag2+1);
						lbMoveChar.setText(res);
						keychar1.setText(null);
					}
					else {
						life -=2;
						ps.println("1");
						noknowword(flag,translation);
					}
					
				}
			}
			else if(tmp > 1){//����ڶ�����ĸ
					//��һ����ĸ�����˶�
				keychar1.setLocation(lbMoveChar.getX(), keychar1.getY()-1);
					
				if( keychar1.getY()<=lbMoveChar.getY() ) {
					//�ж���ĸ�Ϳ�ȱ���Ƿ���ȣ�����ȣ����¿�ʼ����ȣ�����
						
					if(letter1 == keyletter1) {
						res = flag.substring(0, flag1)+keyletter1+flag.substring(flag1+1, flag2)+"_"+flag.substring(flag2+1);
						lbMoveChar.setText(res);
						keychar1.setText(null);
					}
					else {
						life -=2;
						ps.println("1");
						noknowword(flag,translation);
					}
				}
				//�ڶ�����ĸ�˶�
				keychar2.setLocation(lbMoveChar.getX(), keychar2.getY()-1);
									
				if( keychar2.getY()<=lbMoveChar.getY() ) {
					
					if(letter2 == keyletter2) {
						//�ش���ȷ��һ�֣������Լ���һ�֣��Է���һ��
						life +=1;
						lbMoveChar.setText(flag);
						knowword(flag,translation);
						init();
					}
					else {							
						life -=2;
						ps.println("1");
						noknowword(flag,translation);
						keychar2.setText(null);
					}
				}
			}
		}		
	}
}

