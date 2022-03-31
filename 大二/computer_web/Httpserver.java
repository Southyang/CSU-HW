package computer_web;
import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Httpserver {

    ServerSocket server;               //��������
    Socket client;                     //������Ŀͻ���
    //���캯��
    Httpserver(){
        try {
            server = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //main ����
    public static void main(String[] args){
         Httpserver myserver = new Httpserver();
         myserver.begin();
    }

    //�ڴ˽��ܿͻ��˵����󣬲�����Ӧ
    private void begin() {
         String httpRequest;
         String urlRequest;
         while(true){
             try {
                 //��ʼ����
                client = this.server.accept(); //���ܿͻ��˵���������,������һ���׽���
                System.out.println("one has connected to this server!!" + client.getLocalPort());
                BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));             
                httpRequest = bf.readLine();
                System.out.println(httpRequest);
                //��ȡ��url��ַ,����ͷ����/index.html �������Ҫ��/ȥ��
                urlRequest = httpRequest.split(" ")[1].substring(1);    
                System.out.println(urlRequest);
                urlRequest = "C:\\Users\\����\\Desktop\\"+urlRequest;
                File file = new File(urlRequest);
                if(file.exists()) {//����Ƿ��и��ļ������򷵻أ����򱨴�404
                	PrintWriter out = new PrintWriter(client.getOutputStream());
                    //����һ��״̬��
                    out.println("HTTP/1.0 200 OK"); 
                    //����һ���ײ�,ָ�����뷽ʽ
                    out.println("Content-Type:text/html;charset=GBK");  
                    // ���� HTTP Э��, ���н�����ͷ��Ϣ  
                    out.println();
                    
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String str = "";
                    while(true) {
                    	String temp = br.readLine();
                    	if(temp == null) {
                    		break;
                    	}
                    	str += "\n"+temp;
                    }
                    fr.close();
                    br.close();
                    // �����ʾ��Ϣ
                    out.println("<h1 style='color: green'> Hello Http Server</h1>");  
                    out.println("���, ����һ�� Java HTTP ������ demo Ӧ��.<br>");  
                    out.println("�������·����: " + urlRequest + "<br></h1>"); 
                    out.println("����Ϊ�ļ����� " + "<br>"); 
                    out.println("<br>"); 
                    // ����ļ����� 
                    out.println(str);
                    
                    out.close();  
                }
                else {
                	PrintWriter out = new PrintWriter(client.getOutputStream());
                    //����һ��״̬��
                    out.println("HTTP/1.0 200 OK"); 
                    //����һ���ײ�,ָ�����뷽ʽ
                    out.println("Content-Type:text/html;charset=GBK");  
                    // ���� HTTP Э��, ���н�����ͷ��Ϣ  
                    out.println();
 
                    // ���������Դ
                    out.println("<h1 style='color: green'> Hello Http Server</h1>");  
                    out.println("���, ����һ�� Java HTTP ������ demo Ӧ��.<br>"); 
                    out.println("�������·����: " + urlRequest + "<br>");
                    out.println("<h1 style='color: black'> 404 Not Found</h1>");  
                        
                    out.close(); 
                }
             } catch (IOException e) {
                e.printStackTrace();
            }
         }
    }
}