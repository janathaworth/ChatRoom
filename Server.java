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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Server extends Observable {

	public void setUp() throws IOException{
		ServerSocket serverSocket = new ServerSocket(4242);
		//comment out until while loop if want to keep users
//		PrintWriter pw;
//		try {
//			pw = new PrintWriter("users.txt");
//			pw.close();
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
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
					if (message.contains("users")) {
						String fileName = "users.txt";
						String line = null; 
						Boolean pass = false; 
						Boolean log = false; 
						
						if (message.contains("pass")) {
							pass  = true; 
						}
						if(message.contains("log")){
							log = true;
						}
						try {
				            FileReader fileReader = new FileReader(fileName);
				            BufferedReader bufferedReader =  new BufferedReader(fileReader);
				            
				            while((line = bufferedReader.readLine()) != null) {
				            	String[] split = line.split(" ");
				            	setChanged();
				            	if (pass) {
				            		notifyObservers("1parseme1" + message.split(" ")[1] + line);
				            	}
				            	else {
				            		 notifyObservers("~parseme~" + split[0]);
				            	}
				               
				            }
				            if (log) {
				            	setChanged();
				            	notifyObservers("2parse2");
				            }
				            else if (pass) {
				            	setChanged();
				            	notifyObservers("1parse1");
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
						String fileName; 
						if (request.equals("Everyone")) {
							fileName = "Everyone.txt";
							System.out.println("requested everyone");
						}
						else {
							fileName = sender + request + ".txt";
						}
						
						String line = null; 
						String convo = ""; 
						FileReader fileReader; 
						File f = new File(fileName);
						f.createNewFile(); 
						fileReader = new FileReader(fileName);
						System.out.println("reading from: " + fileName );
						BufferedReader bufferedReader =  new BufferedReader(fileReader);
						while((line = bufferedReader.readLine()) != null) {
							message = "update::" + sender + "::" + request + "::" +  line;
							setChanged();
							System.out.println("sent" + message);
			                notifyObservers(message);
								
			            }   
						
						
					}
					else if (message.contains("~parseme~")) {
						newClient(message.substring(9)); 
						message = message.split(" ")[0];
						setChanged();
						notifyObservers(message);
					}
					else if (message.contains("*")) {
						message = message.substring(1);
						setChanged(); 
						notifyObservers("~parseme~" + message);
					}
					else  {
						setChanged();
						notifyObservers(message);
						
						String[] parts = message.split("~");
						String receiver = parts[0];
						System.out.println("parts " + Arrays.toString(parts));
						String sender = parts[1].split(":")[0];
						
						if (receiver.equals("Everyone")) {
							FileWriter fileWriter = new FileWriter("Everyone.txt", true);
						    fileWriter.write(parts[1] + "\n");
						    fileWriter.close();
						}
						else {
							FileWriter fileWriter = new FileWriter(sender + receiver + ".txt", true);
						    fileWriter.write(parts[1] + "\n");
						    fileWriter.close();
						    
						    FileWriter fileWriter2 = new FileWriter(receiver + sender + ".txt", true);
						    fileWriter2.write(parts[1] + "\n");
						    fileWriter2.close();
							
						}
						
						
//						setChanged();
//						notifyObservers(message);
					}
					System.out.println("recevied: " + message);
					message = reader.readLine(); 
					
				}
			}
			catch (IOException e) {
				e.printStackTrace(); 
			}
		}
	}
}


