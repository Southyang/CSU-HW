package frame;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class Client extends JPanel implements ActionListener,KeyListener,Runnable{
	private int life = 20;//生命值
	
	private JLabel lbMoveChar = new JLabel();//掉下来的单词
	private JLabel lbLife = new JLabel();//用标签显示当前生命值
	private JLabel lbtranslation = new JLabel();//用标签显示当前单词释义
	private JLabel keychar1 = new JLabel();//键入字母1
	private JLabel keychar2 = new JLabel();//键入字母2	
	
	private Socket s = null;
	private Timer timer = new Timer(20,this);//控制单词下落
	private Timer lettertimer = new Timer(2, new timeraction());// 控制字母上升
	private Random rnd = new Random();
	private BufferedReader br = null;
	private PrintStream ps = null;
	private boolean canRun = true;
	private ArrayList words = new ArrayList();
	
	public int flag1;//存放单词上的空缺位置
	public int flag2;
	public String flag; //存放单词
	public String translation;//存放单词释义
	public int tmp;
	public String res;//存放去掉下划线的单词
	public char letter1;//存放被扣掉的两个字母
	public char letter2;
	public char keyletter1;//键入字母1
	public char keyletter2;//键入字母2
	
	public Client(){
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setSize(400,500);
		
		creatPW();//界面设置
		creatPK();
		
	    this.wordfile();//读取单词库
		this.init();
		this.addKeyListener(this);
		try {
			s = new Socket("127.0.0.1",9999);
			JOptionPane.showMessageDialog(this, "连接成功");
			InputStream is = s.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			OutputStream os = s.getOutputStream();
			ps = new PrintStream(os);
			new Thread(this).start();
		}catch(Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(this, "游戏异常，已退出");
			System.exit(0);
		}
		timer.start();
		lettertimer.start();
	}
	
	public void creatPW(){//输出单词面板设置
		this.add(lbLife);
		lbLife.setFont(new Font("楷体",Font.BOLD,20));
		lbLife.setBackground(Color.yellow);
		lbLife.setForeground(Color.PINK);
		lbLife.setBounds(0, 0, this.getWidth(),20);
		
		this.add(lbMoveChar);
		lbMoveChar.setFont(new Font("宋体",Font.BOLD,20));
		lbMoveChar.setForeground(Color.black);
		
		this.add(lbtranslation);
		lbtranslation.setFont(new Font("宋体",Font.BOLD,20));
		lbtranslation.setForeground(Color.black);
	}
	
	public void creatPK() {//键入字母面板设置
		this.add(keychar1);
		keychar1.setFont(new Font("楷体",Font.BOLD,20));
		keychar1.setForeground(Color.red);
		
		this.add(keychar2);
		keychar2.setFont(new Font("楷体",Font.BOLD,20));
		keychar2.setForeground(Color.red);
	}
	
	public void init() {
		tmp = 0;//每次初始化，都要将tmp赋值0
		lbLife.setText("当前生命值："+life);
		//出现随机单词
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
		
		flag = al;//将正确的单词传给flag
		translation = ss[1];//保存释义
		
		flag1 = rnd.nextInt(al.length());
		flag2 = rnd.nextInt(al.length());
		while(flag1 == flag2) {
			flag2 = rnd.nextInt(al.length());
		}
		
		if(flag1>flag2) {//保证flag1小于flag2
			int tmp = flag1;
			flag1 = flag2;
			flag2 = tmp;
		}
		
		letter1 = flag.charAt(flag1);
		letter2 = flag.charAt(flag2);
		//在单词中挖两个空格
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
				String str = br.readLine();//读
				int score = Integer.parseInt(str);
				life += score;			
				checkFail();
			}
			}catch(Exception ex) {
				canRun = false;
				javax.swing.JOptionPane.showMessageDialog(this, "游戏异常，已退出");
				System.exit(0);
		}
	}

	public void checkFail() {
		if(life<=0) {
			timer.stop();
			lettertimer.stop();
			javax.swing.JOptionPane.showMessageDialog(this,"生命值耗尽，游戏失败！");
			System.exit(0);
		}
		else {
			init();
		}
	}
	
	//键盘操作事件对应的行为
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
		File file = new File("f:\\语言类文档\\javaworkfile\\words.txt");
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
			javax.swing.JOptionPane.showMessageDialog(this, "单词库导入异常，已退出");
			System.exit(0);
		}
	}
	
	public void knowword(String word,String translation) {
		try {
			FileWriter fw = new FileWriter("f:\\语言类文档\\javaworkfile\\knowwords.txt",true);
			fw.write(word+" "+translation+"\n");
			fw.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "已会单词库导入异常，已退出");
			System.exit(0);
		}
	}
	public void noknowword(String word,String translation) {
		try {
			FileWriter fw = new FileWriter("f:\\语言类文档\\javaworkfile\\noknowwords.txt",true);
			fw.write(word+" "+translation+"\n");
			fw.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "生僻单词库导入异常，已退出");
			System.exit(0);
		}
	}
	public void noanswerword(String word,String translation) {
		try {
			FileWriter fw = new FileWriter("f:\\语言类文档\\javaworkfile\\noanswerwords.txt",true);
			fw.write(word+" "+translation+"\n");
			fw.close();
		}catch(Exception ex){
			javax.swing.JOptionPane.showMessageDialog(this, "未作答单词库导入异常，已退出");
			System.exit(0);
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	//Timer事件对应单词下落
	public void actionPerformed(ActionEvent e) {
		if(lbMoveChar.getY()>=this.getHeight()) {
			life--;
			noanswerword(flag,translation);
			checkFail();
		}
		
		lbMoveChar.setLocation(lbMoveChar.getX(),lbMoveChar.getY()+1);	
	}
	
	//Timeraction事件对应字母上升
	class timeraction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(tmp == 1) {//键入第一个字母
				keychar1.setLocation(lbMoveChar.getX(), keychar1.getY()-1);
				
				if( keychar1.getY()<=lbMoveChar.getY() ) {
					//判断字母和空缺处是否相等，不相等，重新开始，相等，填入
					
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
			else if(tmp > 1){//键入第二个字母
					//第一个字母继续运动
				keychar1.setLocation(lbMoveChar.getX(), keychar1.getY()-1);
					
				if( keychar1.getY()<=lbMoveChar.getY() ) {
					//判断字母和空缺处是否相等，不相等，重新开始，相等，填入
						
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
				//第二个字母运动
				keychar2.setLocation(lbMoveChar.getX(), keychar2.getY()-1);
									
				if( keychar2.getY()<=lbMoveChar.getY() ) {
					
					if(letter2 == keyletter2) {
						//回答正确加一分，错误，自己扣一分，对方加一分
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

