package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class Server extends Observable {
	
	public void setUp() throws IOException{
		ServerSocket serverSocket = new ServerSocket(4242);
		while(true) {
			Socket clientSocket = serverSocket.accept();
			ClientObserver client = new ClientObserver(clientSocket.getOutputStream());
			addObserver(client);
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
		 }
	}
	
	class ClientHandler implements Runnable {
		BufferedReader reader; 

		public ClientHandler(Socket clientSocket) {
			try {
				InputStreamReader stream = new InputStreamReader(clientSocket.getInputStream());
				reader = new BufferedReader(stream); 
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				String message = reader.readLine(); 
				//System.out.println("recevied: " + message);
				while (message != null) {
					setChanged();
					notifyObservers(message);
					message = reader.readLine(); 
				}
			}
			catch (IOException e) {
				e.printStackTrace(); 
			}
		}
	}
}


