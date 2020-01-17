import java.net.*;
import java.io.*;
import javax.swing.*;

public class Client{
	private Socket sock = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	
	public Client(){
		String ip = JOptionPane.showInputDialog("������ IP�� �Է��ϼ���");
		String id = JOptionPane.showInputDialog("����� ID�� �Է��ϼ���");
		
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
	
		
		System.out.println("Ŭ���̾�Ʈ ����");
		System.exit(0);
	}// exit
	
	public static void main(String args[]){
		new Client();
	}
}