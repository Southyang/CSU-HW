package Experiment;
import java.awt.*; 
import javax.swing.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.util.ArrayList;
import java.util.Vector;

@SuppressWarnings("unused") //表示该属性在方法或类中没有使用。添加此注解可以去除属性上的黄色警告
public class schedule extends Thread{  //继承thread类开发多线程
	private int flag = 0;
	/***添加界面控件***/
	private JFrame frm; //建立GUI容器
	private JScrollPane reserveSp,readySp,waitingSp,blockedSp,finishSp; //带有滚动条的面板
	@SuppressWarnings("rawtypes") //忽略指定的警告
	private JList reserveList, readyList, waitingList, blockedList, finishList;
	
	@SuppressWarnings("rawtypes") //忽略指定的警告
	private DefaultListModel reserveLm, readyLm, waitingLm, blockedLm, finishLm;
	
	/***定义标签***/
	private JTextArea explainTa = new JTextArea("欢迎使用本进程调度模拟系统！\n"
			+"说明：\n"
			+ "1.进程信息为 ：进程名，要求运行时间，优先权，前驱，所需内存大小\n"
			+ "2.前驱为0时代表该进程为独立进程，否则代表前驱进程的进程名\n"
			+ "3.本程序使用抢占式优先级进程调度算法和首次适应内存分配算法");
	private JLabel tip = new JLabel("进程调度模拟系统");
	private JLabel reserveLbl = new JLabel("后备队列");
	private JLabel readyLbl = new JLabel("就绪队列");
	private JLabel twocpuLb = new JLabel("双处理机调度情况");
	private JLabel cpu1Lb = new JLabel("CPU1");
	private JLabel cpu2Lb = new JLabel("CPU2");
	private JLabel waitingLbl = new JLabel("挂起队列");
	private JLabel blockedLbl = new JLabel("阻塞队列");
	private JLabel finishLbl = new JLabel("完成队列");
	
	private JLabel createin = new JLabel("创建进程");
	private JLabel needTimeLbl = new JLabel("要求运行时间");
	private JLabel priorityLbl = new JLabel("优先权");
	private JLabel previousLbl = new JLabel("前驱");
	private JLabel memoryLbl = new JLabel("运行所需内存");
	private JLabel system = new JLabel("系统操作");
	private JLabel tipsLbl = new JLabel("提示：");
	private JLabel tipsContentLbl = new JLabel();//提示内容
	
	private JTextField cpu1TF = new JTextField(20); //轻量级组件，允许编辑单行文本
	private JTextField cpu2TF = new JTextField(20);
	
	@SuppressWarnings("rawtypes")
	private JComboBox needTimeCb, priorityCb,previousCb,memorysizeCb; //添加下拉列表组件
	private JButton addBtn, suspend1Btn, suspend2Btn, unsuspendBtn, pauseBtn,exitBtn,goonBtn; //添加按钮组件
	
	
	/***初始化参数***/
	private int number = 1;   //进程名序号(初始设为1,每添加一个进程就+1)
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
	
	private String b1PriorityProcess = "";//CPU1内被阻塞的进程	
	private String b1PriorityProcessId = "";
	private String b1PriorityProcessTime = "";   
	private String b1PriorityProcessPriority = "0";
	private String b1PriorityProcessPrevious = "0";
	private String b1PriorityProcessMemory = "0";
	
	private String b2PriorityProcess = "";//CPU2内被阻塞的进程
	private String b2PriorityProcessId = "";
	private String b2PriorityProcessTime = "";   
	private String b2PriorityProcessPriority = "0";
	private String b2PriorityProcessPrevious = "0";
	private String b2PriorityProcessMemory = "0";
	
	/***定义面板***/
	JPanel memoryPl,addrPl,cpuPl;
	
	ArrayList<JTextField> memory = new ArrayList<JTextField>();
	ArrayList<JTextField> addrNum = new ArrayList<JTextField>();
	int [] record=new int[40];//内存空间占用记录
	int totalmemory=0;
	
	/***其他参数变量***/
	private String secpri;//暂存第二位的优先级值(用于双处理机调度)
	
	@SuppressWarnings("rawtypes")
	private Vector waitingReadyV = new Vector(1,1);//满足运行条件的后备队列进程(前驱已完成)
	
