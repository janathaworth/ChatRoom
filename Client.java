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

public class Client  {
	int port = 4242;
	
	private BufferedReader reader; 
	private PrintWriter writer;
	TextArea ta;
	TextField  tf;

	String name; 
	
	public Client(String name) {
		this.name = name; 
		Stage s = new Stage(); 
		Scene scene = getScene(); 
		s.setTitle(name); // Set the stage title
		s.setScene(scene); // Place the scene in the stage
		s.show(); // Display the stage
		
		
	}
	
	public Scene getScene() {
		// Panel p to hold the label and text field
		// Create UI
		FlowPane pane1 = new FlowPane();
		pane1.setHgap(4);
		pane1.setStyle("-fx-border-color: green");
		tf = new TextField();
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
			tf.clear();
			// send the message to the server
			writer.println(name + ": " + message);
			writer.flush();
		});
				
		Button sendBt = new Button("Send");
		sendBt.setOnAction(e -> {
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			writer.println(name + ": " +  message);
			//System.out.println(message);
			writer.flush(); 

		});
				
		pane1.getChildren().addAll(new Label("Enter a message: "), tf, sendBt);
		Socket sock;
		try {
			sock = new Socket("10.146.144.25", 4242);
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
