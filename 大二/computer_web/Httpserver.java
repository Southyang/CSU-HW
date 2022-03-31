package computer_web;
import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Httpserver {

    ServerSocket server;               //本服务器
    Socket client;                     //发请求的客户端
    //构造函数
    Httpserver(){
        try {
            server = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //main 函数
    public static void main(String[] args){
         Httpserver myserver = new Httpserver();
         myserver.begin();
    }

    //在此接受客户端的请求，并作响应
    private void begin() {
         String httpRequest;
         String urlRequest;
         while(true){
             try {
                 //开始监听
                client = this.server.accept(); //接受客户端的连接请求,并返回一个套接字
                System.out.println("one has connected to this server!!" + client.getLocalPort());
                BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));             
                httpRequest = bf.readLine();
                System.out.println(httpRequest);
                //获取到url地址,请求头中是/index.html ，因此需要将/去掉
                urlRequest = httpRequest.split(" ")[1].substring(1);    
                System.out.println(urlRequest);
                urlRequest = "C:\\Users\\杨昊楠\\Desktop\\"+urlRequest;
                File file = new File(urlRequest);
                if(file.exists()) {//检查是否有该文件，有则返回，无则报错404
                	PrintWriter out = new PrintWriter(client.getOutputStream());
                    //返回一个状态行
                    out.println("HTTP/1.0 200 OK"); 
                    //返回一个首部,指定编码方式
                    out.println("Content-Type:text/html;charset=GBK");  
                    // 根据 HTTP 协议, 空行将结束头信息  
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
                    // 输出提示信息
                    out.println("<h1 style='color: green'> Hello Http Server</h1>");  
                    out.println("你好, 这是一个 Java HTTP 服务器 demo 应用.<br>");  
                    out.println("您请求的路径是: " + urlRequest + "<br></h1>"); 
                    out.println("以下为文件内容 " + "<br>"); 
                    out.println("<br>"); 
                    // 输出文件内容 
                    out.println(str);
                    
                    out.close();  
                }
                else {
                	PrintWriter out = new PrintWriter(client.getOutputStream());
                    //返回一个状态行
                    out.println("HTTP/1.0 200 OK"); 
                    //返回一个首部,指定编码方式
                    out.println("Content-Type:text/html;charset=GBK");  
                    // 根据 HTTP 协议, 空行将结束头信息  
                    out.println();
 
                    // 输出请求资源
                    out.println("<h1 style='color: green'> Hello Http Server</h1>");  
                    out.println("你好, 这是一个 Java HTTP 服务器 demo 应用.<br>"); 
                    out.println("您请求的路径是: " + urlRequest + "<br>");
                    out.println("<h1 style='color: black'> 404 Not Found</h1>");  
                        
                    out.close(); 
                }
             } catch (IOException e) {
                e.printStackTrace();
            }
         }
    }
}