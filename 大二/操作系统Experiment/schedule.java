package Experiment;
import java.awt.*; 
import javax.swing.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.util.ArrayList;
import java.util.Vector;

@SuppressWarnings("unused") //��ʾ�������ڷ���������û��ʹ�á���Ӵ�ע�����ȥ�������ϵĻ�ɫ����
public class schedule extends Thread{  //�̳�thread�࿪�����߳�
	private int flag = 0;
	/***��ӽ���ؼ�***/
	private JFrame frm; //����GUI����
	private JScrollPane reserveSp,readySp,waitingSp,blockedSp,finishSp; //���й����������
	@SuppressWarnings("rawtypes") //����ָ���ľ���
	private JList reserveList, readyList, waitingList, blockedList, finishList;
	
	@SuppressWarnings("rawtypes") //����ָ���ľ���
	private DefaultListModel reserveLm, readyLm, waitingLm, blockedLm, finishLm;
	
	/***�����ǩ***/
	private JTextArea explainTa = new JTextArea("��ӭʹ�ñ����̵���ģ��ϵͳ��\n"
			+"˵����\n"
			+ "1.������ϢΪ ����������Ҫ������ʱ�䣬����Ȩ��ǰ���������ڴ��С\n"
			+ "2.ǰ��Ϊ0ʱ����ý���Ϊ�������̣��������ǰ�����̵Ľ�����\n"
			+ "3.������ʹ����ռʽ���ȼ����̵����㷨���״���Ӧ�ڴ�����㷨");
	private JLabel tip = new JLabel("���̵���ģ��ϵͳ");
	private JLabel reserveLbl = new JLabel("�󱸶���");
	private JLabel readyLbl = new JLabel("��������");
	private JLabel twocpuLb = new JLabel("˫������������");
	private JLabel cpu1Lb = new JLabel("CPU1");
	private JLabel cpu2Lb = new JLabel("CPU2");
	private JLabel waitingLbl = new JLabel("�������");
	private JLabel blockedLbl = new JLabel("��������");
	private JLabel finishLbl = new JLabel("��ɶ���");
	
	private JLabel createin = new JLabel("��������");
	private JLabel needTimeLbl = new JLabel("Ҫ������ʱ��");
	private JLabel priorityLbl = new JLabel("����Ȩ");
	private JLabel previousLbl = new JLabel("ǰ��");
	private JLabel memoryLbl = new JLabel("���������ڴ�");
	private JLabel system = new JLabel("ϵͳ����");
	private JLabel tipsLbl = new JLabel("��ʾ��");
	private JLabel tipsContentLbl = new JLabel();//��ʾ����
	
	private JTextField cpu1TF = new JTextField(20); //���������������༭�����ı�
	private JTextField cpu2TF = new JTextField(20);
	
	@SuppressWarnings("rawtypes")
	private JComboBox needTimeCb, priorityCb,previousCb,memorysizeCb; //��������б����
	private JButton addBtn, suspend1Btn, suspend2Btn, unsuspendBtn, pauseBtn,exitBtn,goonBtn; //��Ӱ�ť���
	
	
	/***��ʼ������***/
	private int number = 1;   //���������(��ʼ��Ϊ1,ÿ���һ�����̾�+1)
	private String cpu1Process = "";
	private String cpu1ProcessId = "";
	private String cpu1ProcessPriority = "0";
	private String cpu1ProcessTime = "0";
	private String cpu1ProcessPrevious = "0";
	private String cpu1ProcessMemory = "0";
	
	private String cpu2Process = "";
	private String cpu2ProcessId = "";
	private String cpu2ProcessPriority = "0"; 
	private String cpu2ProcessTime = "0";
	private String cpu2ProcessPrevious = "0";
	private String cpu2ProcessMemory = "0";
	
	private String maxPriorityProcess = "";
	private String maxPriorityProcessId = "";
	private String maxPriorityProcessTime = "";   
	private String maxPriorityProcessPriority = "0";
	private String maxPriorityProcessPrevious = "0";
	private String maxPriorityProcessMemory = "0";
	
	private String secPriorityProcess = "";
	private String secPriorityProcessId = "";
	private String secPriorityProcessTime = "";   
	private String secPriorityProcessPriority = "0";
	private String secPriorityProcessPrevious = "0";
	private String secPriorityProcessMemory = "0";
	
	private String b1PriorityProcess = "";//CPU1�ڱ������Ľ���	
	private String b1PriorityProcessId = "";
	private String b1PriorityProcessTime = "";   
	private String b1PriorityProcessPriority = "0";
	private String b1PriorityProcessPrevious = "0";
	private String b1PriorityProcessMemory = "0";
	
	private String b2PriorityProcess = "";//CPU2�ڱ������Ľ���
	private String b2PriorityProcessId = "";
	private String b2PriorityProcessTime = "";   
	private String b2PriorityProcessPriority = "0";
	private String b2PriorityProcessPrevious = "0";
	private String b2PriorityProcessMemory = "0";
	
	/***�������***/
	JPanel memoryPl,addrPl,cpuPl;
	
	ArrayList<JTextField> memory = new ArrayList<JTextField>();
	ArrayList<JTextField> addrNum = new ArrayList<JTextField>();
	int [] record=new int[40];//�ڴ�ռ�ռ�ü�¼
	int totalmemory=0;
	
	/***������������***/
	private String secpri;//�ݴ�ڶ�λ�����ȼ�ֵ(����˫���������)
	
	@SuppressWarnings("rawtypes")
	private Vector waitingReadyV = new Vector(1,1);//�������������ĺ󱸶��н���(ǰ�������)
	
