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
	//����������
	private JComboBox fuction = new JComboBox();
	//���尴ť
	private JButton run = new JButton("����"); //��ťrun
	private JButton exit = new JButton("�˳�");//��ťexit
	//�����ǩ
	private JLabel resultinfor = new JLabel("���������н��"); //������Ϣ
	//�����ı���
	private JScrollPane scrollpane = new JScrollPane(); //��������ta
	private static JTextArea run_result = new JTextArea();
	//���������
	private JLabel lbl = new JLabel("����IP��ַ������");
	private JTextField tf = new JTextField(15);
	//�������
	private JPanel jp = new JPanel();
	private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,10,10);
	
	public Ping_Tracert(){
		//frame������������
		this.setBounds(100,100,600,500);//���ô��ڴ�С
		this.setTitle("Ping and Tracert");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);//����Ϊ���Բ���
		//�������
		jp.setLayout(flowLayout);//���ò��ַ�ʽ
		jp.setBounds(0, 0, 600, 50);
		jp.add(lbl);
		jp.add(tf);
		
		fuction.addItem("ping");
		fuction.addItem("tracert");
		jp.add(fuction);
		jp.add(run);
		jp.add(exit);
		this.add(jp);
		//���ñ�ǩ
		resultinfor.setBounds(0, 50, 600, 35);
		this.add(resultinfor);
		//�����ı���
		scrollpane.setViewportView(run_result); //���ı�������ڹ��������
		scrollpane.setBounds(0,85,600,415);
		scrollpane.setBackground(Color.white);
		run_result.setEditable(false);//����Ϊ���ɱ༭
		this.add(scrollpane);
		//frame����������
		this.setResizable(false);
		this.setVisible(true); //����Ϊ�ɼ�
		this.setLocationRelativeTo(null); //��������Ļ����
		
		run.addActionListener(new ActionListener() {
			//run��ť����
			public void actionPerformed(ActionEvent e) {
				String option = fuction.getSelectedItem().toString();
		    	String ip = tf.getText();
		    	System.out.println(option+" "+ip);
		    	runSystemCommand(option+" " + ip);
			}
		});
		exit.addActionListener(new ActionListener() {
			//run��ť����
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
