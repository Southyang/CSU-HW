package computer_web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public  class Ping {
     public  static  boolean ping(String ipAddress)  throws Exception {
         int  timeOut =  3000 ;   // ��ʱӦ����3������        
         boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);      //  ������ֵ��trueʱ��˵��host�ǿ��õģ�false�򲻿ɡ�
         return status;
    }
    
     public  static  void ping02(String ipAddress)  throws Exception {
        String line =  null;
         try {
            Process pro = Runtime.getRuntime().exec("ping " + ipAddress);
            BufferedReader buf =  new BufferedReader( new InputStreamReader(
                    pro.getInputStream()));
             while ((line = buf.readLine()) !=  null)
                System.out.println(line);
        }  catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
     public  static  boolean ping(String ipAddress,  int pingTimes,  int timeOut) {  
        BufferedReader in =  null;  
        Runtime r = Runtime.getRuntime();   //  ��Ҫִ�е�ping����,��������windows��ʽ������  
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;  
         try {    //  ִ�������ȡ���  
            System.out.println(pingCommand);   
            Process p = r.exec(pingCommand);   
             if (p ==  null) {    
                 return  false;   
            }
            in =  new BufferedReader( new InputStreamReader(p.getInputStream()));    //  ���м�����,�������Ƴ���=23ms TTL=62�����Ĵ���  
             int connectedCount = 0;   
            String line =  null;   
             while ((line = in.readLine()) !=  null) {    
                connectedCount += getCheckResult(line);   
            }    //  �����������=23ms TTL=62����������,���ֵĴ���=���Դ����򷵻���  
             return connectedCount == pingTimes;  
        }  catch (Exception ex) {   
            ex.printStackTrace();    //  �����쳣�򷵻ؼ�  
             return  false;  
        }  finally {   
             try {    
                in.close();   
            }  catch (IOException e) {    
                e.printStackTrace();   
            }  
        }
    }
     // ��line����=18ms TTL=16����,˵���Ѿ�pingͨ,����1,��t����0.
     private  static  int getCheckResult(String line) {   //  System.out.println("����̨����Ľ��Ϊ:"+line);  
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(line);  
         while (matcher.find()) {
             return 1;
        }
         return 0; 
    }
     public  static  void main(String[] args)  throws Exception {
        //String ipAddress = "127.0.0.1";
    	String ipAddress = JOptionPane.showInputDialog("������Ҫ���ʵ�IP��ַ/����");
        System.out.println(ping(ipAddress));
        ping02(ipAddress);
        System.out.println(ping(ipAddress, 5, 5000));
    }
}