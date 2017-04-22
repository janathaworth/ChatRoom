package assignment7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public void setUp() throws IOException{
		ServerSocket serverSocket = new ServerSocket(4242);
		while(true) {
			Socket clientSocket = serverSocket.accept();
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
		 }
	}

}