	//��������
	private Font fontLblAcc = new Font("����_GB2312",Font.BOLD|Font.ITALIC,13);
	private Font fontLblAcc2 = new Font("����_GB2312",Font.BOLD|Font.ITALIC,18);
	private Font fontLblAcc1 = new Font("�����п�",Font.BOLD|Font.ITALIC,20);
	private Font fontLblAcc3 = new Font("����_GB2312",Font.BOLD|Font.ITALIC,13);
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public schedule() 
    //���캯��
    {
    	/***���滯***/
    	frm = new JFrame("����ϵͳʵ��");
    	Container box = frm.getContentPane();
    	
    	/***����***/
    	box.setBackground(Color.white);
    	box.setLayout(null);  //����Ϊnull��Ϊ��ղ��ֹ�������֮��������
    	
    	/***����***/
    	tip.setBounds(300,0,200,30);
    	tip.setFont(fontLblAcc1);
    	box.add(tip);
    	
    	/***��ʾ***/
    	tipsLbl.setForeground(Color.BLACK);
    	tipsLbl.setBounds(40,30,100,30);
    	tipsLbl.setFont(new java.awt.Font("Dialog", 1, 18));
    	box.add(tipsLbl);
    	
    	explainTa.setBounds(100,30,450,100);
    	explainTa.setFont(fontLblAcc3);
    	explainTa.setForeground(Color.white);
    	explainTa.setBackground(Color.black);
    	explainTa.setEditable(false);
    	box.add(explainTa);
    	
    	
    	/***�ؼ���λ***/
    	//�󱸶���
    	reserveLbl.setBounds(20,135,60,30); //���ú󱸶���λ�úʹ�С
    	reserveLbl.setFont(fontLblAcc);
    	box.add(reserveLbl); 
    	reserveLm = new DefaultListModel();
    	reserveList = new JList(reserveLm);
    	reserveSp = new JScrollPane(reserveList);  //�������������
    	reserveSp.setBounds(5,165,100,180);	
    	box.add(reserveSp);
    	
    	//��������
    	readyLbl.setBounds(145,135,60,30);
    	readyLbl.setFont(fontLblAcc);
    	box.add(readyLbl);
    	readyLm = new DefaultListModel();
    	readyList = new JList(readyLm);
    	readySp = new JScrollPane(readyList);  //�������������
    	readySp.setBounds(130,165,100,180);	
    	box.add(readySp);
    	
    	//��������
    	blockedLbl.setBounds(270,135,60,30);
    	blockedLbl.setFont(fontLblAcc);
    	box.add(blockedLbl);
    	blockedLm = new DefaultListModel();
    	blockedList = new JList(blockedLm);//  ��waitingLm�е��б����ݷ����б����
    	blockedSp = new JScrollPane(blockedList);
    	blockedSp.setBounds(255,165,100,180);
    	box.add(blockedSp);
    	
    	//�������
    	waitingLbl.setBounds(395,135,60,30);
    	waitingLbl.setFont(fontLblAcc);
    	box.add(waitingLbl);
    	waitingLm = new DefaultListModel();
    	waitingList = new JList(waitingLm);//  ��waitingLm�е��б����ݷ����б����
    	waitingSp = new JScrollPane(waitingList);
    	waitingSp.setBounds(380,165,100,180);
    	box.add(waitingSp);
    	
    	//��ɶ���
    	finishLbl.setBounds(520,135,60,30);
    	finishLbl.setFont(fontLblAcc);
    	box.add(finishLbl);
    	finishLm = new DefaultListModel();
    	finishList = new JList(finishLm);
    	finishSp = new JScrollPane(finishList);
    	finishSp.setBounds(505,165,100,180);
    	box.add(finishSp);
    	
    	//�������̲����
        createin.setBounds(40,350,160,50);
        createin.setFont(fontLblAcc);
        createin.setFont(fontLblAcc2);
    	box.add(createin);
    	needTimeLbl.setBounds(20,400,100,30);
    	box.add(needTimeLbl);
    	
    	String[] cbNumber = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
    	needTimeCb = new JComboBox(cbNumber);
    	needTimeCb.setEditable(true);
    	needTimeCb.setBackground(Color.white);
    	needTimeCb.setBounds(135,400,80,30);
    	box.add(needTimeCb);

    	priorityLbl.setBounds(20,440,100,30);
    	box.add(priorityLbl);
    	
    	
    	priorityCb = new JComboBox(cbNumber);
    	priorityCb.setBackground(Color.white);
    	priorityCb.setBounds(135,440,80,30);
    	box.add(priorityCb);

    	previousLbl.setBounds(20,480,100,30);
    	box.add(previousLbl);
    	
    	String[] previous = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
    	previousCb = new JComboBox(previous);
    	previousCb.setBackground(Color.white);
    	previousCb.setBounds(135,480,80,30);
    	box.add(previousCb);

    	memoryLbl.setBounds(20,520,100,30);
    	box.add(memoryLbl);
    	
    	memorysizeCb = new JComboBox(cbNumber);
    	memorysizeCb.setBackground(Color.white);
    	memorysizeCb.setBounds(135,520,80,30);
    	box.add(memorysizeCb);
    	
    	addBtn = new JButton("��ӽ���");
    	addBtn.setBounds(100,555,100,30);
    	box.add(addBtn);
    	
    	//CPU
    	twocpuLb.setBounds(240,350,160,50);
    	twocpuLb.setFont(fontLblAcc2);
    	box.add(twocpuLb);
    	cpu1TF.setEditable(false);  
    	cpu2TF.setEditable(false);
    	cpuPl = new JPanel(new GridLayout(4,1,20,0));
    	cpuPl.add(cpu1Lb);
    	cpuPl.add(cpu1TF);
    	cpuPl.add(cpu2Lb);
    	cpuPl.add(cpu2TF);
    	cpuPl.setBounds(240,400,160,130);
    	cpuPl.setBackground(Color.CYAN);
        box.add(cpuPl);

        //ϵͳ���� system
        system.setBounds(450,350,160,50);
        system.setFont(fontLblAcc2);
    	box.add(system);
    	suspend1Btn = new JButton("�������");
    	suspend1Btn.setBounds(425,405,100,40);
    	box.add(suspend1Btn);
    	
    	suspend2Btn = new JButton("��������");
    	suspend2Btn.setBounds(530,405,100,40);
    	box.add(suspend2Btn);
    	
    	unsuspendBtn = new JButton("���");
    	unsuspendBtn.setBounds(425,450,100,40);
    	box.add(unsuspendBtn);
    	
    	exitBtn = new JButton("�˳�");
    	exitBtn.setBounds(530,450,100,40);
    	exitBtn.setBackground(Color.red);
    	box.add(exitBtn);
    	
    	pauseBtn = new JButton("��ͣ");
    	pauseBtn.setBounds(425,495,100,40);
    	box.add(pauseBtn); 
    		
    	goonBtn = new JButton("��������");
    	goonBtn.setBounds(530,495,100,40);
    	box.add(goonBtn); 
    	
    	/***�ڴ�ģ�ⲿ��***/
    	JLabel memoryin = new JLabel("�ڴ�������");
    	memoryin.setBounds(650,30,150,30);
    	memoryin.setFont(fontLblAcc2);
    	box.add(memoryin);
    	memoryPl = new JPanel(new GridLayout(41,1,20,0));
    	addrPl = new JPanel(new GridLayout(41,1,20,0));
        memoryPl.add(new JLabel("�ڴ�����Ϊ40"));
        addrPl.add(new JLabel("��ַ"));
        //ArrayList<JTextField> memory=new ArrayList<JTextField>();
        for(int i=0;i<40;i++) {
        	memory.add(new JTextField());
        	memory.get(i).setEditable(false);     	
        }
        for(int k=0;k<memory.size();++k) {
        	memoryPl.add(memory.get(k));
        	memory.get(k).setBackground(Color.white);
        }
        for(int m=0;m<40;m++) {
        	addrNum.add(new JTextField());
        	addrNum.get(m).setText(String.valueOf(m)); 
        	addrPl.add(addrNum.get(m));
        }       
        memoryPl.setBounds(650,60,85,550);
        box.add(memoryPl); 
        addrPl.setBounds(740,60,30,550);
        box.add(addrPl);
        
        memory.get(1).setBackground(Color.red);
        memory.get(2).setBackground(Color.red);
        memory.get(0).setBackground(Color.red);
        memory.get(1).setText("�ں�ռ��");
        memory.get(2).setText("�ں�ռ��");
        memory.get(0).setText("�ں�ռ��");

 
        /***Button�¼�ע��***/     //���а�������
    	ButtonListener btnListener = new ButtonListener();
    	addBtn.addActionListener(btnListener); //���ָ���Ķ���������,�Խ��շ��Դ˰�ť�Ķ����¼���
    	suspend1Btn.addActionListener(btnListener);
    	suspend2Btn.addActionListener(btnListener);
    	unsuspendBtn.addActionListener(btnListener);
    	pauseBtn.addActionListener(btnListener);
    	exitBtn.addActionListener(btnListener);
    	goonBtn.addActionListener(btnListener);
    	
    	/***�������߳̿���***/
    	this.start();
    	frm.setSize(800,700); 
    	frm.setVisible(true);  
    	frm.setLocationRelativeTo(null);
    	frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //�˳�
    	}
    

