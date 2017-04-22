package assignment7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public void setUp() {
		try {
			ServerSocket serverSock = new ServerSocket(4242);
			while(true) {
				Socket clientSocket = serverSock.accept();
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			 }
		}
		
		catch (IOException e) {
			e.printStackTrace(); 
		}
	}

}
