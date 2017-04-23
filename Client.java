package assignment7;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Client extends Application {
	int port = 4242;
	// IO streams
//	DataOutputStream toServer = null;
//	DataInputStream fromServer = null;
	
	private BufferedReader reader; 
	private PrintWriter writer;
	TextArea ta;
	
	public void runMe(){
		launch();
	}
	
	public Scene getScene() {
		// Panel p to hold the label and text field
		// Create UI
		FlowPane pane1 = new FlowPane();
		pane1.setHgap(4);
		pane1.setStyle("-fx-border-color: green");
		TextField tf = new TextField();
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		BorderPane mainPane = new BorderPane();
		// Text area to display contents
		ta = new TextArea();
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(pane1);
		
		// handle action event
				tf.setOnAction(e -> {
					// get the message from the text field
					String message = tf.getText();
					// send the message to the server
					writer.println(message);
					writer.flush();
					// read message: get the message from the server
//				@SuppressWarnings("deprecation")
					//String remssg = fromServer.readLine();
//						String remssg = reader.readLine(); 
//						// display to the text area
//						ta.appendText("Mssg received from the server is: " + remssg + "\n");
				});
				
				Button sendBt = new Button("Send");
				sendBt.setOnAction(e -> {
					// get the message from the text field
					String message = tf.getText();
					// send the message to the server
//						toServer.writeChars(message);
//						toServer.flush();
					writer.println(message);
					System.out.println(message);
					writer.flush(); 
					// read message: get the message from the server
//					@SuppressWarnings("deprecation")
					//String remssg = fromServer.readLine();
				});
				
				pane1.getChildren().addAll(new Label("Enter a message: "), tf, sendBt);
				Socket sock;
				try {
					sock = new Socket("127.0.0.1", 4242);
				writer = new PrintWriter(sock.getOutputStream());
				InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(streamReader);
				Thread readerThread = new Thread(new IncomingReader()); 
				readerThread.start();
				}
				catch (Exception e) {
					e.printStackTrace(); 
				}
				
		return new Scene(mainPane);
	}
	
	public void newCustomer() {
		
	}
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		
	
		// Create a scene and place it in the stage
		
		Scene scene = getScene(); 
		primaryStage.setTitle("Client A"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		
		Stage secondaryStage = new Stage(); 
		secondaryStage.setTitle("Client B");
		secondaryStage.setScene(getScene());
		secondaryStage.show(); 
		
		
//		// lisa's code: ChatClient.java - day21network/observer
//		try {
//			// request connection: create a socket to connect to the server
//			@SuppressWarnings("resource")
//	
//			
//		} catch (IOException ex) {
//			//ta.appendText(ex.toString() + "\n");
//			/*
//			 * lisa: InputStreamReader streamReader = new
//			 * InputStreamReader(sock.getInputStream()); reader = new
//			 * BufferedReader(streamReader); writer = new
//			 * PrintWriter(sock.getOutputStream()); System.out.println(
//			 * "networking established"); Thread readerThread = new Thread(new
//			 * IncomingReader()); readerThread.start();
//			 */
//		}
	}
	
	class IncomingReader implements Runnable {
		public void run() { 
			try {
				String message = reader.readLine(); 
				while (message != null) {
					ta.appendText(message + "\n");
					message = reader.readLine(); 
				}	
			} 
			catch (IOException ex) { ex.printStackTrace(); }
			}
	}
}
