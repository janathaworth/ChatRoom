package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	
	public void newClient(String user) {
		String fileName = "users.txt";
		try {
	        FileWriter fileWriter = new FileWriter(fileName, true);
	        String[] info = user.split(" ");
	      fileWriter.write(info[0] + " " + info[1] + "\n");
	       fileWriter.close();
	    }
	    catch(IOException ex) {
	   	 ex.printStackTrace();
	    }
	}
	
	public static ArrayList<String> getUsers() {
		ArrayList<String> users = new ArrayList<String>(); 
		String fileName = "users.txt";
		String line = null; 
		try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader =  new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) {
            	System.out.println("line " + line);
            	String[] split = line.split(" ");
                users.add(split[0]);
            }   
            bufferedReader.close();         
        }
        catch(Exception e){
           	e.printStackTrace();
        }
		System.out.println(users);
		return users; 
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
					if (message.contains("tparsemet")) {
						newClient(message.substring(9)); 
						message = message.split(" ")[0];
					}
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


