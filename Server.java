package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Server extends Observable {

	public void setUp() throws IOException{
		ServerSocket serverSocket = new ServerSocket(4242);
		//comment out until while loop if want to keep users
		PrintWriter pw;
		try {
			pw = new PrintWriter("users.txt");
			pw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	

	
//	public static ArrayList<String> getUsers() {
//
//		ArrayList<String> users = new ArrayList<String>(); 
//		String fileName = "users.txt";
//		String line = null; 
//		try {
//            FileReader fileReader = new FileReader(fileName);
//            BufferedReader bufferedReader =  new BufferedReader(fileReader);
//            
//            while((line = bufferedReader.readLine()) != null) {
//            	System.out.println("line " + line);
//            	String[] split = line.split(" ");
//                users.add(split[0]);
//            }   
//            bufferedReader.close();         
//        }
//        catch(Exception e){
//           	e.printStackTrace();
//        }
//		System.out.println(users);
//		return users; 
//	}
//	
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
				System.out.println("recevied: " + message);
				while (message != null) {
					if (message.contains("users")) {
						String fileName = "users.txt";
						String line = null; 
						try {
				            FileReader fileReader = new FileReader(fileName);
				            BufferedReader bufferedReader =  new BufferedReader(fileReader);
				            
				            while((line = bufferedReader.readLine()) != null) {
				            	String[] split = line.split(" ");
				            	setChanged();
				                notifyObservers("tparsemet" + split[0]);
				            }   
				            bufferedReader.close();         
				        }
				        catch(Exception e){
				           	e.printStackTrace();
				        }
					}
					else if (message.contains("update")) {
						String request = message.split(":")[2];
						String sender = message.split(":")[1];
						String fileName = sender + request + ".txt";
						String line = null; 
						String convo = ""; 
						FileReader fileReader; 
						File f = new File(fileName);
						f.createNewFile(); 
						fileReader = new FileReader(fileName);
						System.out.println("reading from: " + sender + request );
						BufferedReader bufferedReader =  new BufferedReader(fileReader);
						while((line = bufferedReader.readLine()) != null) {
							message = "update::" + sender + "::" + request + "::" +  line;
							setChanged();
							System.out.println("sent" + message);
			                notifyObservers(message);
								
			            }   
						
						
					}
					else if (message.contains("ttt")) {
						String[] parts = message.split("ttt");
						String receiver = parts[0];
						String sender = parts[1].split(":")[0];
						FileWriter fileWriter = new FileWriter(sender + receiver + ".txt", true);
					    fileWriter.write(parts[1] + "\n");
					    fileWriter.close();
					    
					    FileWriter fileWriter2 = new FileWriter(receiver + sender + ".txt", true);
					    fileWriter2.write(parts[1] + "\n");
					    fileWriter2.close();
					    
					    setChanged();
						notifyObservers(message);
						
					}
					else {
						if (message.contains("tparsemet")) {
							newClient(message.substring(9)); 
							message = message.split(" ")[0];
						}
						
						setChanged();
						notifyObservers(message);
					}
					message = reader.readLine(); 
					
				}
			}
			catch (IOException e) {
				e.printStackTrace(); 
			}
		}
	}
}


