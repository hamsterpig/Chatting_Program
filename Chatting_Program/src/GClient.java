import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

public class GClient extends JFrame implements ActionListener{
	
	public static final int NORMAL = 0;
	public static final int EXCEPTIONAL = -1;
	
	private JTextField input;
	private JTextArea display;
	private BufferedReader br;
	private PrintWriter pw;
	private Socket sock;
	
	public GClient(){
		init();
		connect();
		setDisplay();
		addListeners();
		showFrame();
	}
	
	private void init(){
		input = new JTextField();
		input.setBorder(new TitledBorder("input"));
		display = new JTextArea();
		display.setEditable(false);
	}
	
	private void setDisplay(){
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(new JScrollPane(display));
		pnlCenter.setBorder(new TitledBorder("Chat"));
		add(pnlCenter, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
	}
	
	private void addListeners(){
		input.addActionListener(this);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				pw.println("/quit");
				pw.flush();
			}
		});
	}
	
	private void showFrame(){
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(500, 600);
		this.setResizable(false);
		this.setVisible(true);
		input.requestFocus();
	}
	
	private void connect(){
		String ip = null;
		do{
			ip = JOptionPane.showInputDialog(this, "Please Input IP");
		}while(ip == null || ip.equals(""));
		
		String id = null;
		do{
			id = JOptionPane.showInputDialog(this, "Please Input Name");
		}while(id == null || id.equals(""));
			
		try{
			sock = new Socket(ip, 10001);
			pw = new PrintWriter(
					new OutputStreamWriter(sock.getOutputStream()));
			
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			
			pw.println(id.trim());
			pw.flush();
			WinInputThread wit = new WinInputThread();
			wit.start();
		}catch(Exception e){
			System.out.println("ERROR");
			System.out.println(e);
			System.exit(EXCEPTIONAL);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == input){
			String msg = input.getText();
			pw.println(msg);
			pw.flush();
			input.selectAll();
			input.requestFocus();
		}
	}
	
	class WinInputThread extends Thread{
		public void run(){
			try{
				String line = null;
				while((line = br.readLine()) != null){
					if(line.equals("/quit")){
						throw new Exception();
					}
					display.append(line + "\n");
					// 커서 위치 조절(스크롤 문제)
					display.setCaretPosition(
							display.getDocument().getLength());
				}
			}catch(Exception e){
				System.out.println("Client thread :" + e);
				JOptionPane.showMessageDialog(GClient.this, "EXIT");
			}finally{
				try{
					if(br != null) br.close();
					if(pw != null) pw.close();
					if(sock != null) sock.close();
					
					
				}catch(Exception e){}
				System.exit(NORMAL);
			}
		}
	}
	
	public static void main(String args[]){
		new GClient();
	}
}