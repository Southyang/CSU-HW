package game;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class makefile {

	public static void main(String[] args) throws Exception{
		FileWriter fw = new FileWriter("f:\\�������ĵ�\\javaworkfile\\info.txt",true);
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		System.out.println("������Ϣ����:");
		String msg = br.readLine();//+"\n";
		fw.write(msg+"\n");
		fw.close();

	}

}
