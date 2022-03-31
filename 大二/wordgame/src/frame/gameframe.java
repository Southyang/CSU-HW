package frame;

import java.io.BufferedReader;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class gameframe extends JFrame{
	private Client c;
	//private BufferedReader br = null;
	//private PrintStream ps = null;
	public gameframe(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String nickname = JOptionPane.showInputDialog("输入昵称");
		this.setTitle(nickname);
		c = new Client();
		this.add(c);
		//获取焦点
		c.setFocusable(true);
		this.setSize(c.getWidth(),c.getHeight());
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);// 居中放置
	}
	public static void main(String[] args) {
		new gameframe();
	}

}
