package assignment7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	
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
		TextArea ta = new TextArea();
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(pane1);
		
		// handle action event
				tf.setOnAction(e -> {
					try {
						// get the message from the text field
						String message = tf.getText();
						// send the message to the server
						toServer.writeChars(message);
						toServer.flush();
						// read message: get the message from the server
						@SuppressWarnings("deprecation")
						String remssg = fromServer.readLine();
						// display to the text area
						ta.appendText("Mssg received from the server is: " + remssg + "\n");
					} catch (IOException ex) {
						System.err.println(ex);
					}
				});
				Button sendBt = new Button("Send");
				sendBt.setOnAction(e -> {
					try {
						// get the message from the text field
						String message = tf.getText();
						// send the message to the server
						toServer.writeChars(message);
						toServer.flush();
						// read message: get the message from the server
						@SuppressWarnings("deprecation")
						String remssg = fromServer.readLine();
						// display to the text area
						ta.appendText("Mssg received from the server is: " + remssg + "\n");
					} catch (IOException ex) {
						System.err.println(ex);
					}
				});
				
				pane1.getChildren().addAll(new Label("Enter a message: "), tf, sendBt);
		return new Scene(mainPane);
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
		
		
		// lisa's code: ChatClient.java - day21network/observer
		try {
			// request connection: create a socket to connect to the server
			@SuppressWarnings("resource")
			Socket sock = new Socket("127.0.0.1", 4242);
			// input from server: create an input stream to receive data from
			// the server
			fromServer = new DataInputStream(sock.getInputStream());
			toServer = new DataOutputStream(sock.getOutputStream());
		} catch (IOException ex) {
			//ta.appendText(ex.toString() + "\n");
			/*
			 * lisa: InputStreamReader streamReader = new
			 * InputStreamReader(sock.getInputStream()); reader = new
			 * BufferedReader(streamReader); writer = new
			 * PrintWriter(sock.getOutputStream()); System.out.println(
			 * "networking established"); Thread readerThread = new Thread(new
			 * IncomingReader()); readerThread.start();
			 */
		}
	}
}
