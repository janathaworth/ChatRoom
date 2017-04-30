package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class Client  {
	ArrayList<String> online; 
	int port = 4242;
	
	ListView<String> list;
	private BufferedReader reader; 
	PrintWriter writer;
	TextArea ta;
	TextField  tf;
	String name; 
	String lastClick;
	
	public Client(String name, Stage s) {
		this.name = name; 
		s = new Stage(); 
		Scene scene = getScene(); 
		s.setTitle(name); // Set the stage title
		s.setScene(scene); // Place the scene in the stage
		s.show(); // Display the stage
		online = new ArrayList<String>(); 
	}
	
	public Scene getScene() {
		
		try {
			Socket sock = new Socket("10.146.239.174", 4242); //10.146.204.23
			writer = new PrintWriter(sock.getOutputStream());
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			Thread readerThread = new Thread(new IncomingReader()); 
			readerThread.start();
			}
		catch (Exception e) {
					e.printStackTrace(); 
		}
		
		// Panel p to hold the label and text field
		// Create UI
		BorderPane border = new BorderPane(); 
		FlowPane pane = new FlowPane();
		FlowPane pane1 = new FlowPane();
		pane1.setHgap(4);
		pane1.setAlignment(Pos.CENTER);
		tf = new TextField();
		VBox v = new VBox(); 
		ta = new TextArea();
		ta.setPrefRowCount(23);
		ta.setEditable(false);
		TextArea clients = new TextArea(); 
		clients.setEditable(false);
		clients.setPrefColumnCount(14);
		list = new ListView<>();

		ObservableList<String> items = FXCollections.observableArrayList();
		items.add("Everyone");
		writer.println("users");
		list.setItems(items);
		border.setLeft(list);
		pane.getChildren().add( v);
		
		String receiver = ""; 
		list.addEventHandler(MouseEvent.MOUSE_CLICKED, 
		    new EventHandler<MouseEvent>() { 
	        @Override public void handle(MouseEvent e) {
	        	String thisClick = list.getSelectionModel().getSelectedItem();
	        	lastClick = thisClick; 
	        	if (thisClick != null) {
	        		ta.setText("");
	        		writer.println("update:" + name + ":" + thisClick );
	        		writer.flush(); 
	        	}
	        		
	        	//}
	        }
		});
	   
		// handle action event
		tf.setOnAction(e -> {
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			writer.println(lastClick + "~" + name + ": " + message );
			writer.flush(); 
		});

		DropShadow shadow = new DropShadow();
		//Adding the shadow when the mouse cursor is on

		Button sendBt = new Button("Send");
		sendBt.setStyle("-fx-background-color: #55ffff");
		sendBt.setOnAction(e -> {
			String message = tf.getText();
			tf.clear();
			writer.println(lastClick + "~" + name + ": " + message );
			writer.flush(); 
		});
		
		DropShadow sendShadow = new DropShadow();
		//Adding the shadow when the mouse cursor is on
		sendBt.addEventHandler(MouseEvent.MOUSE_ENTERED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        	sendBt.setEffect(shadow);
		        }
		});
	//	Removing the shadow when the mouse cursor is off
		sendBt.addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        	sendBt.setEffect(null);
		        }
		});
		pane1.getChildren().addAll(new Label("Enter a message: "), tf, sendBt);
		v.getChildren().addAll(new ScrollPane(ta), pane1);
		border.setRight(pane);
		return new Scene(border);
	}

	class IncomingReader implements Runnable {
		String message; 
		String receiver = ""; 
		String sender = ""; 
		public void run() {  
			try {
				message = reader.readLine(); 
				String check = "~parseme~" + name;
				while (message != null) {
					//System.out.println(message);
					if (!message.equals(check)) {
						if (message.contains("~parseme~")) {
							String not = message.substring(9);
							if (!online.contains(not)) {
								online.add(not);
								Platform.runLater(new Runnable() {
					                 @Override public void run() {
					                	 list.getItems().add(not);
					                 }
								});
							}
						}
						
						else if (message.contains("~")) {
							//System.out.println(message);
							String[] parts = message.split("~");
							int i = 1; 
							while (parts[i - 1].equals(parts[i])) {
								i++; 
							}
							receiver = parts[i - 1];
							sender = parts[i].split(":")[0];
							if (( lastClick != null && (name.equals(receiver) && lastClick.equals(sender)) )|| name.equals(sender) ) {
								System.out.println(Arrays.toString(parts));
								ta.appendText(parts[i].split("tmstp")[0] + "\n");
							}
							if(receiver.equals("Everyone") && !name.equals(sender) && lastClick.equals("Everyone")) {
								ta.appendText(parts[i].split("tmstp")[0] + "\n");
							}
							//System.out.println(Arrays.toString(parts));
						}
						else if (message.contains("update")) {
							String[] incoming = message.split("::");
							System.out.println(Arrays.toString(incoming));
							System.out.println("length is " + incoming.length);
							String sent = incoming[1];
							String to = incoming[2];
							if (incoming.length <= 3 && name.equals(sent)) {
								ta.setText("");
							}
							else if (name.equals(sent)) {
								ta.appendText(incoming[3] + "\n");
							}
						}
					}
					System.out.println("received " + message);
					message = reader.readLine(); 
				}
				} 
			catch (IOException ex) { ex.printStackTrace(); }
			}
	}
}
