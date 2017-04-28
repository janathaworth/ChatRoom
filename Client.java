package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import junit.framework.Test;

public class Client  {
	ArrayList<String> online; 
	int port = 4242;
	
	ListView<String> list;
	private BufferedReader reader; 
	PrintWriter writer;
	TextArea ta;
	TextField  tf;

	String name; 
	
	public Client(String name, Stage s) {
		this.name = name; 
		s = new Stage(); 
		Scene scene = getScene(); 
		s.setTitle(name); // Set the stage title
		s.setScene(scene); // Place the scene in the stage
		s.show(); // Display the stage
//		writer.println("tparsemet" + name);
//		writer.flush();
		online = new ArrayList<String>(); 
	}
	
	public Scene getScene() {
		
		try {
			Socket sock = new Socket("10.146.204.23", 4242);
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
		//pane1.setStyle("-fx-border-color: green");
		pane1.setAlignment(Pos.CENTER);
		tf = new TextField();
		VBox v = new VBox(); 
		ta = new TextArea();
		ta.setPrefRowCount(23);
		ta.setEditable(false);
		TextArea clients = new TextArea(); 
		clients.setEditable(false);
		clients.setPrefColumnCount(14);
		//ScrollPane online = new ScrollPane(clients);
		
//		MenuBar menuBar = new MenuBar(); 
//		ScrollPane online = new ScrollPane();
		
		 list = new ListView<>();

		ObservableList<String> items = FXCollections.observableArrayList();
		items.add("Online Users:");
//		ArrayList<String> users = Server.getUsers(); 
//		for (String name : users) {
//			if (!name.equals(this.name)){
//				items.add(name);
//			}
//		}
		writer.println("users");
		list.setItems(items);
		border.setLeft(list);
		pane.getChildren().add( v);
		
		String receiver = ""; 
		list.addEventHandler(MouseEvent.MOUSE_CLICKED, 
		    new EventHandler<MouseEvent>() {
			String lastClick; 
	        @Override public void handle(MouseEvent e) {
	        	String thisClick = list.getSelectionModel().getSelectedItem();
	        	//if (lastClick == null || !lastClick.equals(thisClick)) {
	        	if (thisClick != null) {
	        		writer.println("update:" + name + ":" + thisClick );
	        		writer.flush(); 
	        		writer.print( thisClick + "ttt");
	        	}
	        		
	        	//}
	        }
		});
	   
		// handle action event
		tf.setOnAction(e -> {
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			// send the message to the server
			writer.println(name + ": " + message);
			writer.flush();
		});
//		ImageView cry = new ImageView(
//			      new Image("http://allenage.com/wp-content/uploads/2016/03/crying-emoji.jpg?15b286")
//		);
		ImageView smileImage = new ImageView(
			      new Image("http://clipart-library.com/images/6Tyoad7Rc.jpg")
		);
		smileImage.setFitWidth(40);
		smileImage.setPreserveRatio(true);
		smileImage.setSmooth(true);
		Button emojiBt = new Button("" , smileImage);
		//emojiBt.setContentDisplay(ContentDisplay.TOP);
		emojiBt.setOnAction(e -> {
			writer.println(smileImage);
			writer.flush(); 
		});
		emojiBt.setStyle("-fx-background-color: transparent;");	//gray shades: http://www.computerhope.com/cgi-bin/htmlcolor.pl?c=C0C0C0
		
		DropShadow shadow = new DropShadow();
		//Adding the shadow when the mouse cursor is on
		emojiBt.addEventHandler(MouseEvent.MOUSE_ENTERED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		            emojiBt.setEffect(shadow);
		        }
		});
		//Removing the shadow when the mouse cursor is off
		emojiBt.addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		            emojiBt.setEffect(null);
		        }
		});
		
		Button sendBt = new Button("Send");
		sendBt.setStyle("-fx-background-color: #55ffff");
		sendBt.setOnAction(e -> {
			//SOUND
//			AudioClip aud = new AudioClip(
//					"https://www.soundjay.com/button/sounds/button-09.mp3");
//		     aud.play();
//			 aud.stop();
			// get the message from the text field
			String message = tf.getText();
			tf.clear();
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			writer.println(name + ": " + message);
			writer.println("                                       " + timeStamp);
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
		//Removing the shadow when the mouse cursor is off
		sendBt.addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		        	sendBt.setEffect(null);
		        }
		});
		
		//String image = "http://allenage.com/wp-content/uploads/2016/03/crying-emoji.jpg?15b286"; 
		pane1.getChildren().addAll(new Label("Enter a message: "), tf, emojiBt, sendBt);
	    FlowPane.setMargin(emojiBt,new Insets(0));

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
				String check = "tparsemet" + name;
				while (message != null) {
					//System.out.println(message);
					if (!message.equals(check)) {
						if (message.contains("tparsemet")) {
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
						
						else if (message.contains("ttt")) {
							//System.out.println(message);
							String[] parts = message.split("ttt");
							int i = 1; 
							while (parts[i - 1].equals(parts[i])) {
								i++; 
							}
							receiver = parts[i - 1];
							sender = parts[i].split(":")[0];
							if (name.equals(receiver) || name.equals(sender) ) {
								ta.appendText(parts[i] + "\n");
							}
							//System.out.println(Arrays.toString(parts));
						}
						else if (message.contains("update")) {
							String[] incoming = message.split(":");
							System.out.println(Arrays.toString(incoming));
							System.out.println("length is " + incoming.length);
							String sent = incoming[1];
							String to = incoming[2];
							if (incoming.length <= 3 && name.equals(sent)) {
								ta.setText("");
							}
							else if (name.equals(sent)) {
								ta.setText(incoming[3]);
							}
						}
						else {
							//System.out.println(receiver);
							if (receiver.equals(name)) {
								ta.appendText(message + "\n");
							}
							
						}
					}
					message = reader.readLine(); 
				}
				System.out.println(message);
				} 
			catch (IOException ex) { ex.printStackTrace(); }
			}
	}
}