	//定义字体
	private Font fontLblAcc = new Font("楷体_GB2312",Font.BOLD|Font.ITALIC,13);
	private Font fontLblAcc2 = new Font("楷体_GB2312",Font.BOLD|Font.ITALIC,18);
	private Font fontLblAcc1 = new Font("华文行楷",Font.BOLD|Font.ITALIC,20);
	private Font fontLblAcc3 = new Font("楷体_GB2312",Font.BOLD|Font.ITALIC,13);
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public schedule() 
    //构造函数
    {
    	/***界面化***/
    	frm = new JFrame("操作系统实验");
    	Container box = frm.getContentPane();
    	
    	/***布局***/
    	box.setBackground(Color.white);
    	box.setLayout(null);  //设置为null即为清空布局管理器，之后添加组件
    	
    	/***标题***/
    	tip.setBounds(300,0,200,30);
    	tip.setFont(fontLblAcc1);
    	box.add(tip);
    	
    	/***提示***/
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
    	
    	
    	/***控件定位***/
    	//后备队列
    	reserveLbl.setBounds(20,135,60,30); //设置后备队列位置和大小
    	reserveLbl.setFont(fontLblAcc);
    	box.add(reserveLbl); 
    	reserveLm = new DefaultListModel();
    	reserveList = new JList(reserveLm);
    	reserveSp = new JScrollPane(reserveList);  //带滚动条的面板
    	reserveSp.setBounds(5,165,100,180);	
    	box.add(reserveSp);
    	
    	//就绪队列
    	readyLbl.setBounds(145,135,60,30);
    	readyLbl.setFont(fontLblAcc);
    	box.add(readyLbl);
    	readyLm = new DefaultListModel();
    	readyList = new JList(readyLm);
    	readySp = new JScrollPane(readyList);  //带滚动条的面板
    	readySp.setBounds(130,165,100,180);	
    	box.add(readySp);
    	
    	//阻塞队列
    	blockedLbl.setBounds(270,135,60,30);
    	blockedLbl.setFont(fontLblAcc);
    	box.add(blockedLbl);
    	blockedLm = new DefaultListModel();
    	blockedList = new JList(blockedLm);//  将waitingLm中的列表数据放入列表框里
    	blockedSp = new JScrollPane(blockedList);
    	blockedSp.setBounds(255,165,100,180);
    	box.add(blockedSp);
    	
    	//挂起队列
    	waitingLbl.setBounds(395,135,60,30);
    	waitingLbl.setFont(fontLblAcc);
    	box.add(waitingLbl);
    	waitingLm = new DefaultListModel();
    	waitingList = new JList(waitingLm);//  将waitingLm中的列表数据放入列表框里
    	waitingSp = new JScrollPane(waitingList);
    	waitingSp.setBounds(380,165,100,180);
    	box.add(waitingSp);
    	
    	//完成队列
    	finishLbl.setBounds(520,135,60,30);
    	finishLbl.setFont(fontLblAcc);
    	box.add(finishLbl);
    	finishLm = new DefaultListModel();
    	finishList = new JList(finishLm);
    	finishSp = new JScrollPane(finishList);
    	finishSp.setBounds(505,165,100,180);
    	box.add(finishSp);
    	
    	//创建进程并添加
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
    	
    	addBtn = new JButton("添加进程");
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

        //系统操作 system
        system.setBounds(450,350,160,50);
        system.setFont(fontLblAcc2);
    	box.add(system);
    	suspend1Btn = new JButton("挂起就绪");
    	suspend1Btn.setBounds(425,405,100,40);
    	box.add(suspend1Btn);
    	
    	suspend2Btn = new JButton("挂起阻塞");
    	suspend2Btn.setBounds(530,405,100,40);
    	box.add(suspend2Btn);
    	
    	unsuspendBtn = new JButton("解挂");
    	unsuspendBtn.setBounds(425,450,100,40);
    	box.add(unsuspendBtn);
    	
    	exitBtn = new JButton("退出");
    	exitBtn.setBounds(530,450,100,40);
    	exitBtn.setBackground(Color.red);
    	box.add(exitBtn);
    	
    	pauseBtn = new JButton("暂停");
    	pauseBtn.setBounds(425,495,100,40);
    	box.add(pauseBtn); 
    		
    	goonBtn = new JButton("继续运行");
    	goonBtn.setBounds(530,495,100,40);
    	box.add(goonBtn); 
    	
    	/***内存模拟部分***/
    	JLabel memoryin = new JLabel("内存分配情况");
    	memoryin.setBounds(650,30,150,30);
    	memoryin.setFont(fontLblAcc2);
    	box.add(memoryin);
    	memoryPl = new JPanel(new GridLayout(41,1,20,0));
    	addrPl = new JPanel(new GridLayout(41,1,20,0));
        memoryPl.add(new JLabel("内存容量为40"));
        addrPl.add(new JLabel("地址"));
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
        memory.get(1).setText("内核占据");
        memory.get(2).setText("内核占据");
        memory.get(0).setText("内核占据");

 
        /***Button事件注册***/     //后方有按键定义
    	ButtonListener btnListener = new ButtonListener();
    	addBtn.addActionListener(btnListener); //添加指定的动作侦听器,以接收发自此按钮的动作事件。
    	suspend1Btn.addActionListener(btnListener);
    	suspend2Btn.addActionListener(btnListener);
    	unsuspendBtn.addActionListener(btnListener);
    	pauseBtn.addActionListener(btnListener);
    	exitBtn.addActionListener(btnListener);
    	goonBtn.addActionListener(btnListener);
    	
    	/***调度器线程开启***/
    	this.start();
    	frm.setSize(800,700); 
    	frm.setVisible(true);  
    	frm.setLocationRelativeTo(null);
    	frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //退出
    	}
    

