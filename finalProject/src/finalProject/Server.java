package finalProject;
import java.io.*;
import java.net.*;

public class Server implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataOutputStream clientOutput;
	private BufferedReader clientInput;
	
	Server(int port) {
		this.port = port;
	}
	
	public void listen() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.print("Server running at port ");
			System.out.println(port);
			clientSocket = serverSocket.accept();
			clientOutput = new DataOutputStream(clientSocket.getOutputStream());
			Thread listener = new Thread(this);
			listener.start();
			serverSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void run() {
		try {
			clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while(true) {
				String clientText = clientInput.readLine();
				if(clientText == null || clientText.equals("exit")) break;
				else System.out.println(clientText); 
			}
			clientInput.close();
			clientOutput.close();
			clientSocket.close();
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public void send(String str) {
		try {
			clientOutput.writeBytes(str + "\n");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
}
