package computer_web;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Ping_Tracert extends JFrame{
	//定义下拉框
	private JComboBox fuction = new JComboBox();
	//定义按钮
	private JButton run = new JButton("运行"); //按钮run
	private JButton exit = new JButton("退出");//按钮exit
	//定义标签
	private JLabel resultinfor = new JLabel("以下是运行结果"); //聊天信息
	//定义文本框
	private JScrollPane scrollpane = new JScrollPane(); //滚动窗口ta
	private static JTextArea run_result = new JTextArea();
	//定义输入框
	private JLabel lbl = new JLabel("输入IP地址或域名");
	private JTextField tf = new JTextField(15);
	//定义面板
	private JPanel jp = new JPanel();
	private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,10,10);
	
	public Ping_Tracert(){
		//frame基本定义设置
		this.setBounds(100,100,600,500);//设置窗口大小
		this.setTitle("Ping and Tracert");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);//设置为绝对布局
		//设置面板
		jp.setLayout(flowLayout);//设置布局方式
		jp.setBounds(0, 0, 600, 50);
		jp.add(lbl);
		jp.add(tf);
		
		fuction.addItem("ping");
		fuction.addItem("tracert");
		jp.add(fuction);
		jp.add(run);
		jp.add(exit);
		this.add(jp);
		//设置标签
		resultinfor.setBounds(0, 50, 600, 35);
		this.add(resultinfor);
		//设置文本框
		scrollpane.setViewportView(run_result); //将文本组件放在滚动面板中
		scrollpane.setBounds(0,85,600,415);
		scrollpane.setBackground(Color.white);
		run_result.setEditable(false);//设置为不可编辑
		this.add(scrollpane);
		//frame框属性设置
		this.setResizable(false);
		this.setVisible(true); //设置为可见
		this.setLocationRelativeTo(null); //放置在屏幕中央
		
		run.addActionListener(new ActionListener() {
			//run按钮处理
			public void actionPerformed(ActionEvent e) {
				String option = fuction.getSelectedItem().toString();
		    	String ip = tf.getText();
		    	System.out.println(option+" "+ip);
		    	runSystemCommand(option+" " + ip);
			}
		});
		exit.addActionListener(new ActionListener() {
			//run按钮处理
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
    public static void runSystemCommand(String command){
         try{
             Process p = Runtime.getRuntime().exec(command);
             BufferedReader inputStream = new BufferedReader(
                       new InputStreamReader(p.getInputStream()));

             String s = "";
             while ((s = inputStream.readLine()) != null) {
            	 System.out.println(s);
            	 run_result.append(s+"\n");
             }    
         }
         catch (Exception e){
         }
    }

    public static void main(String[] args){  
    	new Ping_Tracert();
    }
}
