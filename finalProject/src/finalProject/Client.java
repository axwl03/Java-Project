package finalProject;
import java.io.*;
import java.net.*;

public class Client implements Runnable {
	private Socket clientSocket;
	private DataOutputStream serverOutput;
	private BufferedReader serverInput;
	
	public void connect() {
		try {
			clientSocket = new Socket("127.0.0.1", 8000);
			System.out.println("Connected");
			Thread listener = new Thread(this);
			listener.start();
			serverOutput = new DataOutputStream(clientSocket.getOutputStream());
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void run() {
		try {
			serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while(true) {
				String serverText = serverInput.readLine();
				if(serverText == null || serverText.equals("exit")) break;
				else System.out.println("serverText: " + serverText);
			}
			serverInput.close();
			serverOutput.close();
			clientSocket.close();
		} catch(Exception e) {
			System.out.println("Exception in run()");
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public void send(String str) {
		try {
			serverOutput.writeBytes(str + "\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
