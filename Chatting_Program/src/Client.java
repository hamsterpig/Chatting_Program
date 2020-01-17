import java.net.*;
import java.io.*;
import javax.swing.*;

public class Client{
	private Socket sock = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	
	public Client(){
		String ip = JOptionPane.showInputDialog("접속할 IP를 입력하세요");
		String id = JOptionPane.showInputDialog("사용자 ID를 입력하세요");
		
		if(ip.length() == 0 || id.length() == 0){
			System.out.println("ERROR");
			System.exit(-1);
		}
		try{
			sock = new Socket(ip, 10001);
			
			pw = new PrintWriter(
					new OutputStreamWriter(sock.getOutputStream()));
			
			br = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			
			BufferedReader keyboard = new BufferedReader(
					new InputStreamReader(System.in));
			
			pw.println(id);
			pw.flush();
			
			new Thread(){
				public void run(){
					try{
						String line = null;
						while((line = br.readLine()) != null){
							if(line.equals("/quit"))
								throw new Exception();
							
							System.out.println(line);
						}
					}catch(Exception e){
						System.out.println("In Thread catch");
						System.out.println(e);
					}finally{
						exit();
					}
				}
			}.start();
			
			String line = null;
			
			while((line = keyboard.readLine()) != null){
				pw.println(line);
				pw.flush();
			}
			
			
			
		}catch(Exception e){
			System.out.println("Create Thread catch");
			System.out.println(e);
		}finally{
			exit();	
		}
		
		
	} // constructor
	
	public void exit(){
		
		try{
			if(pw != null)
				pw.close();
			if(br != null)
				br.close();
			if(sock != null)
				sock.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
		System.out.println("클라이언트 종료");
		System.exit(0);
	}// exit
	
	public static void main(String args[]){
		new Client();
	}
}