    @SuppressWarnings({ "unchecked", "static-access" })
	public synchronized void run()
    {
    	try 
    	{
    		while(true)
    		while (flag==0)
	    	{
    			//标志前驱是否完成
    			boolean havepre1 = false;
    			boolean havepre2 = false;
    			
/***判断挂起队列是否有进程在等待进就绪队列(cpu1,cpu2)***/
	    		while (readyLm.size()<6 && waitingReadyV.size() != 0){//当挂起队列有进程在等待进就绪队列(被解挂)
	    			String s = waitingReadyV.get(0).toString();
	    			readyLm.addElement(s);	//将该进程加入就绪队列		
	    			this.sleep(1500);
	    			waitingReadyV.remove(0);
	    			waitingLm.removeElement(s);
	    			
	    		}
    			
/***判断阻塞队列能否进就绪队列***/
	    		if (!blockedLm.isEmpty()&&!finishLm.isEmpty()) {
	    			//若阻塞队列非空且有进程完成
	    			String blockedInfo[];
	    			String finishInfo[];
	    			String tempProcess1 = "";
	    			
	    			for (int i = blockedLm.size() - 1 ; i >= 0 ; i--) {
	    				blockedInfo = blockedLm.getElementAt(i).toString().split(","); //阻塞进程信息
	    				for (int j = finishLm.size() - 1 ; j >= 0  ; j--) 
	    				{
	    					finishInfo = finishLm.getElementAt(j).toString().split(",");//完成进程信息
	    					if(Integer.parseInt(blockedInfo[3]) == Integer.parseInt(finishInfo[0])) 
	    						//如果阻塞进程的前驱已完成
	    					{
	    						
	    						tempProcess1 =blockedInfo[0]+","+blockedInfo[1]+","+blockedInfo[2]+","+blockedInfo[3]+","+blockedInfo[4];		    						
	    						readyLm.addElement(tempProcess1);//该进程进就绪队列
	    						blockedLm.removeElementAt(i);    						
	    						this.sleep(1500);
	    					}
	    				}
	    			}
	    		}
	    		
    			
    			
/***判断后备队列进程能否进就绪队列***/
	    		String a[];
	    		int temp;	
	    		
    			//如果后备队列非空且内存空间足够
	    		while ((!reserveLm.isEmpty())&&(totalmemory <= memory.size()-4)){
	    				a = reserveLm.getElementAt(0).toString().split(","); 
		    			totalmemory += Integer.parseInt(a[4]);
		    			
		    			//每个进程进内存之前都要确认
		    			if(totalmemory <= memory.size()-4) {
		    				for(int x = 0 ; x < memory.size() ; x ++) {
		    					
		    					//如果一块连续的内存空间放得下这个进程
		    					if((record[x]-x+1)>=Integer.parseInt(a[4])){
		    						readyLm.addElement(reserveLm.getElementAt(0));//该进程进就绪队列
		    		    			reserveLm.removeElementAt(0);
		    		    			temp=record[x];
		    		    			
		    		    			//显示内存空间的占用情况
		    		    			for(int y = x ; y < x + Integer.parseInt(a[4]) ; y++) {
		    		    				memory.get(y).setBackground(Color.YELLOW);
		    		    				memory.get(y).setText(a[0]);
		    		    				record[y] = -1;
		    		    			}
		    		    			
		    		    			//标记出新的空闲区域的起始位置
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
	    		
	    		/***就绪队列中的进程按优先级排序，找出优先级最大的进入CPU运行***/
	    		//若就绪队列非空
	    		if (!readyLm.isEmpty()){
	    			String[] tempProcess2 = {" "};
	    		
	    			//String[] l;
	    			boolean unblocked1 = false;
	    			boolean unblocked2 = false;

	    			for (int i = readyLm.size()-1; i >= 0 ; i --)
	    			{
	    				tempProcess2 = readyLm.getElementAt(i).toString().split(","); 
	    				
	    				//如果该进程优先权大于cpu 1内进程的优先权
	    				if (Integer.parseInt(tempProcess2[2]) >= Integer.parseInt(cpu1ProcessPriority)){
	    					
	    					//如果该进程无前驱
	    					if(Integer.parseInt(tempProcess2[3]) == 0){
	    						secpri=cpu1ProcessPriority;//暂存
	    					    maxPriorityProcess = readyLm.getElementAt(i).toString();
	    					    maxPriorityProcessId = tempProcess2[0];
	    					    maxPriorityProcessTime = tempProcess2[1];
	    					    maxPriorityProcessPriority = tempProcess2[2];
	    					    maxPriorityProcessPrevious = tempProcess2[3];
	    					    maxPriorityProcessMemory = tempProcess2[4];
	    					    cpu1ProcessPriority = maxPriorityProcessPriority; 
	    					}
	    						    					
	    					//如果该进程有前驱
		    				else if(Integer.parseInt(tempProcess2[3])!=0) 
		    				{
		    					String[] finishInfo;
		    					
		    					//如果完成队列无进程
			    				if(finishLm.size()==0) {
			    					
			    					//说明该进程的前驱未完成
		    						havepre1 = true; 
		    						
		    						//该进程进阻塞队列
		    						b1PriorityProcess = readyLm.getElementAt(i).toString();
		    						blockedLm.addElement(readyLm.getElementAt(i));
		    						readyLm.remove(i);
    		    					b1PriorityProcessId = tempProcess2[0];
    		    					b1PriorityProcessTime = tempProcess2[1];
    		    					b1PriorityProcessPriority = tempProcess2[2];
    		    					b1PriorityProcessPrevious = tempProcess2[3];
    		    					b1PriorityProcessMemory = tempProcess2[4];
		    					}
			    				//否则(完成队列有进程)
		    					else 
		    					{
		    						for (int j = finishLm.size()-1; j >= 0; j--) 
		    	    				{
		    	    					finishInfo = finishLm.getElementAt(j).toString().split(",");
		    	    					//如果该进程有前驱但前驱已完成
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
		    							//如果前驱未完成
		    					        havepre1 = true;
		    					        //该进程进入阻塞队列
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
	    		
    		
    			
	    		
	    			    		
	    		/***优先级抢占***/
	    		
	    		//如果当前CPU1里的进程的优先级不是最大的    			
	    			if (!maxPriorityProcessId.equals(cpu1ProcessId)){
	    				//如果最大优先级不等于0
		    			if(Integer.parseInt(maxPriorityProcessPriority)!=0) {
		    				cpu1Process = maxPriorityProcess;
			    			cpu1ProcessId = maxPriorityProcessId;
			    			cpu1ProcessTime = maxPriorityProcessTime;
			    			cpu1ProcessPriority = maxPriorityProcessPriority;
			    			cpu1ProcessPrevious = maxPriorityProcessPrevious;
			    			cpu1ProcessMemory = maxPriorityProcessMemory;
		    			}
	    				//如果最大优先级等于0
		    			else if(Integer.parseInt(maxPriorityProcessPriority) == 0){
		    				for (int i = readyLm.size()-1; i >= 0; i--){
		    					String readyInfo[];
		    					tempProcess2 = readyLm.getElementAt(i).toString().split(",");
		    					readyInfo = readyLm.getElementAt(0).toString().split(",");
		    					cpu1ProcessId = readyInfo[0];
		    					
		    					//如果就绪队列有进程序号在前的(先来先服务)
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
	    				
	    				//先把cpu2清空
	    				cpu2Process ="";
	    				cpu2ProcessId = "";
	    				cpu2ProcessTime = "0";
	    				cpu2ProcessPriority = "0";
	    				cpu2ProcessPrevious = "0";
	    				cpu2ProcessMemory="0";
	    				
	    				//如果就绪队列的进程大于等于第二优先级 且 小于等于第一优先级 且 不等于当前cpu1内的进程(避免进程重复进CPU)
	    				if (Integer.parseInt(tempProcess2[2]) >= (Integer.parseInt(secpri)) 
	    						&&Integer.parseInt(tempProcess2[2]) <= Integer.parseInt(maxPriorityProcessPriority)
	    						&&!tempProcess2[0].equals(cpu1ProcessId) ){
	    					
	    					//如果该进程无前驱
	    					if( Integer.parseInt(tempProcess2[3])==0){
	    						secPriorityProcess = readyLm.getElementAt(i).toString();//就绪队列内进程（0）为第二优先级
		    					secPriorityProcessId = tempProcess2[0];
		    					secPriorityProcessTime = tempProcess2[1];
		    					secPriorityProcessPriority = tempProcess2[2];
		    					secPriorityProcessPrevious = tempProcess2[3];
		    					secPriorityProcessMemory = tempProcess2[4];
		    					cpu2ProcessPriority = secPriorityProcessPriority;//该进程进cpu2

		    					secpri = tempProcess2[2];
		    					flag2=true;
	    					}
	    					
	    					//如果就绪队列内进程有前驱
	    					else if(Integer.parseInt(tempProcess2[3])!=0) 
		    				{
		    					String[] finishInfo;
			    				
		    					//如果完成队列为空
			    				if(finishLm.size()==0){
		    						havepre2 = true;  //有前驱且前驱未完成
		    						b2PriorityProcess = readyLm.getElementAt(i).toString();//该进程被阻塞
    		    					b2PriorityProcessId = tempProcess2[0];
    		    					b2PriorityProcessTime = tempProcess2[1];
    		    					b2PriorityProcessPriority = tempProcess2[2];
    		    					b2PriorityProcessPrevious = tempProcess2[3];
    		    					b2PriorityProcessMemory = tempProcess2[4];
		    					}
			    				//如果完成队列非空
		    					else {
		    						for (int j = finishLm.size()-1; j >= 0; j--){
		    	    					finishInfo = finishLm.getElementAt(j).toString().split(",");
		    	    					
		    	    					//如果该进程的前驱已完成
		    	    					if(Integer.parseInt(tempProcess2[3]) == Integer.parseInt(finishInfo[0])){
		    	    						
		    	    						unblocked2=true;
		    	    						secPriorityProcess = readyLm.getElementAt(i).toString();
		    		    					secPriorityProcessId = tempProcess2[0];
		    		    					secPriorityProcessTime = tempProcess2[1];
		    		    					secPriorityProcessPriority = tempProcess2[2];
		    		    					secPriorityProcessPrevious = tempProcess2[3];
		    		    					secPriorityProcessMemory = tempProcess2[4];
		    		    					cpu2ProcessPriority = secPriorityProcessPriority;//该进程进cpu2
		    		    					flag2=true;
		    	    					}
		    	    				}
		    						//有前驱且未被挂起
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
	    			
	    			//如果第二优先级不等于cpu2内进程的优先级 且 cpu2内进程满足运行条件
	    			if (!secPriorityProcessId.equals(cpu2ProcessId) && flag2){
		    			
	    				//如果第二优先级不等于0
	    				if(Integer.parseInt(secPriorityProcessPriority)!=0) {
	    					cpu2Process = secPriorityProcess;//该进程进cpu2(优先级抢占)
			    			cpu2ProcessId = secPriorityProcessId;
			    			cpu2ProcessTime = secPriorityProcessTime;
			    			cpu2ProcessPriority = secPriorityProcessPriority;
			    			cpu2ProcessPrevious = secPriorityProcessPrevious;
			    			cpu2ProcessMemory = secPriorityProcessMemory;
	    				}
	    				//如果第二优先级等于0
		    			else if(Integer.parseInt(secPriorityProcessPriority)==0) {
		    				for (int i = readyLm.size()-1; i>=0; i--){
		    					String readyInfo[];
		    					tempProcess2 = readyLm.getElementAt(i).toString().split(",");
		    					readyInfo = readyLm.getElementAt(0).toString().split(",");
		    					cpu2ProcessId = readyInfo[0];//先来先服务
			    				if (Integer.parseInt(tempProcess2[0]) < Integer.parseInt(cpu2ProcessId)&&!tempProcess2[0].equals(cpu1ProcessId) )
			    				{
			    					//如果优先权=0
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
	    			    	    		
	    		//时间片到了，CPU上的进程转到就绪队列或完成队列
	    		boolean cpuTorlbl = false;//cpu到就绪
	    		boolean cpuToflbl = false;//cpu到完成
	    		if (!cpu1TF.getText().equals("") )
	    			//如果cpu1不为空
	    		{

	    			cpu1TF.setText("");//把cpu1清空
	    			int time = Integer.parseInt(cpu1ProcessTime);
	    			cpu1ProcessTime = String.valueOf(--time);
		    		if (Integer.parseInt(cpu1ProcessPriority) > 0) //若优先级大于0则减1
	    			{

	    				int priority = Integer.parseInt(cpu1ProcessPriority);
	    				cpu1ProcessPriority = String.valueOf(--priority);
		    				
	    			}
	    	
		    		if (Integer.parseInt(cpu1ProcessTime) > 0) //要求运行时间大于0则进入ready队列
		    		{
		    			cpu1Process = cpu1ProcessId + "," + cpu1ProcessTime + "," + cpu1ProcessPriority + "," + cpu1ProcessPrevious+ "," + cpu1ProcessMemory;		    			
		    			readyLm.addElement(cpu1Process);	
		    			cpuTorlbl = true;	
		    			cpu1TF.setText("");   //上面已经置空一次了
		    			cpu1ProcessPriority = "0"; 
		    		}
		    		else   //要求运行时间为0则转到完成队列
		    		{
		    			cpu1ProcessTime = String.valueOf(0);
		    			cpu1ProcessPriority = String.valueOf(0);
		    			cpu1Process = cpu1ProcessId + "," + cpu1ProcessTime + "," + cpu1ProcessPriority+ "," + cpu1ProcessPrevious+ "," + cpu1ProcessMemory;
		    			cpuToflbl = true;
		    			finishLm.addElement(cpu1Process);
		    			int cpu1memory=Integer.parseInt(cpu1ProcessMemory);
		    			
		    			/***内存空间回收***/
		    			totalmemory -= Integer.parseInt(cpu1ProcessMemory);
		    			boolean havechange=false;
		    			for(int x = 0 ; x < memory.size() ; x ++) {
		    				if(memory.get(x).getText().equals(cpu1ProcessId)) {
		    					//record[x]=x+cpu1memory-1;
		    					if(!havechange) {
		    						
		    						memory.get(x).setBackground(Color.WHITE);
	    		    				memory.get(x).setText("");
	    		    				
	    		    				//这里要有回收的四种情况
	    		    				//如果上一块不是空闲的或者这一块是0
	    		    				record[x]=x+cpu1memory-1;
	    		    				if(x==0||((x-1)>=0&&!memory.get(x-1).getText().equals(""))) {
	    		    					//如果下一块也不是空闲的
	    		    					if(x==39||(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals(""))) {
	    		    						//如果下一个也不为空，那么record[x]就不用变了
	    		    						record[x]=record[x];
	    		    						System.out.println(x+"-"+record[x]+"为空闲内存");
	    		    						//record[x]=record[1+record[x]];
	    		    					}
	    		    						    		    					
	    		    					//如果下一块是空闲的，那么要合并回收
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						record[x]=record[1+record[x]];
	    		    						System.out.println(x+"-"+record[x]+"为空闲内存");
	    		    						//刚加的
	    		    						if(1+record[x]<memory.size()) {
	    		    							record[1+record[x]]=0;
	    		    						}
	    		    						
	    		    					}
	    		    				}
	    		    				//如果上一块是空闲的
	    		    				else if((x-1)>=0&&memory.get(x-1).getText().equals("")) {
	    		    					int z=x-1;
	    		    					while(z>=0&&z<=0&&memory.get(z).getText().equals("")) {
	    		    						z--;
	    		    					}
	    		    					if(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals("")) {
	    		    						record[z+1]=record[x];
	    		    						System.out.println((z+1)+"-"+record[x]+"为空闲内存");
	    		    						//刚加的
	    		    						record[x]=0;
	    		    					}
	    		    					//如果下一块是空闲的，要合并
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						
	    		    						record[z+1]=record[1+record[x]];
	    		    						System.out.println((z+1)+"-"+record[z+1]+"为空闲内存");
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
	    		
	    		if (!cpu2TF.getText().equals("") && !cpu2ProcessId.equals(""))  //这里加了个条件
	    		{

	    			cpu2TF.setText("");
	    			int time = Integer.parseInt(cpu2ProcessTime);
	    			cpu2ProcessTime = String.valueOf(--time);
		    		if (Integer.parseInt(cpu2ProcessPriority) > 0) //若优先级大于0则减1
	    			{

	    				int priority = Integer.parseInt(cpu2ProcessPriority);
	    				cpu2ProcessPriority = String.valueOf(--priority);
		    			
	    			}
		    			    	
		    		if (Integer.parseInt(cpu2ProcessTime) > 0) //要求运行时间大于0则进入ready队列
		    		{
		    			cpu2Process = cpu2ProcessId + "," + cpu2ProcessTime + "," + cpu2ProcessPriority+ "," + cpu2ProcessPrevious+ "," + cpu2ProcessMemory;		    			
		    			readyLm.addElement(cpu2Process);
		    			cpuTorlbl = true;			
		    			cpu2TF.setText("");
		    			cpu2ProcessId="";
		    			cpu2ProcessPriority = "0"; 
		    		}
		    		else  //要求运行时间为0则转到完成队列
		    		{
		    			cpu2ProcessTime = String.valueOf(0);
		    			cpu2ProcessPriority = String.valueOf(0);
		    			cpu2Process = cpu2ProcessId + "," + cpu2ProcessTime + "," + cpu2ProcessPriority+ "," + cpu2ProcessPrevious+ "," + cpu2ProcessMemory;	    			
		    			cpuToflbl = true;
		    			finishLm.addElement(cpu2Process);
		    			int cpu2memory=Integer.parseInt(cpu2ProcessMemory);
		    			
		    			//回收内存空间
		    			totalmemory-=Integer.parseInt(cpu2ProcessMemory);
		    			boolean havechange=false;
		    			for(int x=0;x<memory.size();x++) {
		    				if(memory.get(x).getText().equals(cpu2ProcessId)) {
		    					//record[x]=x+cpu2memory-1;
		    					if(!havechange) {
		    						memory.get(x).setBackground(Color.WHITE);
	    		    				memory.get(x).setText("");
	    		    				record[x]=x+cpu2memory-1;
	    		    				//如果上一块不是空闲的或者这一块是0
	    		    				if(x==0||((x-1)>=0&&!memory.get(x-1).getText().equals(""))) {
	    		    					//如果下一块也不是空闲的或者下一块是第39号
	    		    					if(x==39||(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals(""))) {
	    		    						//如果下一个也不为空，那么record[x]就不用变了
	    		    						record[x]=record[x];
	    		    						System.out.println(x+"-"+record[x]+"为空闲内存");
	    		    						//record[x]=record[1+record[x]];
	    		    					}
	    		    						    		    					
	    		    					//如果下一块是空闲的，那么要合并回收
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						int z=record[x]+1;
		    		    					while(memory.get(z).getText().equals("")) {
		    		    						z++;	    		    						
		    		    					}
		    		    					record[x]=z-1;
		    		    					System.out.println(x+"-"+record[x]+"为空闲内存");
	    		    						//record[x]=record[1+record[x]];
	    		    						//刚加的
	    		    						//record[1+record[x]]=0;
		    		    					if(1+record[x]<memory.size()) {
	    		    							record[1+record[x]]=0;//记录清零
	    		    						}
	    		    					}
	    		    				}
	    		    				//如果上一块是空闲的
	    		    				else if((x-1)>=0&&memory.get(x-1).getText().equals("")) {
	    		    					int z=x-1;
	    		    					while(z>=0&&memory.get(z).getText().equals("")) {
	    		    						z--;
	    		    					}
	    		    					if(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals("")) {
	    		    						record[z+1]=record[x];
	    		    						System.out.println((z+1)+"-"+record[x]+"为空闲内存");
	    		    						//刚加的
	    		    						record[x]=0;
	    		    					}
	    		    					//如果下一块是空闲的，要合并
	    		    					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
	    		    						
	    		    						record[z+1]=record[1+record[x]];
	    		    						System.out.println((z+1)+"-"+record[z+1]+"为空闲内存");
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
	    		
	    		//重新统计一下空内存块
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
   
    					
     /***按钮监听***/
    class ButtonListener implements ActionListener{
    	@SuppressWarnings("unchecked")
		public synchronized void actionPerformed(ActionEvent ae){
    		JButton source = (JButton)ae.getSource();  //返回事件源
    		if (source == addBtn)          //增加进程
    		{
    			String time = "1";//设置初始值
    			time = needTimeCb.getSelectedItem().toString();
    			String priority = "1";//设置初始值
    			priority = priorityCb.getSelectedItem().toString();
    			String pid = String.valueOf(number);
    			String previous = "0";//设置初始值
    			previous = previousCb.getSelectedItem().toString();
    			String memorysize = "1";//设置初始值
    			memorysize = memorysizeCb.getSelectedItem().toString();
    			
    			//如果进程名等于前驱
    			if(pid.equals(previous)) {
    				tipsContentLbl.setText("不能把自己设为前驱！");	
    			}
    			//否则(进程名不等于前驱)
    			else {
    			String reserveItem = "";
    			reserveItem = pid + "," + time + "," + priority +  "," + previous + "," + memorysize;
    			reserveLm.addElement(reserveItem);//把该进程添加到后备队列
    			number ++;
    			tipsContentLbl.setText("");
    			}
    		}
    		else if (source == suspend1Btn){ //挂起就绪
    			 String suspendProcess = ""; 
    			 String S[]; 
    			 //如果已选择
     			if (readyList.getSelectedValue()!= null){
     				suspendProcess = readyList.getSelectedValue().toString();
     				S = readyList.getSelectedValue().toString().split(",");
     				waitingLm.addElement(suspendProcess);//将该进程添加到挂起队列
     				readyLm.removeElement(suspendProcess);//将该进程从就绪队列移除
     				
     				int waitingmemory = Integer.parseInt(S[4]);
     				
    				/***回收内存空间***/
     				memory_recovery(waitingmemory,totalmemory,S,memory);
	    			
     				
     				tipsContentLbl.setText("");        
     				System.out.println("挂起成功" ); /////
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
     				tipsContentLbl.setText("选择一个需挂起的进程");
     			}
    		}
    		else if (source == suspend2Btn) //挂起阻塞
    		{
    			System.out.println("点击挂起" ); /////
    			
    			String suspendProcess = ""; 
   			    String S[];
    			if (blockedList.getSelectedValue()!= null)
    			{
    				suspendProcess = blockedList.getSelectedValue().toString();
    				S=blockedList.getSelectedValue().toString().split(",");
    				waitingLm.addElement(suspendProcess);
    				blockedLm.removeElement(suspendProcess);
    				
    				int waitingmemory=Integer.parseInt(S[4]);
    				//回收内存空间
    				memory_recovery(waitingmemory,totalmemory,S,memory);
	    			
    				
    				tipsContentLbl.setText("");        
    				System.out.println("挂起成功" ); /////
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
    				tipsContentLbl.setText("选择一个需挂起的进程");
    			}
    		}
    		
    		else if (source == unsuspendBtn)  //解挂进程
    		{    			
    			System.out.println("点击解挂" ); /////
    			String unsuspendProcess = "";
    			
    			if (waitingList.getSelectedValue()!= null)
    			{
    				unsuspendProcess = waitingList.getSelectedValue().toString();
    				waitingReadyV.add(unsuspendProcess);

    				tipsContentLbl.setText("");     				
    				//分配内存
    				String [] a = new String[10];
    				//dataType[] arrayRefVar = new dataType[arraySize];
    				int temp;
    				a = unsuspendProcess.split(","); 
	    			totalmemory += Integer.parseInt(a[4]);
	    			//开始在内存块里找位置
	    			boolean canput=false;
	    			if(totalmemory <= memory.size()-4) {
	    				
	    				for(int x = 0;x < memory.size() -1; x++) {
	    					//如果一块连续的内存空间放得下这个进程，就进就绪队列
	    					if((record[x]-x+1)>=
	    							Integer.parseInt(a[4])) {
	    		    			canput=true;
	    						temp=record[x];	    		    			
	    		    			for(int y=x;y<x+Integer.parseInt(a[4]);y++) {
	    		    				memory.get(y).setBackground(Color.YELLOW);
	    		    				memory.get(y).setText(a[0]);
	    		    				record[y]=-1;
	    		    			}
	    		    			//标记出新的空闲区域的起始位置
	    		    			if(x+Integer.parseInt(a[4])<=memory.size()) {
	    		    				System.out.println(x+"-"+(x+Integer.parseInt(a[4])-1)+"被占用");
	    		    				if((x+Integer.parseInt(a[4]))<=39) {
	    		    					record[x+Integer.parseInt(a[4])]=temp;
		    		    				System.out.println(x+Integer.parseInt(a[4])+"-"+temp+"为空闲内存");
	    		    				}	    		    				
	    		    			}
	    		    			System.out.println("解挂成功" ); /////	
	    		    			break;
	    					}
	    					//如果没有合适大小的，就直接跳到下一块连续的内存块
	    					else {
	    						x=record[x]-1;
	    					}
	    				}
	    			}
	    			if(!canput) {
	    				System.out.println("解挂失败" );
	    				tipsContentLbl.setText("没有足够的内存");
	    			}
    				
    				
	    			
    			}
    			else 
    			{
    				tipsContentLbl.setText("选择一个需解挂进程");
    			}
    		}
    	
    		else if(source == pauseBtn) {
    			flag = 1;
    		}
    		
    		else if (source == exitBtn) //退出程序
    		{
    			System.exit(0);
    		}
    		
    		else if(source == goonBtn) //继续运行
    		{
    			flag = 0;
    		}
    	}
    }
    
    
     // 函数主体 
    public static void main(String args[])
    {
    	new schedule();
    }
    
    void memory_recovery(int waitingmemory,int totalmemory,String [] S,ArrayList<JTextField> memory) {
	totalmemory -= Integer.parseInt(S[4]);
	boolean havechange = false;
	for(int x = 0 ; x < memory.size() ; x ++) {	    				
		//选中内存空间中被挂起的进程
		if(memory.get(x).getText().equals(S[0])) {
			if(!havechange) {
				//清空(显示为白色)
				memory.get(x).setBackground(Color.WHITE);
				memory.get(x).setText("");
				
				/***回收的四种情况***/
				
				record[x] = x + waitingmemory - 1; //记录
				
				//如果上一块内存非空
				if(x == 0||((x-1)>=0 &&!memory.get(x-1).getText().equals(""))) {
					
					//如果下一块也不是空闲的
					if(x==39||(1+record[x]<memory.size()&&!memory.get(1+record[x]).getText().equals(""))) {
						
						record[x]=record[x];//不用修改
					}	    		    					
					//如果下一块是空闲的
					else if(1 + record[x] < memory.size() && memory.get(1 + record[x]).getText().equals("")) {
						
						int z = record[x]+1;
						//合并空闲分区
    					while(memory.get(z).getText().equals("")) {
    						z++;	    		    						
    					}
    					record[x] = z-1;
						record[x] = record[1+record[x]];

						//刚加的
						if(1+record[x]<memory.size()) {
							record[1+record[x]] = 0;//=0？？
						}
						
					}
				}
				//如果上一块是空闲的
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
						System.out.println((z+1)+"-"+record[x]+"为空闲内存");
						//刚加的
						record[x]=0;
					}
					//如果下一块是空闲的，要合并
					else if(1+record[x]<memory.size()&&memory.get(1+record[x]).getText().equals("")) {
						
						record[z+1]=record[1+record[x]];
						System.out.println((z+1)+"-"+record[z+1]+"为空闲内存");
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