    @SuppressWarnings({ "unchecked", "static-access" })
	public synchronized void run()
    {
    	try 
    	{
    		while(true)
    		while (flag==0)
	    	{
    			//��־ǰ���Ƿ����
    			boolean havepre1 = false;
    			boolean havepre2 = false;
    			
/***�жϹ�������Ƿ��н����ڵȴ�����������(cpu1,cpu2)***/
	    		while (readyLm.size()<6 && waitingReadyV.size() != 0){//����������н����ڵȴ�����������(�����)
	    			String s = waitingReadyV.get(0).toString();
	    			readyLm.addElement(s);	//���ý��̼����������		
	    			this.sleep(1500);
	    			waitingReadyV.remove(0);
	    			waitingLm.removeElement(s);
	    			
	    		}
    			
/***�ж����������ܷ����������***/
	    		if (!blockedLm.isEmpty()&&!finishLm.isEmpty()) {
	    			//���������зǿ����н������
	    			String blockedInfo[];
	    			String finishInfo[];
	    			String tempProcess1 = "";
	    			
	    			for (int i = blockedLm.size() - 1 ; i >= 0 ; i--) {
	    				blockedInfo = blockedLm.getElementAt(i).toString().split(","); //����������Ϣ
	    				for (int j = finishLm.size() - 1 ; j >= 0  ; j--) 
	    				{
	    					finishInfo = finishLm.getElementAt(j).toString().split(",");//��ɽ�����Ϣ
	    					if(Integer.parseInt(blockedInfo[3]) == Integer.parseInt(finishInfo[0])) 
	    						//����������̵�ǰ�������
	    					{
	    						
	    						tempProcess1 =blockedInfo[0]+","+blockedInfo[1]+","+blockedInfo[2]+","+blockedInfo[3]+","+blockedInfo[4];		    						
	    						readyLm.addElement(tempProcess1);//�ý��̽���������
	    						blockedLm.removeElementAt(i);    						
	    						this.sleep(1500);
	    					}
	    				}
	    			}
	    		}
	    		
    			
    			
/***�жϺ󱸶��н����ܷ����������***/
	    		String a[];
	    		int temp;	
	    		
    			//����󱸶��зǿ����ڴ�ռ��㹻
	    		while ((!reserveLm.isEmpty())&&(totalmemory <= memory.size()-4)){
	    				a = reserveLm.getElementAt(0).toString().split(","); 
		    			totalmemory += Integer.parseInt(a[4]);
		    			
		    			//ÿ�����̽��ڴ�֮ǰ��Ҫȷ��
		    			if(totalmemory <= memory.size()-4) {
		    				for(int x = 0 ; x < memory.size() ; x ++) {
		    					
		    					//���һ���������ڴ�ռ�ŵ����������
		    					if((record[x]-x+1)>=Integer.parseInt(a[4])){
		    						readyLm.addElement(reserveLm.getElementAt(0));//�ý��̽���������
		    		    			reserveLm.removeElementAt(0);
		    		    			temp=record[x];
		    		    			
		    		    			//��ʾ�ڴ�ռ��ռ�����
		    		    			for(int y = x ; y < x + Integer.parseInt(a[4]) ; y++) {
		    		    				memory.get(y).setBackground(Color.YELLOW);
		    		    				memory.get(y).setText(a[0]);
		    		    				record[y] = -1;
		    		    			}
		    		    			
		    		    			//��ǳ��µĿ����������ʼλ��
		    		    			if(x + Integer.parseInt(a[4]) <= memory.size()) {
		    		    				if((x + Integer.parseInt(a[4])) <= 39) {
		    		    					record[x+Integer.parseInt(a[4])] = temp;
		    		    				}	    		    				
		    		    			}	    		    				    		    			
		    		    			break;
		    					}
		    				}
		    					    				
			    			this.sleep(1500);			    			
		    			}	    					    			
	    		}
	    		
	    		/***���������еĽ��̰����ȼ������ҳ����ȼ����Ľ���CPU����***/
	    		//���������зǿ�
	    		if (!readyLm.isEmpty()){
	    			String[] tempProcess2 = {" "};
	    		
	    			//String[] l;
	    			boolean unblocked1 = false;
	    			boolean unblocked2 = false;

	    			for (int i = readyLm.size()-1; i >= 0 ; i --)
	    			{
	    				tempProcess2 = readyLm.getElementAt(i).toString().split(","); 
	    				
	    				//����ý�������Ȩ����cpu 1�ڽ��̵�����Ȩ
	    				if (Integer.parseInt(tempProcess2[2]) >= Integer.parseInt(cpu1ProcessPriority)){
	    					
	    					//����ý�����ǰ��
	    					if(Integer.parseInt(tempProcess2[3]) == 0){
	    						secpri=cpu1ProcessPriority;//�ݴ�
	    					    maxPriorityProcess = readyLm.getElementAt(i).toString();
	    					    maxPriorityProcessId = tempProcess2[0];
	    					    maxPriorityProcessTime = tempProcess2[1];
	    					    maxPriorityProcessPriority = tempProcess2[2];
	    					    maxPriorityProcessPrevious = tempProcess2[3];
	    					    maxPriorityProcessMemory = tempProcess2[4];
	    					    cpu1ProcessPriority = maxPriorityProcessPriority; 
	    					}
	    						    					
	    					//����ý�����ǰ��
		    				else if(Integer.parseInt(tempProcess2[3])!=0) 
		    				{
		    					String[] finishInfo;
		    					
		    					//�����ɶ����޽���
			    				if(finishLm.size()==0) {
			    					
			    					//˵���ý��̵�ǰ��δ���
		    						havepre1 = true; 
		    						
		    						//�ý��̽���������
		    						b1PriorityProcess = readyLm.getElementAt(i).toString();
		    						blockedLm.addElement(readyLm.getElementAt(i));
		    						readyLm.remove(i);
    		    					b1PriorityProcessId = tempProcess2[0];
    		    					b1PriorityProcessTime = tempProcess2[1];
    		    					b1PriorityProcessPriority = tempProcess2[2];
    		    					b1PriorityProcessPrevious = tempProcess2[3];
    		    					b1PriorityProcessMemory = tempProcess2[4];
		    					}
			    				//����(��ɶ����н���)
		    					else 
		    					{
		    						for (int j = finishLm.size()-1; j >= 0; j--) 
		    	    				{
		    	    					finishInfo = finishLm.getElementAt(j).toString().split(",");
		    	    					//����ý�����ǰ����ǰ�������
		    	    					if(Integer.parseInt(tempProcess2[3]) == Integer.parseInt(finishInfo[0])){ 
		    	    						unblocked1=true;
		    	    						secpri=cpu1ProcessPriority;
		    	    						maxPriorityProcess = readyLm.getElementAt(i).toString();
		    		    					maxPriorityProcessId = tempProcess2[0];
		    		    					maxPriorityProcessTime = tempProcess2[1];
		    		    					maxPriorityProcessPriority = tempProcess2[2];
		    		    					maxPriorityProcessPrevious = tempProcess2[3];
		    		    					maxPriorityProcessMemory = tempProcess2[4];
		    		    					cpu1ProcessPriority = maxPriorityProcessPriority;
		    	    					}
		    	    				}
		    						
		    						if(!unblocked1) {
		    							//���ǰ��δ���
		    					        havepre1 = true;
		    					        //�ý��̽�����������
		    							b1PriorityProcess = readyLm.getElementAt(i).toString();
	    		    					b1PriorityProcessId = tempProcess2[0];
	    		    					b1PriorityProcessTime = tempProcess2[1];
	    		    					b1PriorityProcessPriority = tempProcess2[2];
	    		    					b1PriorityProcessPrevious = tempProcess2[3];
	    		    					b1PriorityProcessMemory = tempProcess2[4];
		    						}
		    						
		    					}				    				
		    				}		    					
	    				}			    				
	    			}
	    		
    		
    			
	    		
	    			    		
	    		/***���ȼ���ռ***/
	    		
	    		//�����ǰCPU1��Ľ��̵����ȼ���������    			
	    			if (!maxPriorityProcessId.equals(cpu1ProcessId)){
	    				//���������ȼ�������0
		    			if(Integer.parseInt(maxPriorityProcessPriority)!=0) {
		    				cpu1Process = maxPriorityProcess;
			    			cpu1ProcessId = maxPriorityProcessId;
			    			cpu1ProcessTime = maxPriorityProcessTime;
			    			cpu1ProcessPriority = maxPriorityProcessPriority;
			    			cpu1ProcessPrevious = maxPriorityProcessPrevious;
			    			cpu1ProcessMemory = maxPriorityProcessMemory;
		    			}
	    				//���������ȼ�����0
		    			else if(Integer.parseInt(maxPriorityProcessPriority) == 0){
		    				for (int i = readyLm.size()-1; i >= 0; i--){
		    					String readyInfo[];
		    					tempProcess2 = readyLm.getElementAt(i).toString().split(",");
		    					readyInfo = readyLm.getElementAt(0).toString().split(",");
		    					cpu1ProcessId = readyInfo[0];
		    					
		    					//������������н��������ǰ��(�����ȷ���)
			    				if (Integer.parseInt(tempProcess2[3])==0){
			    					if(Integer.parseInt(tempProcess2[0]) < Integer.parseInt(cpu1ProcessId)) 
			    					{
			    						cpu1ProcessId=tempProcess2[0];
			    					    maxPriorityProcess = readyLm.getElementAt(i).toString();
			    					    maxPriorityProcessId = tempProcess2[0];
			    					    maxPriorityProcessTime = tempProcess2[1];
			    					    maxPriorityProcessPriority = tempProcess2[2];
			    					    maxPriorityProcessPrevious = tempProcess2[3];
			    					    maxPriorityProcessMemory = tempProcess2[4];
			    					    cpu1ProcessPriority = maxPriorityProcessPriority; 
			    					}
			    				}
			    			}
		    				cpu1Process = maxPriorityProcess;
			    			cpu1ProcessId = maxPriorityProcessId;
			    			cpu1ProcessTime = maxPriorityProcessTime;
			    			cpu1ProcessPriority = maxPriorityProcessPriority;
			    			cpu1ProcessPrevious = maxPriorityProcessPrevious;
			    			cpu1ProcessMemory=maxPriorityProcessMemory;
		    			}
	    				
		    		}
	    			if(havepre1&&Integer.parseInt(cpu1ProcessPriority)<Integer.parseInt(b1PriorityProcessPriority)) {
	    				readyLm.removeElement(b1PriorityProcess);
	    				blockedLm.addElement(b1PriorityProcess);	
		    			havepre1=false;
	    				
	    			}
	    			
	    			/***CPU2***/
	    			boolean flag2 = false;
	    			for (int i = readyLm.size()-1; i >= 0; i--)
	    			{
	    				tempProcess2 = readyLm.getElementAt(i).toString().split(","); 
	    				
	    				//�Ȱ�cpu2���
	    				cpu2Process ="";
	    				cpu2ProcessId = "";
	    				cpu2ProcessTime = "0";
	    				cpu2ProcessPriority = "0";
	    				cpu2ProcessPrevious = "0";
	    				cpu2ProcessMemory="0";
	    				
	    				//����������еĽ��̴��ڵ��ڵڶ����ȼ� �� С�ڵ��ڵ�һ���ȼ� �� �����ڵ�ǰcpu1�ڵĽ���(��������ظ���CPU)
	    				if (Integer.parseInt(tempProcess2[2]) >= (Integer.parseInt(secpri)) 
	    						&&Integer.parseInt(tempProcess2[2]) <= Integer.parseInt(maxPriorityProcessPriority)
	    						&&!tempProcess2[0].equals(cpu1ProcessId) ){
	    					
	    					//����ý�����ǰ��
	    					if( Integer.parseInt(tempProcess2[3])==0){
	    						secPriorityProcess = readyLm.getElementAt(i).toString();//���������ڽ��̣�0��Ϊ�ڶ����ȼ�
		    					secPriorityProcessId = tempProcess2[0];
		    					secPriorityProcessTime = tempProcess2[1];
		    					secPriorityProcessPriority = tempProcess2[2];
		    					secPriorityProcessPrevious = tempProcess2[3];
		    					secPriorityProcessMemory = tempProcess2[4];
		    					cpu2ProcessPriority = secPriorityProcessPriority;//�ý��̽�cpu2

		    					secpri = tempProcess2[2];
		    					flag2=true;
	    					}
	    					
	    					//������������ڽ�����ǰ��
	    					else if(Integer.parseInt(tempProcess2[3])!=0) 
		    				{
		    					String[] finishInfo;
			    				
		    					//�����ɶ���Ϊ��
			    				if(finishLm.size()==0){
		    						havepre2 = true;  //��ǰ����ǰ��δ���
		    						b2PriorityProcess = readyLm.getElementAt(i).toString();//�ý��̱�����
    		    					b2PriorityProcessId = tempProcess2[0];
    		    					b2PriorityProcessTime = tempProcess2[1];
    		    					b2PriorityProcessPriority = tempProcess2[2];
    		    					b2PriorityProcessPrevious = tempProcess2[3];
    		    					b2PriorityProcessMemory = tempProcess2[4];
		    					}
			    				//�����ɶ��зǿ�
		    					else {
		    						for (int j = finishLm.size()-1; j >= 0; j--){
		    	    					finishInfo = finishLm.getElementAt(j).toString().split(",");
		    	    					
		    	    					//����ý��̵�ǰ�������
		    	    					if(Integer.parseInt(tempProcess2[3]) == Integer.parseInt(finishInfo[0])){
		    	    						
		    	    						unblocked2=true;
		    	    						secPriorityProcess = readyLm.getElementAt(i).toString();
		    		    					secPriorityProcessId = tempProcess2[0];
		    		    					secPriorityProcessTime = tempProcess2[1];
		    		    					secPriorityProcessPriority = tempProcess2[2];
		    		    					secPriorityProcessPrevious = tempProcess2[3];
		    		    					secPriorityProcessMemory = tempProcess2[4];
		    		    					cpu2ProcessPriority = secPriorityProcessPriority;//�ý��̽�cpu2
		    		    					flag2=true;
		    	    					}
		    	    				}
		    						//��ǰ����δ������
		    						if(!havepre2&&!unblocked2) {
		    			   				havepre2 = true;
		    							b2PriorityProcess = readyLm.getElementAt(i).toString();
	    		    					b2PriorityProcessId = tempProcess2[0];
	    		    					b2PriorityProcessTime = tempProcess2[1];
	    		    					b2PriorityProcessPriority = tempProcess2[2];
	    		    					b2PriorityProcessPrevious = tempProcess2[3];
	    		    					b2PriorityProcessMemory = tempProcess2[4];
		    						}
		    						
		    					}				    				
		    				}
	    				}	    						    					    				
	    			}
	    			
	    			//����ڶ����ȼ�������cpu2�ڽ��̵����ȼ� �� cpu2�ڽ���������������
	    			if (!secPriorityProcessId.equals(cpu2ProcessId) && flag2){
		    			
	    				//����ڶ����ȼ�������0
	    				if(Integer.parseInt(secPriorityProcessPriority)!=0) {
	    					cpu2Process = secPriorityProcess;//�ý��̽�cpu2(���ȼ���ռ)
			    			cpu2ProcessId = secPriorityProcessId;
			    			cpu2ProcessTime = secPriorityProcessTime;
			    			cpu2ProcessPriority = secPriorityProcessPriority;
			    			cpu2ProcessPrevious = secPriorityProcessPrevious;
			    			cpu2ProcessMemory = secPriorityProcessMemory;
	    				}
	    				//����ڶ����ȼ�����0
		    			else if(Integer.parseInt(secPriorityProcessPriority)==0) {
		    				for (int i = readyLm.size()-1; i>=0; i--){
		    					String readyInfo[];
		    					tempProcess2 = readyLm.getElementAt(i).toString().split(",");
		    					readyInfo = readyLm.getElementAt(0).toString().split(",");
		    					cpu2ProcessId = readyInfo[0];//�����ȷ���
			    				if (Integer.parseInt(tempProcess2[0]) < Integer.parseInt(cpu2ProcessId)&&!tempProcess2[0].equals(cpu1ProcessId) )
			    				{
			    					//�������Ȩ=0
			    					if(Integer.parseInt(tempProcess2[3])==0) 
			    					{
			    						cpu2ProcessId=tempProcess2[0];
			    					    secPriorityProcess = readyLm.getElementAt(i).toString();
			    					    secPriorityProcessId = tempProcess2[0];
			    					    secPriorityProcessTime = tempProcess2[1];
			    					    secPriorityProcessPriority = tempProcess2[2];
			    					    secPriorityProcessPrevious = tempProcess2[3];
			    					    secPriorityProcessMemory = tempProcess2[4];

			    					    cpu1ProcessPriority = maxPriorityProcessPriority; 
			    					}
			    				}
			    			}
		    				cpu2Process = secPriorityProcess;
			    			cpu2ProcessId = secPriorityProcessId;
			    			cpu2ProcessTime = secPriorityProcessTime;
			    			cpu2ProcessPriority = secPriorityProcessPriority;
			    			cpu2ProcessPrevious = secPriorityProcessPrevious;
			    			cpu2ProcessMemory = secPriorityProcessMemory;
		    			}
		    		}
	    			
	    			if(havepre2&&Integer.parseInt(cpu2ProcessPriority)<Integer.parseInt(b2PriorityProcessPriority)) {
	    				readyLm.removeElement(b2PriorityProcess);
	    				blockedLm.addElement(b2PriorityProcess);
		    			havepre2=false;
	    			}
	    			
	    			cpu1TF.setText(cpu1ProcessId);
	    			cpu2TF.setText(cpu2ProcessId);
	    			
	    			readyLm.removeElement(cpu1Process);
	    			readyLm.removeElement(cpu2Process);
	    			
	    			this.sleep(1500);
	    		}
	    			    	    		
	    		//ʱ��Ƭ���ˣ�CPU�ϵĽ���ת���������л���ɶ���
	    		boolean cpuTorlbl = false;//cpu������
	    		boolean cpuToflbl = false;//cpu�����
	    		if (!cpu1TF.getText().equals("") )
	    			//���cpu1��Ϊ��
	    		{

	    			cpu1TF.setText("");//��cpu1���
	    			int time = Integer.parseInt(cpu1ProcessTime);
	    			cpu1ProcessTime = String.valueOf(--time);
		    		if (Integer.parseInt(cpu1ProcessPriority) > 0) //�����ȼ�����0���1
	    			{

	    				int priority = Integer.parseInt(cpu1ProcessPriority);
	    				cpu1ProcessPriority = String.valueOf(--priority);
		    				
	    			}
	    	
		    		if (Integer.parseInt(cpu1ProcessTime) > 0) //Ҫ������ʱ�����0�����ready����
		    		{
		    			cpu1Process = cpu1ProcessId + "," + cpu1ProcessTime + "," + cpu1ProcessPriority + "," + cpu1ProcessPrevious+ "," + cpu1ProcessMemory;		    			
		    			readyLm.addElement(cpu1Process);	
		    			cpuTorlbl = true;	
		    			cpu1TF.setText("");   //�����Ѿ��ÿ�һ����
		    			cpu1ProcessPriority = "0"; 
		    		}
		    		else   //Ҫ������ʱ��Ϊ0��ת����ɶ���
		    		{
		    			cpu1ProcessTime = String.valueOf(0);
		    			cpu1ProcessPriority = String.valueOf(0);
		    			cpu1Process = cpu1ProcessId + "," + cpu1ProcessTime + "," + cpu1ProcessPriority+ "," + cpu1ProcessPrevious+ "," + cpu1ProcessMemory;
		    			cpuToflbl = true;
		    			finishLm.addElement(cpu1Process);
		    			int cpu1memory=Integer.parseInt(cpu1ProcessMemory);
		    			
		    			/***�ڴ�ռ����***/
		    			totalmemory -= Integer.parseInt(cpu1ProcessMemory);
		    			boolean havechange=false;
		    			for(int x = 0 ; x < memory.size() ; x ++) {
		    				if(memory.get(x).getText().equals(cpu1ProcessId)) {
		    					//record[x]=x+cpu1memory-1;
		    					if(!havechange) {
		    						
		    						memory.get(x).setBackground(Color.WHITE);
	    		    				memory.get(x).setText("");
	    		    				
	    		    				//����Ҫ�л��յ��������
	    		    				//�����һ�鲻�ǿ��еĻ�����һ����0
	    		    				record[x]=x+cpu1memory-1;
	    		    				if(x==0||((x-1)>=0&&!memory.get(x-1).getText().equals(""))) {
	    		    					//�����һ��Ҳ���ǿ��е�
	    		    					if(x==39||(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals(""))) {
	    		    						//�����һ��Ҳ��Ϊ�գ���ôrecord[x]�Ͳ��ñ���
	    		    						record[x]=record[x];
	    		    						System.out.println(x+"-"+record[x]+"Ϊ�����ڴ�");
	    		    						//record[x]=record[1+record[x]];
	    		    					}
	    		    						    		    					
	    		    					//�����һ���ǿ��еģ���ôҪ�ϲ�����
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						record[x]=record[1+record[x]];
	    		    						System.out.println(x+"-"+record[x]+"Ϊ�����ڴ�");
	    		    						//�ռӵ�
	    		    						if(1+record[x]<memory.size()) {
	    		    							record[1+record[x]]=0;
	    		    						}
	    		    						
	    		    					}
	    		    				}
	    		    				//�����һ���ǿ��е�
	    		    				else if((x-1)>=0&&memory.get(x-1).getText().equals("")) {
	    		    					int z=x-1;
	    		    					while(z>=0&&z<=0&&memory.get(z).getText().equals("")) {
	    		    						z--;
	    		    					}
	    		    					if(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals("")) {
	    		    						record[z+1]=record[x];
	    		    						System.out.println((z+1)+"-"+record[x]+"Ϊ�����ڴ�");
	    		    						//�ռӵ�
	    		    						record[x]=0;
	    		    					}
	    		    					//�����һ���ǿ��еģ�Ҫ�ϲ�
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						
	    		    						record[z+1]=record[1+record[x]];
	    		    						System.out.println((z+1)+"-"+record[z+1]+"Ϊ�����ڴ�");
	    		    					}	    		    					
	    		    				}	    		    				
	    		    					    		    				
	    		    				havechange =true;
		    					}
		    					
		    					memory.get(x).setBackground(Color.WHITE);
    		    				memory.get(x).setText("");
    		    				record[x]=0;
		    				}
		    			}
		    			
		    			cpu1ProcessId = "";
		    		}
	    		
	    		}
	    		
	    		if (!cpu2TF.getText().equals("") && !cpu2ProcessId.equals(""))  //������˸�����
	    		{

	    			cpu2TF.setText("");
	    			int time = Integer.parseInt(cpu2ProcessTime);
	    			cpu2ProcessTime = String.valueOf(--time);
		    		if (Integer.parseInt(cpu2ProcessPriority) > 0) //�����ȼ�����0���1
	    			{

	    				int priority = Integer.parseInt(cpu2ProcessPriority);
	    				cpu2ProcessPriority = String.valueOf(--priority);
		    			
	    			}
		    			    	
		    		if (Integer.parseInt(cpu2ProcessTime) > 0) //Ҫ������ʱ�����0�����ready����
		    		{
		    			cpu2Process = cpu2ProcessId + "," + cpu2ProcessTime + "," + cpu2ProcessPriority+ "," + cpu2ProcessPrevious+ "," + cpu2ProcessMemory;		    			
		    			readyLm.addElement(cpu2Process);
		    			cpuTorlbl = true;			
		    			cpu2TF.setText("");
		    			cpu2ProcessId="";
		    			cpu2ProcessPriority = "0"; 
		    		}
		    		else  //Ҫ������ʱ��Ϊ0��ת����ɶ���
		    		{
		    			cpu2ProcessTime = String.valueOf(0);
		    			cpu2ProcessPriority = String.valueOf(0);
		    			cpu2Process = cpu2ProcessId + "," + cpu2ProcessTime + "," + cpu2ProcessPriority+ "," + cpu2ProcessPrevious+ "," + cpu2ProcessMemory;	    			
		    			cpuToflbl = true;
		    			finishLm.addElement(cpu2Process);
		    			int cpu2memory=Integer.parseInt(cpu2ProcessMemory);
		    			
		    			//�����ڴ�ռ�
		    			totalmemory-=Integer.parseInt(cpu2ProcessMemory);
		    			boolean havechange=false;
		    			for(int x=0;x<memory.size();x++) {
		    				if(memory.get(x).getText().equals(cpu2ProcessId)) {
		    					//record[x]=x+cpu2memory-1;
		    					if(!havechange) {
		    						memory.get(x).setBackground(Color.WHITE);
	    		    				memory.get(x).setText("");
	    		    				record[x]=x+cpu2memory-1;
	    		    				//�����һ�鲻�ǿ��еĻ�����һ����0
	    		    				if(x==0||((x-1)>=0&&!memory.get(x-1).getText().equals(""))) {
	    		    					//�����һ��Ҳ���ǿ��еĻ�����һ���ǵ�39��
	    		    					if(x==39||(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals(""))) {
	    		    						//�����һ��Ҳ��Ϊ�գ���ôrecord[x]�Ͳ��ñ���
	    		    						record[x]=record[x];
	    		    						System.out.println(x+"-"+record[x]+"Ϊ�����ڴ�");
	    		    						//record[x]=record[1+record[x]];
	    		    					}
	    		    						    		    					
	    		    					//�����һ���ǿ��еģ���ôҪ�ϲ�����
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						int z=record[x]+1;
		    		    					while(memory.get(z).getText().equals("")) {
		    		    						z++;	    		    						
		    		    					}
		    		    					record[x]=z-1;
		    		    					System.out.println(x+"-"+record[x]+"Ϊ�����ڴ�");
	    		    						//record[x]=record[1+record[x]];
	    		    						//�ռӵ�
	    		    						//record[1+record[x]]=0;
		    		    					if(1+record[x]<memory.size()) {
	    		    							record[1+record[x]]=0;//��¼����
	    		    						}
	    		    					}
	    		    				}
	    		    				//�����һ���ǿ��е�
	    		    				else if((x-1)>=0&&memory.get(x-1).getText().equals("")) {
	    		    					int z=x-1;
	    		    					while(z>=0&&memory.get(z).getText().equals("")) {
	    		    						z--;
	    		    					}
	    		    					if(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals("")) {
	    		    						record[z+1]=record[x];
	    		    						System.out.println((z+1)+"-"+record[x]+"Ϊ�����ڴ�");
	    		    						//�ռӵ�
	    		    						record[x]=0;
	    		    					}
	    		    					//�����һ���ǿ��еģ�Ҫ�ϲ�
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						
	    		    						record[z+1]=record[1+record[x]];
	    		    						System.out.println((z+1)+"-"+record[z+1]+"Ϊ�����ڴ�");
	    		    					}	    		    					
	    		    				}	    		    				
	    		    					    		    				
	    		    				havechange =true;
		    					}
		    					
		    					memory.get(x).setBackground(Color.WHITE);
    		    				memory.get(x).setText("");
    		    				record[x]=0;
		    				}
		    			}
		    			cpu1ProcessId = "";
		    		}	    		
	    		}

	    		this.sleep(1500);
	    		
	    		//����ͳ��һ�¿��ڴ��
	    		int count=0;
	    		for(int r=0;r<memory.size();r++) {
	    			
	    			while(memory.get(r).getText().equals("")) {
	    				++count;
	    				++r;
	    				if(r==40) {
	    					break;
	    				}
	    			}
	    			record[r-count]=r-1;
	    			//r--;
	    			count=0;
	    		}
	   	    sleep(1000);
	    	}	    	
	   }
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}   	
    }
   
    					
     /***��ť����***/
    class ButtonListener implements ActionListener{
    	@SuppressWarnings("unchecked")
		public synchronized void actionPerformed(ActionEvent ae){
    		JButton source = (JButton)ae.getSource();  //�����¼�Դ
    		if (source == addBtn)          //���ӽ���
    		{
    			String time = "1";//���ó�ʼֵ
    			time = needTimeCb.getSelectedItem().toString();
    			String priority = "1";//���ó�ʼֵ
    			priority = priorityCb.getSelectedItem().toString();
    			String pid = String.valueOf(number);
    			String previous = "0";//���ó�ʼֵ
    			previous = previousCb.getSelectedItem().toString();
    			String memorysize = "1";//���ó�ʼֵ
    			memorysize = memorysizeCb.getSelectedItem().toString();
    			
    			//�������������ǰ��
    			if(pid.equals(previous)) {
    				tipsContentLbl.setText("���ܰ��Լ���Ϊǰ����");	
    			}
    			//����(������������ǰ��)
    			else {
    			String reserveItem = "";
    			reserveItem = pid + "," + time + "," + priority +  "," + previous + "," + memorysize;
    			reserveLm.addElement(reserveItem);//�Ѹý�����ӵ��󱸶���
    			number ++;
    			tipsContentLbl.setText("");
    			}
    		}
    		else if (source == suspend1Btn){ //�������
    			 String suspendProcess = ""; 
    			 String S[]; 
    			 //�����ѡ��
     			if (readyList.getSelectedValue()!= null){
     				suspendProcess = readyList.getSelectedValue().toString();
     				S = readyList.getSelectedValue().toString().split(",");
     				waitingLm.addElement(suspendProcess);//���ý�����ӵ��������
     				readyLm.removeElement(suspendProcess);//���ý��̴Ӿ��������Ƴ�
     				
     				int waitingmemory = Integer.parseInt(S[4]);
     				
    				/***�����ڴ�ռ�***/
     				memory_recovery(waitingmemory,totalmemory,S,memory);
	    			
     				
     				tipsContentLbl.setText("");        
     				System.out.println("����ɹ�" ); /////
     				try
     				{
     					sleep(1500);
     				}
     				catch (Exception e)
     				{
     				}
     				//CPUToWLbl.setForeground(Color.BLACK);
     			}
     			else 
     			{
     				tipsContentLbl.setText("ѡ��һ�������Ľ���");
     			}
    		}
    		else if (source == suspend2Btn) //��������
    		{
    			System.out.println("�������" ); /////
    			
    			String suspendProcess = ""; 
   			    String S[];
    			if (blockedList.getSelectedValue()!= null)
    			{
    				suspendProcess = blockedList.getSelectedValue().toString();
    				S=blockedList.getSelectedValue().toString().split(",");
    				waitingLm.addElement(suspendProcess);
    				blockedLm.removeElement(suspendProcess);
    				
    				int waitingmemory=Integer.parseInt(S[4]);
    				//�����ڴ�ռ�
    				memory_recovery(waitingmemory,totalmemory,S,memory);
	    			
    				
    				tipsContentLbl.setText("");        
    				System.out.println("����ɹ�" ); /////
    				try
    				{
    					sleep(1500);
    				}
    				catch (Exception e)
    				{
    				}
    				//CPUToWLbl.setForeground(Color.BLACK);
    			}
    			else 
    			{
    				tipsContentLbl.setText("ѡ��һ�������Ľ���");
    			}
    		}
    		
    		else if (source == unsuspendBtn)  //��ҽ���
    		{    			
    			System.out.println("������" ); /////
    			String unsuspendProcess = "";
    			
    			if (waitingList.getSelectedValue()!= null)
    			{
    				unsuspendProcess = waitingList.getSelectedValue().toString();
    				waitingReadyV.add(unsuspendProcess);

    				tipsContentLbl.setText("");     				
    				//�����ڴ�
    				String [] a = new String[10];
    				//dataType[] arrayRefVar = new dataType[arraySize];
    				int temp;
    				a = unsuspendProcess.split(","); 
	    			totalmemory += Integer.parseInt(a[4]);
	    			//��ʼ���ڴ������λ��
	    			boolean canput=false;
	    			if(totalmemory <= memory.size()-4) {
	    				
	    				for(int x = 0;x < memory.size() -1; x++) {
	    					//���һ���������ڴ�ռ�ŵ���������̣��ͽ���������
	    					if((record[x]-x+1)>=
	    							Integer.parseInt(a[4])) {
	    		    			canput=true;
	    						temp=record[x];	    		    			
	    		    			for(int y=x;y<x+Integer.parseInt(a[4]);y++) {
	    		    				memory.get(y).setBackground(Color.YELLOW);
	    		    				memory.get(y).setText(a[0]);
	    		    				record[y]=-1;
	    		    			}
	    		    			//��ǳ��µĿ����������ʼλ��
	    		    			if(x+Integer.parseInt(a[4])<=memory.size()) {
	    		    				System.out.println(x+"-"+(x+Integer.parseInt(a[4])-1)+"��ռ��");
	    		    				if((x+Integer.parseInt(a[4]))<=39) {
	    		    					record[x+Integer.parseInt(a[4])]=temp;
		    		    				System.out.println(x+Integer.parseInt(a[4])+"-"+temp+"Ϊ�����ڴ�");
	    		    				}	    		    				
	    		    			}
	    		    			System.out.println("��ҳɹ�" ); /////	
	    		    			break;
	    					}
	    					//���û�к��ʴ�С�ģ���ֱ��������һ���������ڴ��
	    					else {
	    						x=record[x]-1;
	    					}
	    				}
	    			}
	    			if(!canput) {
	    				System.out.println("���ʧ��" );
	    				tipsContentLbl.setText("û���㹻���ڴ�");
	    			}
    				
    				
	    			
    			}
    			else 
    			{
    				tipsContentLbl.setText("ѡ��һ�����ҽ���");
    			}
    		}
    	
    		else if(source == pauseBtn) {
    			flag = 1;
    		}
    		
    		else if (source == exitBtn) //�˳�����
    		{
    			System.exit(0);
    		}
    		
    		else if(source == goonBtn) //��������
    		{
    			flag = 0;
    		}
    	}
    }
    
    
     // �������� 
    public static void main(String args[])
    {
    	new schedule();
    }
    
    void memory_recovery(int waitingmemory,int totalmemory,String [] S,ArrayList<JTextField> memory) {
	totalmemory -= Integer.parseInt(S[4]);
	boolean havechange = false;
	for(int x = 0 ; x < memory.size() ; x ++) {	    				
		//ѡ���ڴ�ռ��б�����Ľ���
		if(memory.get(x).getText().equals(S[0])) {
			if(!havechange) {
				//���(��ʾΪ��ɫ)
				memory.get(x).setBackground(Color.WHITE);
				memory.get(x).setText("");
				
				/***���յ��������***/
				
				record[x] = x + waitingmemory - 1; //��¼
				
				//�����һ���ڴ�ǿ�
				if(x == 0||((x-1)>=0 &&!memory.get(x-1).getText().equals(""))) {
					
					//�����һ��Ҳ���ǿ��е�
					if(x==39||(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals(""))) {
						
						record[x]=record[x];//�����޸�
					}	    		    					
					//�����һ���ǿ��е�
					else if(1 + record[x] < memory.size() && memory.get(1 + record[x]).getText().equals("")) {
						
						int z = record[x]+1;
						//�ϲ����з���
    					while(memory.get(z).getText().equals("")) {
    						z++;	    		    						
    					}
    					record[x] = z-1;
						record[x] = record[1+record[x]];

						//�ռӵ�
						if(1+record[x]<memory.size()) {
							record[1+record[x]] = 0;//=0����
						}
						
					}
				}
				//�����һ���ǿ��е�
				else if((x-1) >= 0 && memory.get(x-1).getText().equals("")) {
					//
					int z = x-1;
					while(z >= 0 && /*z <= 0 &&*/    memory.get(z).getText().equals("")) {
						z--;
						if(z == 0) {
							break;
						}
					}
					//
					if(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals("")) {
						record[z+1]=record[x];
						System.out.println((z+1)+"-"+record[x]+"Ϊ�����ڴ�");
						//�ռӵ�
						record[x]=0;
					}
					//�����һ���ǿ��еģ�Ҫ�ϲ�
					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
						
						record[z+1]=record[1+record[x]];
						System.out.println((z+1)+"-"+record[z+1]+"Ϊ�����ڴ�");
					}	    		    					
				}	    		    				
					    		    				
				havechange =true;
			}
			
			memory.get(x).setBackground(Color.WHITE);
			memory.get(x).setText("");
			record[x]=0;
			}
		}
	}
}
